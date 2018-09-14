package domain;

import java.util.Arrays;

import static constant.Board.SIZE;

public class State implements Cloneable {
    private static final char[] LABELS = {'O', '-', 'X'};

    public static State create() {
        return new State();
    }

    public static State forward(State currentState, int i, int j) {
        if (currentState.terminal)
            throw new RuntimeException("game was over -\n" + currentState.toString());

        if (currentState.board[i][j] != 0)
            throw new RuntimeException("piece crash at (" + i + ", " + j + ")");

        State nextState = currentState.clone();
        nextState.maxTurn = !currentState.maxTurn;
        nextState.board[i][j] = currentState.maxTurn ? 1 : -1;
        nextState.lastStep = new int[]{i, j};
        nextState.depth++;
        return nextState.update();
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

    public int[] getLastStep() {
        return lastStep;
    }

    public int getCost() {
        return cost;
    }

    public int getDepth() {
        return depth;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public State clone() {
        try {
            State newState = (State) super.clone();
            newState.lastStep = Arrays.copyOf(lastStep, 2);
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

    private boolean maxTurn = true;
    private boolean terminal = false;
    private int[][] board = new int[SIZE][SIZE];
    private int[] lastStep = {-1, -1};
    private int cost = 0;
    private int depth = 0;

    private State() {
    }

    private State update() {
        int hSum = 0, vSum = 0, d1Sum = 0, d2Sum = 0;
        int i = lastStep[0], j = lastStep[1];
        for (int k = 0; k < SIZE; k++) {
            if (Math.abs(hSum += board[i][k]) == SIZE) {
                cost = hSum > 0 ? 1 : -1;
                terminal = true;
                return this;
            }
            if (Math.abs(vSum += board[k][j]) == SIZE) {
                cost = vSum > 0 ? 1 : -1;
                terminal = true;
                return this;
            }
        }
        if (i == j || i + j == SIZE - 1) {
            for (int k = 0; k < SIZE; k++) {
                if (Math.abs(d1Sum += board[k][k]) == SIZE) {
                    cost = d1Sum > 0 ? 1 : -1;
                    terminal = true;
                    return this;
                }
                if (Math.abs(d2Sum += board[k][SIZE - 1 - k]) == SIZE) {
                    cost = d2Sum > 0 ? 1 : -1;
                    terminal = true;
                    return this;
                }
            }
        }
        terminal = depth == SIZE * SIZE;
        return this;
    }
}
