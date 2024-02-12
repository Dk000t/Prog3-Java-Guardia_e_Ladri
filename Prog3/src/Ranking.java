import java.io.*;
import java.util.Scanner;

public class Ranking {
    private String name, surname;
    int points;
    public Ranking() {
        if (fileExists()){
            System.out.println("Ranking:");
            RankingRead();
        }
            Scanner scanner = new Scanner(System.in);
            System.out.println("Inserisci il tuo nome:");
            this.name = scanner.nextLine();
            System.out.println("Inserisci il tuo cognome:");
            this.surname = scanner.nextLine();
    }
    private boolean fileExists() {
        File file = new File("Ranking.txt");
        return file.exists();
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
    private void Set_Points(int points) {
        this.points = points;
    }
}
