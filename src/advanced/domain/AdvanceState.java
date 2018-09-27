package advanced.domain;

public class AdvanceState extends State {

    public static AdvanceState create() {
        return new AdvanceState();
    }

    private AdvanceState() {
    }

    @Override
    protected State update() {
        int hSum = 0, vSum = 0, d1Sum = 0, d2Sum = 0;
        int[] coord = getLastStepCoord();
        int boardIndex = coord[0];
        int i = coord[1], j = coord[2];
        for (int k = 1; k <= 3; k++) {
            if (Math.abs(hSum += pieceValue(board(boardIndex, i, k))) == 3) {
                value = maxTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
                terminal = true;
                return this;
            }
            if (Math.abs(vSum += pieceValue(board(boardIndex, k, j))) == 3) {
                value = maxTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
                terminal = true;
                return this;
            }
        }
        if (i == j || i + j == 2) {
            for (int k = 1; k <= 3; k++) {
                if (Math.abs(d1Sum += pieceValue(board(boardIndex, k, k))) == 3) {
                    value = maxTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
                    terminal = true;
                    return this;
                }
                if (Math.abs(d2Sum += pieceValue(board(boardIndex, k, 2 - k))) == 3) {
                    value = maxTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
                    terminal = true;
                    return this;
                }
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
