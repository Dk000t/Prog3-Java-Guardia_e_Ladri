import java.awt.*;
import java.util.Random;

interface Strategy{
    int[] move(int[] current_pos);
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

    private int[] rand_adjacent_8(int[] current_pos) {
        Room room = new Room();
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
    public int[] move(int[] current_pos) {
        return rand_adjacent_8(current_pos);
    }
}

class green_move implements Strategy {
    private final int guardX;
    private final int guardY;

    public green_move(int guardX, int guardY) {
        this.guardX = guardX;
        this.guardY = guardY;
    }

    @Override
    public int[] move(int[] thief_direction) {
        Room room = new Room();
        int x = thief_direction[0];
        int y = thief_direction[1];
        int[] guard_movement = new int[]{-x, -y};

        // Controllo se il movimento porta la guardia fuori dalla matrice
        if (guardX + guard_movement[0] < 0 || guardX + guard_movement[0] >= room.row ||
                guardY + guard_movement[1] < 0 || guardY + guard_movement[1] >= room.column) {
            // Se il movimento porta la guardia fuori dalla matrice, mantienila ferma
            return new int[]{0, 0};
        }

        // Controllo se il movimento porta la guardia sopra una casella nera
        if (room.matrix[guardX + guard_movement[0]][guardY + guard_movement[1]] == Color.BLACK) {
            // Se il movimento porta la guardia sopra una casella nera, mantienila ferma
            return new int[]{0, 0};
        }

        return guard_movement;
    }
}

