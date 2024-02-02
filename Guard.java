import java.util.Random;
public class Guard {
    Random random = new Random();
    //Inizializza la guardia nella stanza in modo randomico.
    public void InsertGuard(Room room){
        do {
            x = random.nextInt(room.maxRows);
            y = random.nextInt(room.maxColumns);
        }
        while(room.matrix[x][y] == 'X');
        room.matrix[x][y] = guard;
    }
    static char guard = 'G';
    private int x,y;
}
