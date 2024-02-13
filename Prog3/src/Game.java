import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Interfaccia Observer
interface VictoryObserver {
    void notifyVictory();
}

public class Game extends JFrame {

    private Room room;
    private static int thief_x, thief_y;
    private static int guard_x, guard_y;
    private boolean victoryAchieved = false;
    private List<VictoryObserver> victoryObservers = new ArrayList<>();
    private Timer timer;

    public Game(Room room) {
        Character thief = CharacterFactory.createCharacter(CharacterFactory.CharacterType.THIEF);
        Character guard = CharacterFactory.createCharacter(CharacterFactory.CharacterType.GUARD);
        int[] thief_coordinate = thief.get_Coordinate(room);
        int[] guard_coordinate = guard.get_Coordinate(room);
        this.room = room;
        thief_x = thief_coordinate[0];
        thief_y = thief_coordinate[1];
        guard_x = guard_coordinate[0];
        guard_y = guard_coordinate[1];

        setTitle("Game");
        int hSize = 25;
        int wSize = 23;
        int width = room.column * wSize;
        int height = room.row * hSize;

        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        GamePanel gamePanel = new GamePanel();
        add(gamePanel);
        gamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
        gamePanel.setFocusable(true);
        setVisible(true);

        // Timer per muovere la guardia periodicamente
        this.timer = new Timer(1000, new ActionListener() {
            Random random = new Random();
            int rand = random.nextInt(10);
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] newGuardCoordinate = chosen_movement(room,guard);
                guard_x = newGuardCoordinate[0];
                guard_y = newGuardCoordinate[1];
                repaint();
            }

        });
        timer.start();
        setVisible(true);
    }

    public int[] chosen_movement(Room room, Character guard) {
        Random random = new Random();
        int rand = random.nextInt(10);
        if (rand < 3) { // 30% dei casi
            rand_move randMoveInstance = new rand_move();
            return randMoveInstance.move(room, guard);
        } else { // 70% dei casi
            aco_move acoMoveInstance = new aco_move();
            return acoMoveInstance.move(room, guard);
        }
    }


    private void handleKeyPress(KeyEvent e) {
        if (victoryAchieved) {
            return;
        }

        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_UP && thief_x > 0 && room.matrix[thief_x - 1][thief_y] != Color.BLACK) {
            thief_x--;
        } else if (keyCode == KeyEvent.VK_DOWN && thief_x < room.matrix.length - 1 && room.matrix[thief_x + 1][thief_y] != Color.BLACK) {
            thief_x++;
        } else if (keyCode == KeyEvent.VK_LEFT && thief_y > 0 && room.matrix[thief_x][thief_y - 1] != Color.BLACK) {
            thief_y--;
        } else if (keyCode == KeyEvent.VK_RIGHT && thief_y < room.matrix[0].length - 1 && room.matrix[thief_x][thief_y + 1] != Color.BLACK) {
            thief_y++;
        }

        for (Point exitPoint : room.exit) {
            if (exitPoint.x == thief_x && exitPoint.y == thief_y) {
                victoryAchieved = true;
                notifyVictoryObservers();
                break;
            }
        }

        repaint();
    }

    public void addObserver(VictoryObserver observer) {
        victoryObservers.add(observer);
    }

    private void notifyVictoryObservers() {
        for (VictoryObserver observer : victoryObservers) {
            observer.notifyVictory();
        }
        this.timer.stop();
    }

    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawMatrix(g);
            drawThief(g);
            drawGuard(g);

            if (victoryAchieved) {
                drawVictoryMessage(g);
            }
        }

        private void drawMatrix(Graphics g) {
            int cellSize = 21;

            for (int i = 0; i <= room.row; i++) {
                for (int j = 0; j <= room.column; j++) {
                    g.setColor(room.matrix[i][j]);
                    g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(j * cellSize, i * cellSize, cellSize, cellSize);
                }
            }
        }

        private void drawThief(Graphics g) {
            int cellSize = 21;
            g.setFont(new Font("Segoe UI Emoji", Font.PLAIN, cellSize));
            g.drawString("\uD83C\uDFC3", thief_y * cellSize, (thief_x + 1) * cellSize);
        }

        private void drawGuard(Graphics g) {
            int cellSize = 21;
            g.setFont(new Font("Segoe UI Emoji", Font.PLAIN, cellSize));
            g.drawString("\uD83D\uDEE1", guard_y * cellSize, (guard_x + 1) * cellSize);
        }

        private void drawVictoryMessage(Graphics g) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Hai vinto!", 100, 100);
        }
    }
}