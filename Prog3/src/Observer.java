import java.awt.*;

interface Observer {
    void notify(Graphics g);
}

//Osservatore che gestisce la visualizzazione della vittoria
class VictoryDisplay implements Observer {
    @Override
    public void notify(Graphics g) {

        //Imposta il colore del testo e il font per la visualizzazione della vittoria
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 30));

        //Disegna il messaggio "Hai vinto!" sulla finestra di gioco
        g.drawString("Hai vinto!", 100, 150);
    }
}

//Osservatore che gestisce la visualizzazione della sconfitta
class DefeatDisplay implements Observer{
    @Override
    public void notify(Graphics g) {
        //Imposta il colore del testo e il font per la visualizzazione della sconfitta
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 30));

        //Disegna il messaggio "Hai perso!" sulla finestra di gioco
        g.drawString("Hai perso!", 100, 150);
    }
}