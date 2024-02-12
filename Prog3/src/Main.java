public class Main {
    public static void main(String[] args) {
        Room room = new Room();
        Ranking ranking = new Ranking();
        Game game = new Game(room);
        VictoryObserver victoryObserver = new VictoryDisplay();
        game.addObserver(victoryObserver);
    }
}


