import java.util.Observable;
import java.util.Observer;

public class VictoryObserver implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        // Quando viene notificata la vittoria, esegui le azioni necessarie
        System.out.println("Hai vinto!");
        // Esempio: Puoi chiamare un metodo per salvare il punteggio o altro
        Ranking.RankingSave("Nome", "Cognome", 100); // Esempio di salvataggio del punteggio
    }
}
