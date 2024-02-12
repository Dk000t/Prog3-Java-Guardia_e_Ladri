import java.awt.*;
import java.util.Random;
// Interfaccia Character
interface Character {
    int get_X(Room room);
    int get_Y(Room room);
}

// Classe Thief
class Thief implements Character {

    @Override
    public int get_X(Room room) {
        return room.row - 2;
    }

    @Override
    public int get_Y(Room room) {
        return room.column - 2;
    }
}

// Classe Guard
class Guard implements Character {
    private static Random random = new Random();

    @Override
    public int get_X(Room room) {
        int x = random.nextInt(room.row + 1);
        return x;
    }

    @Override
    public int get_Y(Room room) {
        int y = random.nextInt(room.row + 1);
        return y;
    }
}

// Factory Method
class CharacterFactory {
    enum CharacterType {
        THIEF,
        GUARD
    }

    static Character createCharacter(CharacterType type) {
        switch (type) {
            case THIEF:
                return new Thief();
            case GUARD:
                return new Guard();
            default:
                throw new IllegalArgumentException("Invalid character type");
        }
    }
}
