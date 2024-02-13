import java.util.Random;

public class Strategy {
    Random random = new Random();
    int[] coordinate = new int[2];

    public int[] guard_move(Room room, Character guard) {
        int rand = random.nextInt(10); // Genera un nuovo numero casuale ad ogni chiamata
        switch (rand) {
            case 0, 1, 2:
                coordinate = rand_move(room, guard);
                break;
            case 3, 4, 5, 6, 7, 8, 9:
                coordinate = new int[]{3, 3};
                break;
        }
        return coordinate;
    }

    private int[] rand_move(Room room, Character guard) {
        return guard.get_Coordinate(room);
    }
}
