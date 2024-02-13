import java.awt.*;

interface Observer {
    void notifyVictory(Graphics g);
    void notifyDefeat(Graphics g);
}

class VictoryDisplay implements Observer {
    @Override
    public void notifyVictory(Graphics g) {
        g.setColor(Color.BLUE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Hai vinto!", 100, 100);
    }

    @Override
    public void notifyDefeat(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Hai perso!", 100, 100);
    }
}