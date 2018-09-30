package utimate.domain;

import advanced.domain.State;

import java.util.Arrays;

public class UltimateState extends State {
    public static final int LOCAL_WIN_VALUE = 50_000_000;
    public static final int GLOBAL_WIN_VALUE = 500_000_000;
    protected int[] result = new int[9]; // 1: x win, -1: o win, 0: draw or undetermined
    protected boolean[] localTerminal = new boolean[9]; // each board on going

    public static UltimateState create() {
        return new UltimateState();
    }

    private UltimateState() {
    }

    public int[] getResult() {
        return result;
    }

    public int getResult(int boardIndex) {
        return result[boardIndex - 1];
    }

    public boolean[] isLocalTerminal() {
        return localTerminal;
    }

    public boolean isLocalTerminal(int boardIndex) {
        return localTerminal[boardIndex - 1];
    }

    @Override
    public UltimateState take(int index) {
        return (UltimateState) super.take(index);
    }

    @Override
    public UltimateState take(int boardIndex, int pos) {
        return (UltimateState) super.take(boardIndex, pos);
    }

    @Override
    public UltimateState take(int boardIndex, int i, int j) {
        return (UltimateState) super.take(boardIndex, i, j);
    }

    @Override
    protected void checkBoundValidity(int boardIndex, int pos) {
        if (localTerminal[boardIndex - 1])
            throw new IllegalArgumentException("board #" + boardIndex + " terminated");

        super.checkBoundValidity(boardIndex, pos);
    }

    @Override
    public State clone() {
        UltimateState newState = (UltimateState) super.clone();
        newState.result = Arrays.copyOf(result, 9);
        newState.localTerminal = Arrays.copyOf(localTerminal, 9);
        return newState;
    }

    @Override
    public String toStringWithStatus() {
        StringBuilder builder = new StringBuilder();
        for (int r : result) {
            if (r == 1) {
                builder.append("MAX, ");
            } else if (r == -1) {
                builder.append("MIN, ");
            } else {
                builder.append("0, ");
            }
        }
        String gameResult = builder.substring(0, builder.length() - 2);
        builder = new StringBuilder();
        for (boolean b : localTerminal) {
            builder.append(b ? '1' : '0').append(", ");
        }
        return super.toStringWithStatus() +
                "Game result:     [" + gameResult + "]\n" +
                "Terminated:      [" + builder.substring(0, builder.length() - 2) + "]\n";
    }

    @Override
    public State update() {
        int hSum = 0, vSum = 0, d1Sum = 0, d2Sum = 0;
        int[] coord = getLastStepCoord();
        int boardIndex = coord[0];
        if (pieceCount[boardIndex - 1] == 9) {
            localTerminal[boardIndex - 1] = true;
            return updateGlobalTerminal(boardIndex);
        }
        int i = coord[1], j = coord[2];
        for (int k = 1; k <= 3; k++) {
            if (Math.abs(hSum += pieceValue(board(boardIndex, i, k))) == 3
                    || Math.abs(vSum += pieceValue(board(boardIndex, k, j))) == 3) {
                value = (maxTurn ? 1 : -1) * LOCAL_WIN_VALUE;
                result[boardIndex - 1] = maxTurn ? 1 : -1;
                localTerminal[boardIndex - 1] = true;
                return updateGlobalTerminal(boardIndex);
            }
        }
        if (Math.abs(hSum) == 2) {
            increaseCheckRecords(maxTurn, boardIndex);
        }
        if (Math.abs(vSum) == 2) {
            increaseCheckRecords(maxTurn, boardIndex);
        }
        if ((maxTurn && hSum == -1) || (!maxTurn && hSum == 1)) {
            decreaseCheckRecords(maxTurn, boardIndex);
        }
        if ((maxTurn && vSum == -1) || (!maxTurn && vSum == 1)) {
            decreaseCheckRecords(maxTurn, boardIndex);
        }
        if (i == j || i + j == 4) {
            for (int k = 1; k <= 3; k++) {
                if (Math.abs(d1Sum += pieceValue(board(boardIndex, k, k))) == 3
                        || Math.abs(d2Sum += pieceValue(board(boardIndex, k, 4 - k))) == 3) {
                    value = (maxTurn ? 1 : -1) * LOCAL_WIN_VALUE;
                    result[boardIndex - 1] = maxTurn ? 1 : -1;
                    localTerminal[boardIndex - 1] = true;
                    return updateGlobalTerminal(boardIndex);
                }
            }
            if (i == 1 && j == 3 || i == 3 && j == 1) d1Sum = 0;
            if (i == 1 && j == 1 || i == 3 && j == 3) d2Sum = 0;
            if (Math.abs(d1Sum) == 2) {
                increaseCheckRecords(maxTurn, boardIndex);
            }
            if (Math.abs(d2Sum) == 2) {
                increaseCheckRecords(maxTurn, boardIndex);
            }
            if ((maxTurn && d1Sum == -1) || (!maxTurn && d1Sum == 1)) {
                decreaseCheckRecords(maxTurn, boardIndex);
            }
            if ((maxTurn && d2Sum == -1) || (!maxTurn && d2Sum == 1)) {
                decreaseCheckRecords(maxTurn, boardIndex);
            }
        }
        return this;
    }

    private State updateGlobalTerminal(int boardIndex) {
        int hSum = 0, vSum = 0, d1Sum = 0, d2Sum = 0;
        int i = (boardIndex + 2) / 3, j = (boardIndex + 2) % 3 + 1;
        for (int k = 1; k <= 3; k++) {
            if (Math.abs(hSum += result(i, k)) == 3 || Math.abs(vSum += result(k, j)) == 3) {
                terminal = true;
                value = GLOBAL_WIN_VALUE;
                return this;
            }
        }
        if (i == j || i + j == 4) {
            for (int k = 1; k <= 3; k++) {
                if (Math.abs(d1Sum += result(k, k)) == 3 || Math.abs(d2Sum += result(k, 4 - k)) == 3) {
                    terminal = true;
                    value = GLOBAL_WIN_VALUE;
                    return this;
                }
            }
        }

        for (boolean t : localTerminal) {
            if (!t) {
                return this;
            }
        }
        terminal = true;
        value = 0; // draw
        return this;
    }

    private int result(int i, int j) {
        return result[(i - 1) * 3 + j - 1];
    }

    @SuppressWarnings("Duplicates")
    private void increaseCheckRecords(boolean maxTurn, int boardIndex) {
        int tmp;
        if (maxTurn) {
            tmp = ++maxCheck[boardIndex - 1];
            if (tmp == 1) maxCheckCount++;
        } else {
            tmp = ++minCheck[boardIndex - 1];
            if (tmp == 1) minCheckCount++;
        }
    }

    @SuppressWarnings("Duplicates")
    private void decreaseCheckRecords(boolean maxTurn, int boardIndex) {
        int tmp;
        if (maxTurn) {
            tmp = --minCheck[boardIndex - 1];
            if (tmp == 0) minCheckCount--;
        } else {
            tmp = --maxCheck[boardIndex - 1];
            if (tmp == 0) maxCheckCount--;
        }
    }
}
