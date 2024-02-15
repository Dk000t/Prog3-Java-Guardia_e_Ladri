import java.awt.*;
import java.util.Random;

interface Strategy{
    int[] move(Room room,Character guard, int[] current_pos);
}

class rand_move implements Strategy {
    private int[] rand_adjacent_8(Room room, int[] current_pos) {

        final int[][] DIRECTIONS = {{1, 0},{-1, 0},{0, -1},{0, 1},{1, 1},{1, -1},{-1, 1},{-1, -1}};
        Random random = new Random();
        int randIndex = random.nextInt(8);
        int[] direction = DIRECTIONS[randIndex];
        int newX = current_pos[0] + direction[0];
        int newY = current_pos[1] + direction[1];

        // Verifica se la nuova posizione generata è all'interno della matrice
        if (newX >= 0 && newX < room.matrix.length && newY >= 0 && newY < room.matrix[0].length) {
            // Verifica anche se la nuova posizione è su uno spazio vuoto (non nero)
            if (room.matrix[newX][newY] != Color.BLACK) {
                current_pos[0] = newX;
                current_pos[1] = newY;
            }
        }

        return current_pos;
    }

    @Override
    public int[] move(Room room, Character guard, int[] current_pos) {
        return rand_adjacent_8(room, current_pos);
    }
}

class aco_move implements Strategy{
    @Override
    public int[] move(Room room, Character guard, int[] current_pos) {
        current_pos[0] = 1;
        current_pos[1] = 1;
        return current_pos;
    }
}
