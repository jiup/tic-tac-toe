package advanced.domain;

public class AdvanceState extends State {

    public static AdvanceState create() {
        return new AdvanceState();
    }

    private AdvanceState() {
    }

    @Override
    public State update() {
        int hSum = 0, vSum = 0, d1Sum = 0, d2Sum = 0;
        int[] coord = getLastStepCoord();
        int boardIndex = coord[0];
        int i = coord[1], j = coord[2];
        for (int k = 1; k <= 3; k++) {
            if (Math.abs(hSum += pieceValue(board(boardIndex, i, k))) == 3) {
                value = maxTurn ? Integer.MAX_VALUE : Integer.MIN_VALUE;
                terminal = true;
                return this;
            }
            if (Math.abs(vSum += pieceValue(board(boardIndex, k, j))) == 3) {
                value = maxTurn ? Integer.MAX_VALUE : Integer.MIN_VALUE;
                terminal = true;
                return this;
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
                if (Math.abs(d1Sum += pieceValue(board(boardIndex, k, k))) == 3) {
                    value = maxTurn ? Integer.MAX_VALUE : Integer.MIN_VALUE;
                    terminal = true;
                    return this;
                }
                if (Math.abs(d2Sum += pieceValue(board(boardIndex, k, 4 - k))) == 3) {
                    value = maxTurn ? Integer.MAX_VALUE : Integer.MIN_VALUE;
                    terminal = true;
                    return this;
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
        boolean boardFull = true;
        for (int c : pieceCount) {
            if (c < 9) boardFull = false;
        }
        if (boardFull) {
            terminal = true;
            value = 0; // draw
        }
        return this;
    }

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
