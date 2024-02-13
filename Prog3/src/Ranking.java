import java.io.*;
import java.util.Scanner;

public class Ranking {
    private String name, surname;
    int points;
    File file = new File("Ranking.txt");
    public Ranking() {
        if (file.exists() && file.length() != 0){
            System.out.println("Ranking:");
            RankingRead();
        }
            Scanner scanner = new Scanner(System.in);
            System.out.println("Inserisci il tuo nome:");
            this.name = scanner.nextLine();
            System.out.println("Inserisci il tuo cognome:");
            this.surname = scanner.nextLine();
    }
    public static void RankingRead() {
        try {
            FileReader fileReader = new FileReader("Ranking.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String riga;
            while ((riga = bufferedReader.readLine()) != null) {
                System.out.println(riga);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void RankingSave() {
        try {
            FileWriter fileWriter = new FileWriter("Ranking.txt", true); // "true" indica di appendere al file esistente
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(this.name + "," + this.surname + "," + this.points);
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
