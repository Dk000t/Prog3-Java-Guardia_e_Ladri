import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.JOptionPane;

// Classe che gestisce il ranking dei giocatori
public class Ranking {
    private static String name, surname;
    static File file = new File("Ranking.txt");

    // Metodo per leggere e visualizzare il ranking
    public void RankingRead() {
        try {
            if (!file.exists()) {
                file.createNewFile(); // Crea il file se non esiste
            }

            if (file.length() != 0) {
                System.out.println("\nRanking:\n");
                List<String> tuples = new ArrayList<>();
                FileReader fileReader = new FileReader("Ranking.txt");
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String riga;
                while ((riga = bufferedReader.readLine()) != null) {
                    tuples.add(riga);
                }
                bufferedReader.close();

                // Ordina le tuple in base al valore di "points" in ordine crescente
                tuples.sort(Comparator.comparingInt(s -> Integer.parseInt(s.split(",")[2])));

                // Stampare l'elenco numerato
                int position = 1;
                for (String tuple : tuples) {
                    String[] parts = tuple.split(",");
                    System.out.println(position + ". " + parts[0] + " " + parts[1] + " - " + parts[2] + " Passi");
                    position++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Metodo per impostare le informazioni del giocatore
    public void setPlayer_info() {
        int playGame = JOptionPane.showConfirmDialog(null, "Vuoi giocare a Guardia e Ladro?", "Game", JOptionPane.YES_NO_OPTION);

        if (playGame == JOptionPane.YES_OPTION) {
            do {
                name = JOptionPane.showInputDialog("Inserisci il tuo nome:");

                // Se l'utente ha chiuso la finestra o premuto "Cancel" durante l'inserimento del nome
                if (name == null) {
                    JOptionPane.showMessageDialog(null, "GL HF!");
                    System.exit(0);
                }
            } while (name.trim().isEmpty());  // Continua a chiedere se il campo è vuoto o contiene solo spazi

            do {
                surname = JOptionPane.showInputDialog("Inserisci il tuo cognome:");

                // Se l'utente ha chiuso la finestra o premuto "Cancel" durante l'inserimento del cognome
                if (surname == null) {
                    JOptionPane.showMessageDialog(null, "GL HF!");
                    System.exit(0);
                }
            } while (surname.trim().isEmpty());  // Continua a chiedere se il campo è vuoto o contiene solo spazi
        } else {
            JOptionPane.showMessageDialog(null, "GL HF!");
            System.exit(0); // Chiudi l'applicazione se l'utente ha premuto "No" o chiuso la finestra
        }
    }

    // Metodo per salvare il punteggio del giocatore e aggiornare il ranking
    public void RankingSave(int points) {
        try {
            FileWriter fileWriter = new FileWriter("Ranking.txt", true); // "true" indica di appendere al file esistente
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(name + "," + surname + "," + points);
            printWriter.close();

            // Leggi tutte le tuple dal file
            List<String> tuples = new ArrayList<>();
            FileReader fileReader = new FileReader("Ranking.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String riga;
            while ((riga = bufferedReader.readLine()) != null) {
                tuples.add(riga);
            }
            bufferedReader.close();

            // Ordina le tuple in base al valore di "points"
            tuples.sort(Comparator.comparingInt(s -> Integer.parseInt(s.split(",")[2])));

            // Sovrascrivi il file con le tuple ordinate
            FileWriter overwriteFileWriter = new FileWriter("Ranking.txt");
            PrintWriter overwritePrintWriter = new PrintWriter(overwriteFileWriter);
            for (String tuple : tuples) {
                overwritePrintWriter.println(tuple);
            }
            overwritePrintWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
