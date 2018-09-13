import domain.State;

public class Main {
    public static void main(String[] args) {
        State s = State.newInstance().setBoard(new int[][]{
                {-1, 0, -1},
                {-1, -1, 0},
                {1, 1, 0}
        });
        System.out.println(s);
    }
}
