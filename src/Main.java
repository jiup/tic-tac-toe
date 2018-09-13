import domain.State;

public class Main {
    public static void main(String[] args) {
        System.out.println((new State(new int[]{
                -1,0,-1,
                -1,-1,0,
                1,1,-1
        }, 6).getLastPawn()));
    }
}
