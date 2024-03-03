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
import java.util.Random;
import javax.swing.Timer;

public class Game extends JFrame {

    private final Room room;
    private static int[] thief_coordinate;
    private static int[] guard_coordinate;
    private static int[] thief_direction;
    private boolean victoryAchieved = false;
    private final List<Observer> victoryObservers = new ArrayList<>();
    private boolean gameOver = false;
    static int points;
    VictoryDisplay victory = new VictoryDisplay();
    DefeatDisplay defeat = new DefeatDisplay();
    Ranking ranking = new Ranking();
    private boolean isOnGreen = false;
    private boolean isOnYellow = false;
    private boolean isOnRed = false;
    private boolean isOnWhite = false;
    private final Timer greenTimer;
    private final Timer yellowTimer;
    Random rand = new Random();
    private Command exitCommand;



    public Game(Room room) {
        thief_direction = new int[2];
        this.room = room;
        Character thief = CharacterFactory.createCharacter(CharacterFactory.CharacterType.THIEF);
        Character guard = CharacterFactory.createCharacter(CharacterFactory.CharacterType.GUARD);
        thief_coordinate = thief.get_Coordinate(room);
        guard_coordinate = guard.get_Coordinate(room);
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
        setVisible(true);

        greenTimer = new Timer(10000, (e) -> isOnGreen = false);
        greenTimer.setRepeats(false);
        greenTimer.start();

        yellowTimer = new Timer(10000,(e) -> isOnYellow = false);
        yellowTimer.setRepeats(false);
        yellowTimer.start();
    }

    private void handleKeyPress(KeyEvent e) {
        if (victoryAchieved || gameOver) {
            return;
        }

        int keyCode = e.getKeyCode();
        boolean thiefMoved = false;

        if (keyCode == KeyEvent.VK_UP && thief_coordinate[0] > 0 && room.matrix[thief_coordinate[0] - 1][thief_coordinate[1]] != Color.BLACK) {
            thief_coordinate[0]--; //NORD
            thief_direction = new int[]{-1, 0};
            thiefMoved = true;
        } else if (keyCode == KeyEvent.VK_DOWN && thief_coordinate[0] < room.matrix.length - 1 && room.matrix[thief_coordinate[0] + 1][thief_coordinate[1]] != Color.BLACK) {
            thief_coordinate[0]++; //SUD
            thief_direction = new int[]{1, 0};
            thiefMoved = true;
        } else if (keyCode == KeyEvent.VK_LEFT && thief_coordinate[1] > 0 && room.matrix[thief_coordinate[0]][thief_coordinate[1] - 1] != Color.BLACK) {
            thief_coordinate[1]--; //OVEST
            thief_direction = new int[]{0, -1};
            thiefMoved = true;
        } else if (keyCode == KeyEvent.VK_RIGHT && thief_coordinate[1] < room.matrix[0].length - 1 && room.matrix[thief_coordinate[0]][thief_coordinate[1] + 1] != Color.BLACK) {
            thief_coordinate[1]++; //EST
            thief_direction = new int[]{0, 1};
            thiefMoved = true;
        }

        if (keyCode == KeyEvent.VK_ESCAPE) {
            if (exitCommand != null) {
                exitCommand.execute();
            }
            return;
        }

        if (thiefMoved) {
            points += 1; // Incrementa i punti di 10

            if (room.matrix[thief_coordinate[0]][thief_coordinate[1]] == Color.GREEN) {
                isOnGreen = true;
                isOnYellow = false;
                isOnRed = false;
                isOnWhite = false;
                greenTimer.restart();
                yellowTimer.stop();
            }
            else if (room.matrix[thief_coordinate[0]][thief_coordinate[1]] == Color.YELLOW) {
                isOnYellow = true;
                isOnGreen = false;
                isOnRed = false;
                isOnWhite = false;
                yellowTimer.restart();
                greenTimer.stop();
            }
            else if(room.matrix[thief_coordinate[0]][thief_coordinate[1]] == Color.RED) {
                isOnRed = true;
                isOnGreen = false;
                isOnYellow = false;
                isOnWhite = false;
                greenTimer.stop();
                yellowTimer.stop();
            }
            else if(room.matrix[thief_coordinate[0]][thief_coordinate[1]] == Color.WHITE) {
                isOnWhite = true;
                isOnGreen = false;
                isOnYellow = false;
                isOnRed = false;
                greenTimer.stop();
                yellowTimer.stop();
            }

            if (isOnYellow) {
                rand_move rand_move = new rand_move();
                int[] newGuardCoordinate = rand_move.move(guard_coordinate);
                guard_coordinate[0] = newGuardCoordinate[0];
                guard_coordinate[1] = newGuardCoordinate[1];
            }

            if (isOnGreen) {
                green_move greenMoveInstance = new green_move(guard_coordinate[0], guard_coordinate[1]);
                int[] newGuardCoordinate = greenMoveInstance.move(thief_direction);
                guard_coordinate[0] += newGuardCoordinate[0];
                guard_coordinate[1] += newGuardCoordinate[1];
            }

            if (isOnRed) {
                aco_move acoMoveInstance = new aco_move();
                acoMoveInstance.setBehavior(true);
                acoMoveInstance.setThief(thief_coordinate);
                int[] newGuardCoordinate = acoMoveInstance.move(guard_coordinate);
                guard_coordinate[0] = newGuardCoordinate[0];
                guard_coordinate[1] = newGuardCoordinate[1];
            }

            if(isOnWhite){
                switch (rand.nextInt(10)){
                    case 0,1,2: {
                        green_move greenMoveInstance = new green_move(guard_coordinate[0], guard_coordinate[1]);
                        int[] newGuardCoordinate = greenMoveInstance.move(thief_direction);
                        guard_coordinate[0] += newGuardCoordinate[0];
                        guard_coordinate[1] += newGuardCoordinate[1];
                    }
                    case 3,4,5,6,7,8,9: {
                        aco_move acoMoveInstance = new aco_move();
                        acoMoveInstance.setBehavior(false);
                        acoMoveInstance.setThief(thief_coordinate);
                        int[] newGuardCoordinate = acoMoveInstance.move(guard_coordinate);
                        guard_coordinate[0] = newGuardCoordinate[0];
                        guard_coordinate[1] = newGuardCoordinate[1];
                    }
                }
            }

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
    }

    public void setExitCommand(Command exitCommand) {
        this.exitCommand = exitCommand;
    }

    public void addObserver(Observer observer) {
        victoryObservers.add(observer);
    }

    private void notifyVictoryObservers() {
        for (Observer observer : victoryObservers) {
            observer.notify(getGraphics()); // Passa il Graphics del pannello di gioco
        }
    }

    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawMatrix(g);
            drawThief(g);
            drawGuard(g);

            if (victoryAchieved) {
                victory.notify(g);
                ranking.RankingRead();

            } else if (thief_coordinate[0] == guard_coordinate[0] && thief_coordinate[1] == guard_coordinate[1]) {
                defeat.notify(g);
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

        private void drawThief(Graphics g) {
            int cellSize = 21;
            g.setFont(new Font("Segoe UI Emoji", Font.PLAIN, cellSize));
            g.drawString("\uD83C\uDFC3", thief_coordinate[1] * cellSize, (thief_coordinate[0] + 1) * cellSize);
        }

        private void drawGuard(Graphics g) {
            int cellSize = 21;
            g.setFont(new Font("Segoe UI Emoji", Font.PLAIN, cellSize));
            g.drawString("\uD83D\uDEE1", guard_coordinate[1] * cellSize, (guard_coordinate[0] + 1) * cellSize);
        }
    }
}
