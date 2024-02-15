import java.awt.*;
import java.util.Random;

interface Strategy{
    int[] move(Room room, int[] current_pos);
}

class rand_move implements Strategy {
    private boolean isExit(Point[] exit, int x, int y) {
        for (Point p : exit) {
            if (p.x == x && p.y == y) {
                return true;
            }
        }
        return false;
    }

    private int[] rand_adjacent_8(Room room, int[] current_pos) {
        final int[][] DIRECTIONS = {{-1,0},{1,0},{0,-1},{0,1},{1,1},{-1,-1},{1,-1},{-1,1}};

        Random random = new Random();
        int randIndex;
        int[] direction;
        int newX, newY;

        do {
            randIndex = random.nextInt(8);
            direction = DIRECTIONS[randIndex];
            newX = current_pos[0] + direction[0];
            newY = current_pos[1] + direction[1];
        } while (newX < 0 || newX >= room.row || newY < 0 || newY >= room.column || room.matrix[newX][newY] == Color.BLACK || isExit(room.exit, newX, newY));

        current_pos[0] = newX;
        current_pos[1] = newY;

        return current_pos;
    }

    @Override
    public int[] move(Room room, int[] current_pos) {
        return rand_adjacent_8(room, current_pos);
    }
}

class aco_move implements Strategy{
    @Override
    public int[] move(Room room, int[] current_pos) {
        current_pos[0] = 1;
        current_pos[1] = 1;
        return current_pos;
    }
}
