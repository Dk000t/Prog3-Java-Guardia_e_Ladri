public class Main {
    public static void main(String[] args) {
        Room room = new Room();
        Game game = new Game(room);

        // Creazione dell'observer
        VictoryObserver victoryObserver = new VictoryDisplay();

        // Aggiunta dell'observer al gioco
        game.addObserver(victoryObserver);
    }
}


