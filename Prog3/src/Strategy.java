import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Definizione dell'interfaccia Strategy.
interface Strategy{
    // Le classi che implementano Strategy forniranno un'implementazione specifica di move.
    int[] move(int[] current_pos);
}

// Classe deputata alla gestione del k% dei casi per il movimento casuale della guardia e per gestire il movimento della guardia quando il ladro è su una casella gialla.
class rand_move implements Strategy {

    // IsExit evita che la guardia possa catturare il ladro sulla casella di uscita quando il ladro ha già vinto..
    private boolean isExit(Point[] exit, int x, int y) {
        for (Point p : exit) {
            if (p.x == x && p.y == y) {
                return true;
            }
        }
        return false;
    }

    // Lo spostamento casuale viene calcolato per le 8 caselle adiacenti, la guardia si muoverà di una casella alla volta.
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

    // Viene ritornato uno spostamento casuale valido della guardia.
    @Override
    public int[] move(int[] current_pos) {
        return rand_adjacent_8(current_pos);
    }
}

// Classe deputata al movimento speculare della guardia rispetto al movimento del ladro. Essa viene richiamata quando il ladro è su una casella verde.
class green_move implements Strategy {
    private final int guardX;
    private final int guardY;

    // Il costruttore passa le coordinate attuali della guardia.
    public green_move(int guardX, int guardY) {
        this.guardX = guardX;
        this.guardY = guardY;
    }

    // Il metodo move sovrascrive la firma dell'interfaccia Strategy per gestire il movimento tramite green_move.
    @Override
    public int[] move(int[] thief_direction) {
        Room room = new Room();

        // A seconda dello spostamento del ladro, vengono salvate le direzioni intraprese. Queste direzioni (Nord, Sud, Est, Ovest, Nord-Est ... etc) vengono sommate alle attuali coordinate della guardia.
        int x = thief_direction[0];
        int y = thief_direction[1];
        int[] guard_movement = new int[]{-x, -y};

        // Controllo se il movimento porta la guardia fuori dalla matrice.
        if (guardX + guard_movement[0] < 0 || guardX + guard_movement[0] >= room.row ||
                guardY + guard_movement[1] < 0 || guardY + guard_movement[1] >= room.column) {
            // Se il movimento porta la guardia fuori dalla matrice, mantienila ferma
            return new int[]{0, 0};
        }

        // Controllo se il movimento porta la guardia sopra una casella nera.
        if (room.matrix[guardX + guard_movement[0]][guardY + guard_movement[1]] == Color.BLACK) {
            // Se il movimento porta la guardia sopra una casella nera, mantienila ferma
            return new int[]{0, 0};
        }

        return guard_movement;
    }
}

// Classe deputata alla gestione dell'ANT COLONY OPTIMIZATION, essa si attiva quando è su una casella bianca %(100 - k) dei casi o è su una casella rossa.
class aco_move implements Strategy {
    Room room = new Room(); // Viene istanziato un oggetto Room;
    private final double[][] PHEROMONES_MATRIX = new double[room.row][room.column]; // Matrice dei feromoni.
    private final int ANTS = room.row * room.column; // Quantità di formiche.
    Point[] ants = new Point[ANTS]; // Array di Point per salvare la posizione di tutte le formiche presenti.
    final int[][] ANTS_DIRECTIONS = {{-1,0},{1,0},{0,-1},{0,1},{1,1},{-1,-1},{1,-1},{-1,1}}; // Array bidimensionale utile per usare le posizioni come offset per lo spostamento nelle 8 caselle adiacenti.
    private Point thief; // Posizione del ladro.
    private Point guard; // Posizione della guardia.
    Random rand = new Random(); // Oggetto di tipo Random per la generazione di numeri pseudocasuali.
    Point latest_seen = new Point(); // Ultima posizione in cui è stato avvistato/trovato il cibo.
    private Boolean isRed; // Flag utile a far variare il comportamento dell'ACO,

    // Il costruttore viene chiamato per inizializzare il feromone nella matrice alla creazione dell'oggetto.
    public aco_move(){
        init_pheromones();
    }

