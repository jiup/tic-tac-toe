package advanced.agent;

import advanced.domain.State;

import java.util.List;

public class HeuristicPrunedMinimaxAgent implements Agent {
    private static final int SEARCH_DEPTH = 0;
//    static Random random = new Random(System.nanoTime());

    @Override
    public int handle(State state) {
        List<State> actions = actions(state);
        State optimalNextState = actions.get(0);
        for (State s : actions) {
            calculateValue(s, Integer.MIN_VALUE, Integer.MAX_VALUE, SEARCH_DEPTH);
            int optimalValue = optimalNextState.getValue();
            if (s.isMaxTurn() ? (s.getValue() > optimalValue) : (s.getValue() < optimalValue)) {
                optimalNextState = s;
            }
        }
//        return actions.get(random.nextInt(actions.size())).getLastStep();
        return optimalNextState.getLastStep();
    }

    private void calculateValue(State state, int alpha, int beta, int depth) {
        if (state.isTerminal()) return;
        if (depth == 0) {
//            long start = System.nanoTime();
            int h = heuristic(state);
//            System.out.println(state);
//            System.out.println("Heuristic value: " + h + ", time: " + (System.nanoTime() - start) + " ns");
            state.setValue(h);
            return;
        }

        boolean maxTurn = !state.isMaxTurn();
        int optimalValue = maxTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (State s : actions(state)) {
            calculateValue(s, alpha, beta, depth - 1);
            if (maxTurn ? s.getValue() > optimalValue : s.getValue() < optimalValue) {
                optimalValue = s.getValue();
            }
            if (maxTurn ? optimalValue >= beta : optimalValue <= alpha) break;

            if (maxTurn) alpha = optimalValue;
            else beta = optimalValue;
        }
        state.setValue(optimalValue);
    }

    // Heuristic grading of state
    protected static int CHECK_SCORE = 1000;
    protected static int CHESS_COUNT_SCORE = 100;
    protected static int[] POS_SCORE = new int[]{3, 2, 3, 2, 4, 2, 3, 2, 3};
    protected static int POS_SCALE = 10;

    private int heuristic(State state) {
        int[] lastPos = state.getLastStepPos();
        boolean maxTurn = state.isMaxTurn();
        int boardIndex = lastPos[0];
        int pos = lastPos[1];
        int grade = 0;

        // exclude opponent's check boards
        if (state.getMaxCheck(pos) == 0 && state.getMinCheck(pos) == 0) {
            grade += 2 * CHECK_SCORE;
        } else {
            if (maxTurn && state.getMaxCheck(pos) > 0 && state.getMinCheck(pos) == 0
                    || (!maxTurn && state.getMinCheck(pos) > 0) && state.getMaxCheck(pos) == 0) {
                grade += CHECK_SCORE;
            } else {
                grade -= CHECK_SCORE;
            }
        }

        // avoid opponent's chess count
        int count = 0;
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                if (state.board(pos, i, j) == (maxTurn ? State.O_PIECE : State.X_PIECE)) {
                    count++;
                }
            }
        }
//        System.out.println("opponent count: " + count);
        grade -=  count * CHESS_COUNT_SCORE;

        // choose local pos_advantage
        grade = grade + POS_SCALE * POS_SCORE[pos - 1];

        // encourage check advantage
        if (maxTurn && state.getMaxCheck(boardIndex) > 0 || !maxTurn && state.getMinCheck(boardIndex) > 0) {
            grade += 5;
        }

        return maxTurn ? grade : -grade;
    }
}
