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
            if (maxTurn) maxCheck[boardIndex - 1]++;
            else minCheck[boardIndex - 1]++;
        }
        if (Math.abs(vSum) == 2) {
            if (maxTurn) maxCheck[boardIndex - 1]++;
            else minCheck[boardIndex - 1]++;
        }
        if ((maxTurn && hSum == -1) || (!maxTurn && hSum == 1)) {
            if (maxTurn) minCheck[boardIndex - 1]--;
            else maxCheck[boardIndex - 1]--;
        }
        if ((maxTurn && vSum == -1) || (!maxTurn && vSum == 1)) {
            if (maxTurn) minCheck[boardIndex - 1]--;
            else maxCheck[boardIndex - 1]--;
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
                if (maxTurn) maxCheck[boardIndex - 1]++;
                else minCheck[boardIndex - 1]++;
            }
            if (Math.abs(d2Sum) == 2) {
                if (maxTurn) maxCheck[boardIndex - 1]++;
                else minCheck[boardIndex - 1]++;
            }
            if ((maxTurn && d1Sum == -1) || (!maxTurn && d1Sum == 1)) {
                if (maxTurn) minCheck[boardIndex - 1]--;
                else maxCheck[boardIndex - 1]--;
            }
            if ((maxTurn && d2Sum == -1) || (!maxTurn && d2Sum == 1)) {
                if (maxTurn) minCheck[boardIndex - 1]--;
                else maxCheck[boardIndex - 1]--;
            }
        }
        boolean boardFull = true;
        for (int c : zoneCount) {
            if (c < 9) boardFull = false;
        }
        if (boardFull) {
            terminal = true;
            value = 0; // draw
        }
        return this;
    }
}