    // Imposta la posizione del ladro passo per passo.
    public void setThief(int[] thief_pos){
        this.thief = new Point(thief_pos[0],thief_pos[1]);
    }

    // Viene controllata la validità delle coordinate scelte, se esse si trovino nella matrice e non stiano sulle pareti della stanza.
    private boolean is_valid(Point point){
        return point.x >= 0 && point.x <= room.row && point.y >= 0 && point.y <= room.column && room.matrix[point.x][point.y] != Color.BLACK;
    }

    // Se la posizione della formica è uguale alla posizione in cui è sto trovato il Cibo allora ritorna True.
    private Boolean isTarget(Point current_point){
        return current_point.equals(latest_seen);
    }

    /* Serve a gestire il comportamento dell'Aco a seconda di chi debba essere il target */
    public void setBehavior(Boolean isRed) {
        this.isRed = isRed;
    }

    // Trovato il Cibo le formiche rilasciano sul suolo il feromone.
    private void drop_pheromones(Point ant_pos) {
        // Quantità di feromone rilasciato.
        double PHEROMONE_DROP = 5.0;
        PHEROMONES_MATRIX[ant_pos.x][ant_pos.y] += PHEROMONE_DROP;
    }

    // I feromoni evaporano man mano che le formice si muovono.
    private void pheromones_evaporation(){
        for (int i = 0; i < room.row; i++)
            for (int j = 0; j < room.column; j++){
                double EVAPORATION_RATE = 0.5;// Quantità di feromone evaporata.
                PHEROMONES_MATRIX[i][j] *= (1 - EVAPORATION_RATE);
            }
    }

    // Vengono inizializzati i feromoni in tutta la matrice.
    private void init_pheromones(){
        for (int i = 0; i < room.row; i++) {
            for (int j = 0; j < room.column; j++) {
                double PHEROMONE_INIT = 2.0; // Quantità di feromone inizializzato.
                PHEROMONES_MATRIX[i][j] = PHEROMONE_INIT;
            }
        }
    }

    // Le formiche vengono inizializzate alla colonia (Guardia).
    private void init_ants_random(){
        for(int i = 0; i < ANTS; i++) {
            ants[i] = new Point(rand.nextInt(room.row), room.column);
        }
    }

    // Le formiche vengono inizializzate nella posizione della guardia dopo che il cibo è stato trovato per calcolare il percorso tra Colonia e Cibo.
    private void init_ants_to_guard(){
        for(int i = 0; i < ANTS; i++) {
            ants[i] = new Point(guard.x, guard.y);
        }
    }

    /* La guardia effettua decisioni dei suoi spostamenti in base alle informazioni sul suolo.
    * Esso valuta il feromone presente e la visibilità.
    * La prossima mossa viene scelta calcolando uno score delle caselle adiacenti pesato facendo uso di
    * fattori quali ALPHA e BETA. La cella con il punteggio più alto è reputata la miglior mossa. */
    private Point guard_next_move() {
        final double ALPHA = 5.0; // Importanza dei feromoni.
        final double BETA = 1.0;  // Importanza della visibilità.

        Point[] adjacent_cells = new Point[]{
                new Point(guard.x - 1, guard.y),   // Sinistra
                new Point(guard.x + 1, guard.y),   // Destra
                new Point(guard.x, guard.y - 1),   // Sopra
                new Point(guard.x, guard.y + 1),   // Sotto
                new Point(guard.x - 1, guard.y - 1), // Diagonale in alto a sinistra
                new Point(guard.x + 1, guard.y - 1), // Diagonale in alto a destra
                new Point(guard.x - 1, guard.y + 1), // Diagonale in basso a sinistra
                new Point(guard.x + 1, guard.y + 1)  // Diagonale in basso a destra
        };

        Point best_move = null;
        double highest_score = Double.NEGATIVE_INFINITY;

        for (Point adjacent_cell : adjacent_cells) {
            if (is_valid(adjacent_cell)) {
                double pheromone_level = PHEROMONES_MATRIX[adjacent_cell.x][adjacent_cell.y];
                double proximity_score = 1.0 / (1.0 + distance(guard, adjacent_cell));
                double score = Math.pow(pheromone_level, ALPHA) * Math.pow(proximity_score, BETA);
                if (score > highest_score || best_move == null) {
                    highest_score = score;
                    best_move = adjacent_cell;
                }
            }
        }

        return best_move;
    }

