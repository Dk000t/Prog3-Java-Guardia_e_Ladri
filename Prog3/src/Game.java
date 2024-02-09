import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Game extends JFrame {
    private Thief thief;
    private Room room;
    private int playerX;
    private int playerY;

    public Game(Room room) {
        this.room = room;
        Character thief = CharacterFactory.createCharacter(CharacterFactory.CharacterType.THIEF);        this.playerX = thief.get_X(room);
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
        int keyCode = e.getKeyCode();

        // Verifica se il personaggio è nella prima riga prima di muoversi verso l'alto
        if (keyCode == KeyEvent.VK_UP && playerX > 0 && room.matrix[playerX - 1][playerY] != Color.BLACK) {
            playerX--;
        } else if (keyCode == KeyEvent.VK_DOWN && playerX < room.matrix.length - 1 && room.matrix[playerX + 1][playerY] != Color.BLACK) {
            playerX++;
        } else if (keyCode == KeyEvent.VK_LEFT && playerY > 0 && room.matrix[playerX][playerY - 1] != Color.BLACK) {
            playerY--;
        } else if (keyCode == KeyEvent.VK_RIGHT && playerY < room.matrix[0].length - 1 && room.matrix[playerX][playerY + 1] != Color.BLACK) {
            playerY++;
        }

        // Ridisegna il pannello di gioco dopo il movimento
        repaint();
    }

    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawMatrix(g);
            drawPlayer(g);
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
            // Usa il carattere Unicode dell'emoji di una persona che corre
            g.setFont(new Font("Segoe UI Emoji", Font.PLAIN, cellSize));
            g.drawString("\uD83C\uDFC3", playerY * cellSize, (playerX + 1) * cellSize);
        }
    }
}
