import javax.swing.*;
import java.awt.*;
import java.util.Random;

// Classe che rappresenta la stanza di gioco.
public class Room extends JFrame {

    // Matrice di colori che rappresenta la disposizione della stanza.
    public Color[][] matrix = {
            {Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK},
            {Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK},
            {Color.BLACK, Color.WHITE, Color.WHITE, Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK},
            {Color.BLACK, Color.WHITE, Color.WHITE, Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK},
            {Color.BLACK, Color.WHITE, Color.WHITE, Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK},
            {Color.BLACK, Color.WHITE, Color.WHITE, Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK},
            {Color.BLACK, Color.WHITE, Color.WHITE, Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK},
            {Color.BLACK, Color.WHITE, Color.WHITE, Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK},
            {Color.BLACK, Color.WHITE, Color.WHITE, Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK},
            {Color.BLACK, Color.WHITE, Color.WHITE, Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK},
            {Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK, Color.BLACK, Color.BLACK, Color.WHITE, Color.BLACK},
            {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK},
            {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK},
            {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK},
            {Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK},
            {Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK}};

    // Numero di righe nella matrice.
    public int row = matrix.length - 1;

    // Numero di colonne nella matrice.
    public int column = matrix[0].length - 1;

    // Array di punti che rappresentano le uscite dalla stanza.
    public Point[] exit = {new Point(11, 0), new Point(12, 0), new Point(13, 0)};

    // Metodo per riempire la matrice con celle di colori casuali.
    public void fillMatrix() {
        int totalCells = matrix.length * matrix[0].length;
        int greenCells = totalCells / 10;
        int redCells = totalCells / 10;
        int yellowCells = totalCells / 10;
        int whiteCells = totalCells - (greenCells + redCells + yellowCells); // Celle rimanenti.

        Random random = new Random();

        int greenCount = 0;
        int redCount = 0;
        int yellowCount = 0;
        int whiteCount = 0;

        // Creazione di un array di indici casuali per scandire la matrice in modo randomico.
        int[] randomIndices = new int[totalCells];
        for (int i = 0; i < totalCells; i++) {
            randomIndices[i] = i;
        }
        // Mescolare gli indici in modo casuale.
        for (int i = 0; i < totalCells; i++) {
            int randomIndex = random.nextInt(totalCells);
            int temp = randomIndices[i];
            randomIndices[i] = randomIndices[randomIndex];
            randomIndices[randomIndex] = temp;
        }

        for (int k = 0; k < totalCells; k++) {
            int i = randomIndices[k] / matrix[0].length;
            int j = randomIndices[k] % matrix[0].length;

            if (matrix[i][j] == Color.WHITE) {
                int rand = random.nextInt(100) + 1;

                // Assegna il colore in base alle percentuali specificate.
                if (rand <= 25 && greenCount < greenCells) {
                    matrix[i][j] = Color.GREEN;
                    greenCount++;
                } else if (rand <= 50 && redCount < redCells) {
                    matrix[i][j] = Color.RED;
                    redCount++;
                } else if (rand <= 75 && yellowCount < yellowCells) {
                    matrix[i][j] = Color.YELLOW;
                    yellowCount++;
                } else if (whiteCount < whiteCells) {
                    matrix[i][j] = Color.WHITE;
                    whiteCount++;
                }
            }
        }
    }
}