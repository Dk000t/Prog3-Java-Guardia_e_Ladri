import java.awt.*;

interface Observer {
    void notify(Graphics g);
}

class VictoryDisplay implements Observer {
    @Override
    public void notify(Graphics g) {
        g.setColor(Color.BLUE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Hai vinto!", 100, 150);
    }
}

class DefeatDisplay implements Observer{
    @Override
    public void notify(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Hai perso!", 100, 150);
    }
}