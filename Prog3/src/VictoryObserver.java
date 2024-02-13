class VictoryDisplay implements VictoryObserver {
    @Override
    public void notifyVictory() {
        System.out.println("Hai vinto!");
    }
}