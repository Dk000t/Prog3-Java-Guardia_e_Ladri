class VictoryDisplay implements VictoryObserver {
    Ranking ranking = new Ranking();
    @Override
    public void notifyVictory() {
        System.out.println("Hai vinto!");
        ranking.RankingSave();
    }
}