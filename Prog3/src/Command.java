import javax.swing.JOptionPane;

public interface Command {
    void execute();
}

//Comando che gestisce l'uscita dal gioco
class ExitCommand implements Command {
    @Override
    public void execute() {
        // Visualizza una finestra di dialogo per confermare l'uscita dal gioco
        int result = JOptionPane.showConfirmDialog(null, "Vuoi veramente uscire dal gioco?", "Exit", JOptionPane.YES_NO_OPTION);

        //Premendo "si", l'utente sceglie di uscire
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0); // Termina il programma
        }
        //Altrimenti, scegliendo "no", si chiude la finestra di dialogo
    }
}
