public class Thief {
//Inizializza il ladro nella stanza.
    public void InsertThief(Room room){
        x = room.maxRows - 2;
        y = room.maxColumns -2;
        room.matrix[x][y] = thief;
    }
    static char thief = 'L';
    private int x,y;
}
