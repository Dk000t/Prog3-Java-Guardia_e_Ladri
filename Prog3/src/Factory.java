// Interfaccia Character
interface Character {
    int[] get_Coordinate(Room room);
}

// Classe Thief che implementa l'interfaccia Character
class Thief implements Character {

    @Override
    public int[] get_Coordinate(Room room){
        // Inizializza le coordinate del ladro nell'angolo in basso a destra della stanza
        int x = room.row - 1;
        int y = room.row - 1;
        return new int[]{x,y};
    }

}

// Classe Guard che implementa l'interfaccia Character
class Guard implements Character {

    @Override
    public int[] get_Coordinate(Room room){
        // Inizializza le coordinate della guardia in una posizione predefinita
        int x = 7;
        int y = 5;
        return new int[]{x,y};
    }

}

// Factory Method per creare istanze di personaggi
class CharacterFactory {
    // Tipi di personaggi disponibili
    enum CharacterType {
        THIEF,
        GUARD
    }

    // Si restituisce un'istanza del personaggio in base al tipo specificato
    static Character createCharacter(CharacterType type) {
        // Utilizza uno switch per determinare il tipo di personaggio e creare un'istanza corrispondente
        return switch (type) {
            case THIEF -> new Thief();
            case GUARD -> new Guard();
        };
    }
}
