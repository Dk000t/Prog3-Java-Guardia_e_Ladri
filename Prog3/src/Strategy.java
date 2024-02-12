import java.awt.*;
public class Strategy {
    public int[] rand_move(Room room, Character guard){
        int x,y;
        do{
            x = guard.get_X(room);
            y = guard.get_Y(room);
        }while (room.matrix[x][y] == Color.BLACK && room.matrix[x][y] == room.matrix[room.row][room.column]);

        int[] coordinates = {x,y};
        return coordinates;
    }
}
