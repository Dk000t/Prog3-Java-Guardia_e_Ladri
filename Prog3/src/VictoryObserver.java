class VictoryDisplay implements VictoryObserver {
    @Override
    public void notifyVictory() {
        System.out.println("Hai vinto!");
        // Qui puoi implementare la logica per visualizzare il messaggio di vittoria nel pannello di gioco o fare altre azioni necessarie
    }
}