package advanced.domain;

import java.util.Arrays;

public abstract class State implements Cloneable {
    protected static final boolean DRAW_BOUNDARY = true;
    public static final char EMPTY_PIECE = '-';
    public static final char X_PIECE = 'X';
    public static final char O_PIECE = 'O';

    public abstract State update();

    protected static int pieceValue(char c) {
        if (c == X_PIECE) return 1;
        if (c == O_PIECE) return -1;
        return 0;
    }

    protected char[] board = new char[81];
    protected int[] pieceCount = new int[9];
    protected int[] maxPieceCount = new int[9];
    protected int[] maxCheck = new int[9];
    protected int[] minCheck = new int[9];
    protected int maxCheckCount = 0;
    protected int minCheckCount = 0;
    protected boolean maxTurn = false;
    protected boolean terminal = false;
    protected int lastStep = -1;
    protected int value = 0;
    protected int depth = 0;

    protected State() {
        Arrays.fill(board, EMPTY_PIECE);
    }

    public State take(int index) {
        return take(index / 9 + 1, index % 9 + 1);
    }

    public State take(int boardIndex, int pos) {
        checkBoundValidity(boardIndex, pos);
        int index = (boardIndex - 1) * 9 + (pos - 1);
        State nextState = this.clone();
        nextState.board[index] = maxTurn ? O_PIECE : X_PIECE;
        nextState.maxTurn = !maxTurn;
        nextState.depth = depth + 1;
        nextState.lastStep = index;
        nextState.pieceCount[boardIndex - 1]++;
        if (!maxTurn) {
            nextState.maxPieceCount[boardIndex - 1]++;
        }
        return nextState.update();
    }

    public State take(int boardIndex, int i, int j) {
        return take(boardIndex, (i - 1) * 3 + j);
    }

    public int[] getPieceCount() {
        return pieceCount;
    }

    public int getPieceCount(int boardIndex) {
        return pieceCount[boardIndex - 1];
    }

    public int[] getMaxPieceCount() {
        return maxPieceCount;
    }

    public int getMaxPieceCount(int boardIndex) {
        return maxPieceCount[boardIndex - 1];
    }

    public int[] getMaxCheck() {
        return maxCheck;
    }

    public int getMaxCheck(int boardIndex) {
        return maxCheck[boardIndex - 1];
    }

    public int[] getMinCheck() {
        return minCheck;
    }

    public int getMinCheck(int boardIndex) {
        return minCheck[boardIndex - 1];
    }

    public int getMaxCheckCount() {
        return maxCheckCount;
    }

    public int getMinCheckCount() {
        return minCheckCount;
    }

    public boolean isMaxTurn() {
        return maxTurn;
    }

    public boolean isTerminal() {
        return terminal;
    }

    public int getLastStep() {
        return lastStep;
    }

    public int[] getLastStepPos() {
        return lastStep == -1 ? new int[]{0, 0} :
                new int[]{lastStep / 9 + 1, lastStep % 9 + 1};
    }

    public int[] getLastStepCoord() {
        return lastStep == -1 ? new int[]{0, 0, 0} :
                new int[]{lastStep / 9 + 1, (lastStep % 9) / 3 + 1, (lastStep % 9) % 3 + 1};
    }

    public int getValue() {
        return value;
    }

    public int getDepth() {
        return depth;
    }

    public void setLastStep(int lastStep) {
        this.lastStep = lastStep;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String toStringWithStatus() {
        return toString() +
                "Piece count:     " + Arrays.toString(pieceCount) + "\n" +
                "Max Piece count: " + Arrays.toString(maxPieceCount) + "\n" +
                "Max check:       " + Arrays.toString(maxCheck) + "[" + maxCheckCount + "]\n" +
                "Min check:       " + Arrays.toString(minCheck) + "[" + minCheckCount + "]\n" +
                "Last step:       " + Arrays.toString(getLastStepPos()) + "\n" +
                "Depth/Status:    [" + depth + "]" + (terminal ? "[TERMINAL]" : (maxTurn ? "[MAX]" : "[MIN]")) + "\n";
    }

    @Override
    public String toString() {
        char[][] tmp = new char[9][9];
        for (char[] row : tmp) Arrays.fill(row, ' ');
        int p = 0;
        for (int bi = 0; bi < 3; bi++) {
            for (int bj = 0; bj < 3; bj++) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        tmp[bi * 3 + i][bj * 3 + j] = board[p++];
                    }
                }
            }
        }

        String lineSplitter = "+-----------+-----------+-----------+\n";
        StringBuilder builder = new StringBuilder();
        if (DRAW_BOUNDARY) builder.append(lineSplitter);
        for (int i = 0; i < tmp.length; i++) {
            if (DRAW_BOUNDARY) builder.append("|  ");
            for (int j = 0; j < tmp[i].length; j++) {
                builder.append(tmp[i][j]).append("  ");
                if ((j + 1) % 3 == 0) {
                    if (DRAW_BOUNDARY) builder.append("|  ");
                    else builder.append("   ");
                }
            }
            builder.append("\n");
            if (i != tmp.length - 1 && (i + 1) % 3 == 0) {
                if (DRAW_BOUNDARY) builder.append(lineSplitter);
                else builder.append("\n");
            }
        }
        if (DRAW_BOUNDARY) builder.append(lineSplitter);
        return builder.toString();
    }

    @Override
    public State clone() {
        try {
            State newState = (State) super.clone();
            newState.board = Arrays.copyOf(board, 81);
            newState.pieceCount = Arrays.copyOf(pieceCount, 9);
            newState.maxPieceCount = Arrays.copyOf(maxPieceCount, 9);
            newState.maxCheck = Arrays.copyOf(maxCheck, 9);
            newState.minCheck = Arrays.copyOf(minCheck, 9);
            return newState;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public char board(int boardIndex, int pos) {
        return board[(boardIndex - 1) * 9 + (pos - 1)];
    }

    public char board(int boardIndex, int i, int j) {
        return board[(boardIndex - 1) * 9 + ((i - 1) * 3 + j - 1)];
    }

    protected void checkBoundValidity(int boardIndex, int pos) {
        if (boardIndex < 1 || boardIndex > 9)
            throw new IllegalArgumentException("board index out of bound: " + boardIndex);
        if (pos < 1 || pos > 9)
            throw new IllegalArgumentException("position out of bound: " + pos);
        if (board(boardIndex, pos) != EMPTY_PIECE)
            throw new IllegalStateException("piece crash on board #" + boardIndex + ": " + pos);
    }
}
