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

public class Game extends JFrame {

    private Room room;
    private boolean victoryAchieved = false;
    private List<Observer> victoryObservers = new ArrayList<>();
    private Timer timer;
    private boolean gameOver = false;
    static int points;
    VictoryDisplay victory = new VictoryDisplay();
    DefeatDisplay defeat = new DefeatDisplay();
    Ranking ranking = new Ranking();
    Character thief = CharacterFactory.createCharacter(CharacterFactory.CharacterType.THIEF);
    Character guard = CharacterFactory.createCharacter(CharacterFactory.CharacterType.GUARD);
    int[] thief_coordinate = thief.get_Coordinate(room);
    int[] guard_coordinate = guard.get_Coordinate(room);
    int[] current_guard_coordinate = guard_coordinate;

    public Game(Room room) {
        this.room = room;
        GamePanel gamePanel = new GamePanel(thief_coordinate, guard_coordinate);
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
                handleKeyPress(e,thief_coordinate);
            }
        });
        gamePanel.setFocusable(true);
        setVisible(true);

        // Timer per muovere la guardia periodicamente
        this.timer = new Timer(50, new ActionListener() {
            Random random = new Random();
            int rand = random.nextInt(10);
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] newGuardCoordinate = chosen_movement(room,current_guard_coordinate);
                guard_coordinate[0] = newGuardCoordinate[0];
                guard_coordinate[1] = newGuardCoordinate[1];
                current_guard_coordinate[0] = guard_coordinate[0];
                current_guard_coordinate[1] = guard_coordinate[1];
                repaint();
            }
        });
        timer.start();
        setVisible(true);
    }

    public int[] chosen_movement(Room room, int[] current_pos) {
        rand_move randMoveInstance = new rand_move();
        return randMoveInstance.move(room,current_pos);
    }
    private void handleKeyPress(KeyEvent e, int[] thief_coordinate) {
        if (victoryAchieved || gameOver) {
            return;
        }
        int keyCode = e.getKeyCode();

        // Utilizzare thief_coordinate
        if (keyCode == KeyEvent.VK_UP && thief_coordinate[0] > 0 && room.matrix[thief_coordinate[0] - 1][thief_coordinate[1]] != Color.BLACK) {
            thief_coordinate[0]--;
            points += 10; // Incremento del punteggio
        } else if (keyCode == KeyEvent.VK_DOWN && thief_coordinate[0] < room.matrix.length - 1 && room.matrix[thief_coordinate[0] + 1][thief_coordinate[1]] != Color.BLACK) {
            thief_coordinate[0]++;
            points += 10; // Incremento del punteggio
        } else if (keyCode == KeyEvent.VK_LEFT && thief_coordinate[1] > 0 && room.matrix[thief_coordinate[0]][thief_coordinate[1] - 1] != Color.BLACK) {
            thief_coordinate[1]--;
            points += 10; // Incremento del punteggio
        } else if (keyCode == KeyEvent.VK_RIGHT && thief_coordinate[1] < room.matrix[0].length - 1 && room.matrix[thief_coordinate[0]][thief_coordinate[1] + 1] != Color.BLACK) {
            thief_coordinate[1]++;
            points += 10; // Incremento del punteggio
        }

        // Verificare se il ladro ha raggiunto l'uscita
        for (Point exitPoint : room.exit) {
            if (exitPoint.x == thief_coordinate[0] && exitPoint.y == thief_coordinate[1]) {
                victoryAchieved = true;
                notifyVictoryObservers();
                ranking.RankingSave(points);
                break;
            }
        }
        repaint();
    }


    public void addObserver(Observer observer) {
        victoryObservers.add(observer);
    }

    private void notifyVictoryObservers() {
        for (Observer observer : victoryObservers) {
            observer.notify(getGraphics()); // Passa il Graphics del pannello di gioco
        }
        this.timer.stop();
    }

    private class GamePanel extends JPanel {
        private int[] thief_coordinate;
        private int[] guard_coordinate;

        public GamePanel(int[] thief_coordinate, int[] guard_coordinate) {
            this.thief_coordinate = thief_coordinate;
            this.guard_coordinate = guard_coordinate;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawMatrix(g);
            drawThief(g);
            drawGuard(g);

            if (victoryAchieved) {
                victory.notify(g);
            }
            else if (thief_coordinate[0] == guard_coordinate[0] && thief_coordinate[1] == guard_coordinate[1]) {
                defeat.notify(g);
                timer.stop();
                gameOver = true;
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

        private void drawThief(Graphics g, int[] thief_coordinate) {
            int cellSize = 21;
            g.setFont(new Font("Segoe UI Emoji", Font.PLAIN, cellSize));
            g.drawString("\uD83C\uDFC3", thief_coordinate[1] * cellSize, (thief_coordinate[0] + 1) * cellSize);
        }

        private void drawGuard(Graphics g, int[] guard_coordinate) {
            int cellSize = 21;
            g.setFont(new Font("Segoe UI Emoji", Font.PLAIN, cellSize));
            g.drawString("\uD83D\uDEE1", guard_coordinate[1] * cellSize, (guard_coordinate[0] + 1) * cellSize);
        }
    }
}