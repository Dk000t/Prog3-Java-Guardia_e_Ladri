public class Main {
    public static void main(String[] args) {
        Ranking ranking = new Ranking();
        ranking.RankingRead();
        ranking.setPlayer_info();
        Room room = new Room();
        room.fillMatrix();
        Game game = new Game(room);
        Observer victoryObserver = new VictoryDisplay();
        game.addObserver(victoryObserver);
    }
}