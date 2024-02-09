import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;

// Interfaccia Observer
interface VictoryObserver {
    void notifyVictory();
}

public class Game extends JFrame {

    private Room room;
    private int playerX;
    private int playerY;
    private boolean victoryAchieved = false;
    private List<VictoryObserver> victoryObservers = new ArrayList<>();

    public Game(Room room) {
        this.room = room;
        Character thief = CharacterFactory.createCharacter(CharacterFactory.CharacterType.THIEF);
        this.playerX = thief.get_X(room);
        this.playerY = thief.get_Y(room);

        setTitle("Game");
        int hSize = 22;
        int wSize = 21;
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
    }

    private void handleKeyPress(KeyEvent e) {
        if (victoryAchieved) {
            return;
        }

        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_UP && playerX > 0 && room.matrix[playerX - 1][playerY] != Color.BLACK) {
            playerX--;
        } else if (keyCode == KeyEvent.VK_DOWN && playerX < room.matrix.length - 1 && room.matrix[playerX + 1][playerY] != Color.BLACK) {
            playerX++;
        } else if (keyCode == KeyEvent.VK_LEFT && playerY > 0 && room.matrix[playerX][playerY - 1] != Color.BLACK) {
            playerY--;
        } else if (keyCode == KeyEvent.VK_RIGHT && playerY < room.matrix[0].length - 1 && room.matrix[playerX][playerY + 1] != Color.BLACK) {
            playerY++;
        }

        for (Point exitPoint : room.exit) {
            if (exitPoint.x == playerX && exitPoint.y == playerY) {
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
    }

    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawMatrix(g);
            drawPlayer(g);

            if (victoryAchieved) {
                drawVictoryMessage(g);
            }
        }

        private void drawMatrix(Graphics g) {
            int cellSize = 20;

            for (int i = 0; i < room.row; i++) {
                for (int j = 0; j < room.matrix[i].length; j++) {
                    g.setColor(room.matrix[i][j]);
                    g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(j * cellSize, i * cellSize, cellSize, cellSize);
                }
            }
        }

        private void drawPlayer(Graphics g) {
            int cellSize = 20;
            g.setFont(new Font("Segoe UI Emoji", Font.PLAIN, cellSize));
            g.drawString("\uD83C\uDFC3", playerY * cellSize, (playerX + 1) * cellSize);
        }

        private void drawVictoryMessage(Graphics g) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Hai vinto!", 100, 100);
        }
    }
}
