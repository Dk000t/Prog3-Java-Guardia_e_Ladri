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

// La classe Game estende JFrame e gestisce la logica di gioco
public class Game extends JFrame {

    private final Room room;
    private static int[] thief_coordinate; // Coordinate del ladro.
    private static int[] guard_coordinate; // Coordinate della guardia.
    private static int[] thief_direction; // Direzione intrapresa dal ladro.
    private boolean victoryAchieved = false; // Flag per la vittoria.
    private final List<Observer> victoryObservers = new ArrayList<>();
    private boolean gameOver = false; // Flag per la sconfitta.
    static int points; // Punteggio relativo ai passi effettuati.
    VictoryDisplay victory = new VictoryDisplay(); // Observer per la gestione della vittoria.
    DefeatDisplay defeat = new DefeatDisplay(); // Observer per la gestione della sconfitta.
    Ranking ranking = new Ranking();
    private boolean isOnGreen = false; // Flag per caselle verdi.
    private boolean isOnYellow = false; // Flag per caselle gialle.
    private boolean isOnRed = false; // Flag per caselle rosse.
    private boolean isOnWhite = false; // Flag per caselle bianche.
    private final Timer greenTimer; // Timer per le caselle verdi.
    private final Timer yellowTimer; // Timer per le caselle gialle.
    Random rand = new Random();
    private Command exitCommand;


    // Costruttore della classe Game
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
        setLocationRelativeTo(null); // Accentra la finestra.
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

        // Timer di 10 secondi per gestire il movimento sulle caselle verdi.
        greenTimer = new Timer(10000, (e) -> isOnGreen = false);
        greenTimer.setRepeats(false);
        greenTimer.start();

        //Timer di 10 secondi per gestire il movimento sulle caselle gialle.
        yellowTimer = new Timer(10000,(e) -> isOnYellow = false);
        yellowTimer.setRepeats(false);
        yellowTimer.start();
    }

    // Gestisce la pressione del tasto
    private void handleKeyPress(KeyEvent e) {
        if (victoryAchieved || gameOver) {
            return;
        }

        int keyCode = e.getKeyCode();
        boolean thiefMoved = false;

        // Movimento del ladro in base al tasto premuto
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

        // Gestisce l'uscita dal gioco se premuto ESC
        if (keyCode == KeyEvent.VK_ESCAPE) {
            if (exitCommand != null) {
                exitCommand.execute();
            }
            return;
        }

        // Aggiorna la posizione del ladro
        if (thiefMoved) {
            points += 1; // Incrementa i punti di 10

            // Verifica il tipo di cella su cui si trova il ladro
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

            else if(room.matrix[thief_coordinate[0]][thief_coordinate[1]] == Color.RED && !isOnGreen  && !isOnYellow) {
                isOnRed = true;
                isOnWhite = false;
            }
            else if(room.matrix[thief_coordinate[0]][thief_coordinate[1]] == Color.WHITE && !isOnGreen && !isOnYellow) {
                isOnWhite = true;
                isOnRed = false;
            }

            // Aggiorna la posizione della guardia in base al tipo di cella su cui si trova il ladro
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

            // Verifica se il ladro ha raggiunto una delle uscite
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

    // Imposta il comando di uscita
    public void setExitCommand(Command exitCommand) {
        this.exitCommand = exitCommand;
    }

    // Aggiunge un osservatore alla lista
    public void addObserver(Observer observer) {
        victoryObservers.add(observer);
    }

    // Notifica tutti gli osservatori della vittoria
    private void notifyVictoryObservers() {
        for (Observer observer : victoryObservers) {
            observer.notify(getGraphics());
        }
    }

    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawMatrix(g);
            drawThief(g);
            drawGuard(g);

            // Visualizza il messaggio di vittoria o sconfitta
            if (victoryAchieved) {
                victory.notify(g);
                ranking.RankingRead();

            } else if (thief_coordinate[0] == guard_coordinate[0] && thief_coordinate[1] == guard_coordinate[1]) {
                defeat.notify(g);
                gameOver = true;
            }
        }

        // Disegna la matrice della stanza
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

        // Disegna il ladro nella stanza
        private void drawThief(Graphics g) {
            int cellSize = 21;
            g.setFont(new Font("Segoe UI Emoji", Font.PLAIN, cellSize));
            g.drawString("\uD83C\uDFC3", thief_coordinate[1] * cellSize, (thief_coordinate[0] + 1) * cellSize);
        }

        // Disegna la guardia nella stanza
        private void drawGuard(Graphics g) {
            int cellSize = 21;
            g.setFont(new Font("Segoe UI Emoji", Font.PLAIN, cellSize));
            g.drawString("\uD83D\uDEE1", guard_coordinate[1] * cellSize, (guard_coordinate[0] + 1) * cellSize);
        }
    }
}
