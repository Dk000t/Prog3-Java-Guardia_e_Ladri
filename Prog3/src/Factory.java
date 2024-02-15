// Interfaccia Character
interface Character {
    int[] get_Coordinate(Room room);
}

// Classe Thief
class Thief implements Character {

    @Override
    public int[] get_Coordinate(Room room){
        int x = room.row - 1;
        int y = room.row - 1;
        return new int[]{x,y};
    }

}

// Classe Guard
class Guard implements Character {

    @Override
    public int[] get_Coordinate(Room room){
        int x = 7;
        int y = 5;
        return new int[]{x,y};
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