    // Calcola la distanza euclidea tra due punti.
    private double distance(Point a, Point b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    /* La firma del metodo move dell'Interfaccia Strategy viene sovrascritta, viene passata la posizione corrente della guardia ogni volta,
     * viene chiamato ants_move per far muovere le formiche, trovare il target e trovare il percorso migliore.
     * Infine vengono ritornate le coordinate che dovrà assumere la guardia per i requisiti definiti. */
    @Override
    public int[] move(int[] guard_pos) {
        this.guard = new Point(guard_pos[0],guard_pos[1]);
        ants_move();
        Point next_direction = guard_next_move();
        return new int[] {next_direction.x, next_direction.y};
    }

    /* Le formiche vengono inizializzate in modo randomico nella stanza.
    * A seconda dello stato di isRed cambia il target (Cibo) a cui si applica l'ACO.
    * Una volta impostato il target a cui applicare l'ACO le formiche si muovono
    * di una casella alla volta nelle 8 caselle adiacenti fino a quando non trovano il Target.
    * La prima formica che trova il ladro rilascia feromone in quella posizione e successivamente
    * viene avviato l'Ant Colony Optimization. Calcolato il path viene fatto evaporare il feromone. */
    private void ants_move() {
        init_ants_random();
        boolean targetFound = false;
        Point target = new Point();

        if(isRed) {
            Point exit = new Point(room.exit[rand.nextInt(3)]);
            target.setLocation(exit);
        }
        else {
            target.setLocation(thief);
        }

        while (!targetFound) {
            for (int i = 0; i < ANTS; i++) {
                Point point = new Point(); // Copia il punto della formica
                do {
                    int[] direction = ANTS_DIRECTIONS[rand.nextInt(8)];
                    point.setLocation(direction[0] + ants[i].x, direction[1] + ants[i].y);
                } while (!is_valid(point));

                ants[i].setLocation(point.x, point.y);

                if (ants[i].equals(target)) {
                    targetFound = true; // Imposta il flag per indicare che il ladro è stato trovato
                    latest_seen.setLocation(ants[i]);
                    drop_pheromones(ants[i]);
                    break; // Esci dal ciclo for quando il ladro è stato trovato
                }
            }
        }
        find_path();
        pheromones_evaporation();
    }

    /* Trovato il cibo vengono inizializzate tutte le formiche alla colonia.
    * Ogni formica si muove di una casella alla volta nelle 8 caselle adiacenti, ogni spostamento
    * va a definire un punto del percorso che essa compie per arrivare al Target.
    * Una volta che la formica ha raggiunto il target tutti i punti percorsi vengono salvati.
    * Viene calcolato il percorso più breve, tenendo conto del path calcolato dalla formica precedente.
    * Infine nelle posizioni che definiscono il percorso migliore viene depositato il feromone. */
    private void find_path() {
        init_ants_to_guard();
        int ANTS_ITERATION = 20;
        List<Point> shortestPath = null;

        for (int j = 0; j < ANTS_ITERATION; j++) {
            for (int i = 0; i < ANTS; i++) {
                Point current_point = new Point(ants[i]);
                List<Point> path = new ArrayList<>();
                while (!isTarget(current_point)) {
                    Point next_point;
                    do {
                        int[] dir = ANTS_DIRECTIONS[rand.nextInt(8)];
                        next_point = new Point(current_point.x + dir[0], current_point.y + dir[1]);
                    } while (!is_valid(next_point));
                    path.add(next_point);
                    current_point = next_point; // Aggiorna la posizione corrente
                }
                if (shortestPath == null || path.size() < shortestPath.size()) {
                    shortestPath = new ArrayList<>(path); // Se il nuovo percorso è più breve, aggiornalo
                }
            }
        }

        assert shortestPath != null;
        for (Point step : shortestPath) {
            drop_pheromones(step);
        }
    }

}
