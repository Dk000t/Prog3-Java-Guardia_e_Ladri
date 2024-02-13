interface Strategy{
    int[] move(Room room,Character guard);
}
class rand_move implements Strategy{
    @Override
    public int[] move(Room room, Character guard) {
        return guard.get_Coordinate(room);
    }
}
class aco_move implements Strategy{
    @Override
    public int[] move(Room room, Character guard) {
        return new int[] {1,1};
    }
}


