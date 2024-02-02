public class Main {
    public static void main(String[] args){
        User session = new User();
        Room room = new Room();
        Thief thief = new Thief();
        Guard guard = new Guard();
        thief.InsertThief(room);
        guard.InsertGuard(room);
        room.getRoom();
    }
}
