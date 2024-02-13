import java.util.Random;

public class Strategy {
    public int[] rand_move(Room room, Character guard) {
        int[] coordinate = new int[2];
        Random random = new Random();
        int rand = random.nextInt(10);

        switch (rand) {
            case 0,1,2:
                coordinate = guard.get_Coordinate(room);
                break;
        }

        return coordinate;
    }
}
