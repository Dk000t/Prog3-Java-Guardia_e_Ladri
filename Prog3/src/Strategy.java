import java.awt.*;
import java.util.Arrays;
import java.util.Random;

interface Strategy{
    int[] move(Room room,Character guard);
}
class rand_move implements Strategy{
    private int[] rand_adjacent_8(Room room, Character guard) {
        final int[][] DIRECTIONS = {
                {0, 1}, {0, -1}, {1, 0}, {-1, 0},
                {1, 1}, {-1, 1}, {1, -1}, {-1, -1}
        };
        Random random = new Random();
        int[] current_pos = guard.get_Coordinate(room);
        int rand;

        do {
            rand = random.nextInt(8);
            int[] direction = DIRECTIONS[rand];
            current_pos = new int[]{current_pos[0] + direction[0], current_pos[1] + direction[1]};
        } while (current_pos[0] < 0 || current_pos[0] >= room.matrix.length ||
                current_pos[1] < 0 || current_pos[1] >= room.matrix[0].length ||
                room.matrix[current_pos[0]][current_pos[1]] == Color.BLACK);

        return current_pos;
    }

    @Override
    public int[] move(Room room, Character guard) {
        return rand_adjacent_8(room,guard);
    }
}
class aco_move implements Strategy{
    @Override
    public int[] move(Room room, Character guard) {
        return new int[] {1,1};
    }
}


