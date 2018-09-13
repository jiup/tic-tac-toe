package domain;

import java.util.Arrays;

public class State {
    public static final char[] LABELS = {'O', '-', 'X'};
    public static final int PLAYER_NUM = 2;
    public static final int SIZE = 3;

    private int[] costs = new int[PLAYER_NUM];
    private int[] board = new int[SIZE * SIZE];
    private int lastStep = -1;

    public State() {
    }

    public State(int[] board, int lastStep) {
        this.board = Arrays.copyOf(board, SIZE * SIZE);
        this.lastStep = lastStep;
    }

    public int[] getLastStep() {
        return lastStep < 0 ? null : new int[]{lastStep / SIZE, lastStep % SIZE};
    }

    public char getLastPawn() {
        return lastStep < 0 ? ' ' : LABELS[board[lastStep] + 1];
    }

    public boolean isTerminal() {
        int bSlashSum = 0, slashSum = 0;
        for (int i = 0; i < SIZE; i++) {
            int rowSum = 0, colSum = 0;
            for (int j = 0; j < SIZE; j++) {
                if (Math.abs(rowSum += board(i, j)) == SIZE
                        || Math.abs(colSum += board(j, i)) == SIZE) {
                    return true;
                }
            }
            if (Math.abs(bSlashSum += board(i, i)) == SIZE
                    || Math.abs(slashSum += board(i, SIZE - 1 - i)) == SIZE) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            builder.append(LABELS[board[i] + 1]).append(" ")
                    .append((i + 1) % SIZE == 0 ? "\n" : "");
        }
        return builder.toString();
    }

    private int board(int i, int j) {
        return board[i * SIZE + j];
    }
}
