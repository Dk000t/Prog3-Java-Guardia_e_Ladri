import java.util.Scanner;
public class User {
    //Viene richiesto il Nome e Cognome del giocatore.
    public User(){
        System.out.println("Benvenuto in \"Guardia e Ladri\".");
        System.out.println("Digita il tuo Nome:");
        String name = inputScanner.nextLine();
        System.out.println("Digita il tuo Cognome:");
        String surname = inputScanner.nextLine();
    }
    private Scanner inputScanner = new Scanner(System.in);
}
