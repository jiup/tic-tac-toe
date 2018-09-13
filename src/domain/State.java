package domain;

import java.util.Arrays;

import static constant.Board.SIZE;

public class State implements Cloneable {
    private static final char[] LABELS = {'O', '-', 'X'};

    public static State create() {
        return new State();
    }

    public boolean isMaxTurn() {
        return maxTurn;
    }

    public boolean isTerminal() {
        return terminal;
    }

    public int[][] getBoard() {
        return board;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public State forward(int i, int j) {
        if (terminal)
            throw new RuntimeException("game was over -\n" + toString());

        if (board[i][j] != 0)
            throw new RuntimeException("piece crash at (" + i + ", " + j + ")");

        State nextState = this.clone();
        nextState.board[i][j] = (nextState.maxTurn = !maxTurn) ? 1 : -1;
        nextState.depth++;
        nextState.updateStatus(i, j);
        return nextState;
    }

    @Override
    public State clone() {
        try {
            State newState = (State) super.clone();
            newState.board = new int[SIZE][SIZE];
            for (int row = 0; row < SIZE; row++) {
                newState.board[row] = Arrays.copyOf(board[row], board[row].length);
            }
            return newState;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < SIZE; i++) {
            builder.append("\n");
            for (int j = 0; j < SIZE; j++) {
                builder.append(LABELS[board[i][j] + 1]).append(" ");
            }
        }
        return builder.substring(1);
    }

    private boolean maxTurn = false;
    private boolean terminal = false;
    private int[][] board = new int[SIZE][SIZE];
    private int cost = 0;
    private int depth = 0;

    private State() {
    }

    private void updateStatus(int i, int j) {
        int hSum = 0, vSum = 0, d1Sum = 0, d2Sum = 0;
        for (int k = 0; k < SIZE; k++) {
            if (Math.abs(hSum += board[i][k]) == SIZE
                    || Math.abs(vSum += board[k][j]) == SIZE) {
                cost = maxTurn ? 1 : -1;
                terminal = true;
                return;
            }
        }
        if (i == j) {
            for (int k = 0; k < SIZE; k++) {
                if (Math.abs(d1Sum += board[k][k]) == SIZE
                        || Math.abs(d2Sum += board[k][SIZE - 1 - k]) == SIZE) {
                    cost = maxTurn ? 1 : -1;
                    terminal = true;
                    return;
                }
            }
        }
        terminal = depth == SIZE * SIZE;
    }
}
