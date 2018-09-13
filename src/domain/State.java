package domain;

public class State implements Cloneable {
    private static final int SIZE = 3;
    private static final char[] LABELS = {'O', '-', 'X'};

    private boolean maxTurn = true;
    private int[][] board = new int[SIZE][SIZE];
    private int value = 0;
    private int depth = 0;

    public static State newInstance() {
        return new State();
    }

    private State() {
    }

    public boolean isMaxTurn() {
        return maxTurn;
    }

    public int[][] getBoard() {
        return board;
    }

    public int getValue() {
        return value;
    }

    public int getDepth() {
        return depth;
    }

    public State flipTurn() {
        this.maxTurn = !maxTurn;
        return this;
    }

    public State setBoard(int[][] board) {
        this.board = board;
        return this;
    }

    public State setValue(int value) {
        this.value = value;
        return this;
    }

    public State setDepth(int depth) {
        this.depth = depth;
        return this;
    }

    public boolean isTerminal() {
        int bSlashSum = 0, slashSum = 0;
        for (int i = 0; i < SIZE; i++) {
            int rowSum = 0, colSum = 0;
            for (int j = 0; j < SIZE; j++) {
                if (Math.abs(rowSum += board[i][j]) == SIZE
                        || Math.abs(colSum += board[j][i]) == SIZE) {
                    return true;
                }
            }
            if (Math.abs(bSlashSum += board[i][i]) == SIZE
                    || Math.abs(slashSum += board[i][SIZE - 1 - i]) == SIZE) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                builder.append(LABELS[board[i][j] + 1]).append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
