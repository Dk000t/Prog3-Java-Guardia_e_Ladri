import java.awt.*;
import java.util.ArrayList;
import java.util.List;
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

class aco_move implements Strategy {
    Room room = new Room();
    private final double PHEROMONE_DROP = 5.0;
    private final double PHEROMONE_INIT = 2.0;
    private final double EVAPORATION_RATE = 0.5;
    private static final double ALPHA = 3.0; // Importanza dei feromoni
    private static final double BETA = 2.0;  // Importanza della visibilità
    private final double[][] PHEROMONES_MATRIX = new double[room.row][room.column];
    private final int ANTS = room.row * room.column;
    Point[] ants = new Point[ANTS];
    final int[][] ANTS_DIRECTIONS = {{-1,0},{1,0},{0,-1},{0,1},{1,1},{-1,-1},{1,-1},{-1,1}};
    private Point thief;
    private Point guard;
    Random rand = new Random();

    public aco_move(){
        init_pheromones();
        init_ants();
    }

    // Vengono inizializzati i feromoni in tutta la matrice.
    private void init_pheromones(){
        for (int i = 0; i < room.row; i++) {
            for (int j = 0; j < room.column; j++) {
                PHEROMONES_MATRIX[i][j] = PHEROMONE_INIT;
            }
        }
    }

    // Inizializzo le formiche in una posizione casuale.
    private void init_ants(){
        for(int i = 0; i < ANTS; i++) {
            ants[i] = new Point();
            do {
                ants[i].setLocation(rand.nextInt(room.row),rand.nextInt(room.column));
            }
            while (!is_valid(ants[i]));
        }
    }

    // Le formiche inizializzate vengono fatte muovere nelle 8 caselle adiacenti.
    private void ants_move() {
        boolean thiefFound = false;

        while (!thiefFound) {
            for (int i = 0; i < ANTS; i++) {
                Point point = new Point(ants[i]); // Copia il punto della formica
                do {
                    int[] direction = ANTS_DIRECTIONS[rand.nextInt(8)];
                    point.setLocation(direction[0] + ants[i].x, direction[1] + ants[i].y);
                } while (!is_valid(point));

                ants[i].setLocation(point.x, point.y);

                if (ants[i].equals(thief)) {
                    drop_pheromones(ants[i]);
                    go_to_colony(ants[i]);
                    thiefFound = true; // Imposta il flag per indicare che il ladro è stato trovato
                    break; // Esci dal ciclo for quando il ladro è stato trovato
                }
            }
        }
    }


    // Viene controllata la validità delle coordinate generate casualmente o assegnate in base alla direzione scelta.
    private boolean is_valid(Point point){
        if(point.x >= 0 && point.x < room.row && point.y >= 0 && point.y < room.column && room.matrix[point.x][point.y] != Color.BLACK)
            return true;
        else
            return false;
    }

    // Vengono rilasciati i feromoni quando viene trovato del "cibo",
    private void drop_pheromones(Point ant){
        PHEROMONES_MATRIX[ant.x][ant.y] += PHEROMONE_DROP;
    }

    // I feromoni evaporano man mano che le formice si muovono.
    private void pheromones_evaporation(){
        for (int i = 0; i < room.row; i++)
            for (int j = 0; j < room.column; j++){
                PHEROMONES_MATRIX[i][j] *= (1 - EVAPORATION_RATE);
            }
    }

    // Una volta che il cibo è stato trovato la formica torna alla colonia rilasciando feromone dal cibo alla colonia.
    private void go_to_colony(Point ant) {
        Point newLocation;

        while(!(ant.equals(guard))){
            do {
                int[] dir = ANTS_DIRECTIONS[rand.nextInt(8)];
                newLocation = new Point(ant.x + dir[0], ant.y + dir[1]);
            } while (!is_valid(newLocation));

            ant.setLocation(newLocation);
            drop_pheromones(ant);
            pheromones_evaporation();
        }
    }

    // Una volta che le formiche sono tornate alla colonia la guardia decide le sue coordinate in base alle informazioni del feromone presente.
    private Point guard_next_move() {
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

    // Calcola la distanza euclidea tra due punti
    private double distance(Point a, Point b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    // Passa la posizione del ladro.
    public void setThief(int[] thief_pos){
        this.thief = new Point(thief_pos[0],thief_pos[1]);
    }

    @Override
    public int[] move(int[] guard_pos) {
        this.guard = new Point(guard_pos[0],guard_pos[1]);
        ants_move();
        Point next_direction = guard_next_move();
        return new int[] {next_direction.x, next_direction.y};
    }
}