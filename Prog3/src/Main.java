public class Main {
    public static void main(String[] args) {
        Ranking ranking = new Ranking(); // Definisco un oggetto ranking.
        ranking.RankingRead(); // Chiamo il metodo RankingRead per leggere la classifica all avvio del gioco.
        ranking.setPlayer_info(); // SetPlayer è il metodo che si occupa di far inserire le credenziali al player.
        Room room = new Room(); // Definisco un oggetto room.
        room.fillMatrix(); // Il metodo fillMatrix riempie la stanza di caselle rosse, gialle, verdi e bianche.
        Game game = new Game(room); // Definisco un oggetto game.

        // Definisco un oggetto observer per gestire la vittoria o la sconfitta del player.
        Observer victoryObserver = new VictoryDisplay();
        game.addObserver(victoryObserver);
        // Definisco un oggetto command per gestire l'uscita dal gioco tramite tasto "ESC".
        ExitCommand exitCommand = new ExitCommand();
        game.setExitCommand(exitCommand);
    }
}