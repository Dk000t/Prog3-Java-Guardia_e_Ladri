public class Main {
    public static void main(String[] args) {
        Room room = new Room();
        Game game = new Game(room);
        Observer victoryObserver = new VictoryDisplay();
        game.addObserver(victoryObserver);
    }
}