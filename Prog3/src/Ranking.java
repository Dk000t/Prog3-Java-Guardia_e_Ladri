import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Ranking {
    private static String name, surname;
    static File file = new File("Ranking.txt");

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

                // Ordina le tuple in base al valore di "points" in ordine decrescente
                tuples.sort(Comparator.comparingInt(s -> Integer.parseInt(s.split(",")[2])));
                Collections.reverse(tuples);

                // Stampare l'elenco numerato
                int position = 1;
                for (String tuple : tuples) {
                    String[] parts = tuple.split(",");
                    System.out.println(position + ". " + parts[0] + " " + parts[1] + " - " + parts[2] + " points");
                    position++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("\nInserisci il tuo nome:");
        name = scanner.nextLine();
        System.out.println("Inserisci il tuo cognome:");
        surname = scanner.nextLine();
    }

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
