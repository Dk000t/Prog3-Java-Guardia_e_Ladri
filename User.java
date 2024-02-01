import java.util.Scanner;
import java.util.Random;
public class User {
    //Viene richiesto il Nome e Cognome del giocatore.
    public User(){
        System.out.println("Benvenuto in \"Guardia e Ladri\".");
        System.out.println("Digita il tuo Nome:");
        String nome = inputScanner.nextLine();
        System.out.println("Digita il tuo Cognome:");
        String cognome = inputScanner.nextLine();
    }
    //Inizializzeremo la posizione della guardia e del ladro in modo randomico all'interno della stanza.
    public void Guard_n_Thief_inizialize(){
        System.out.println("Il ladro entra nella stanza...");
        Random random = new Random();
        Room position = new Room();
        //Si inizializza la posizione della guardia in modo randomico.
        do {
            guard_x = random.nextInt(position.maxRows);
            guard_y = random.nextInt(position.maxColumns);
        }
        while(position.matrice[guard_x][guard_y] == 'X');
        position.matrice[guard_x][guard_y] = 'G';
        //Il ladro sarà inserito nel punto piu lontano dalla stanza.
        thief_x = position.maxRows - 1;
        thief_y = position.maxColumns -1;
        position.matrice[thief_x][thief_y] = 'L';
    }
    private Scanner inputScanner = new Scanner(System.in);
    public int guard_x,guard_y,thief_x,thief_y;
}
