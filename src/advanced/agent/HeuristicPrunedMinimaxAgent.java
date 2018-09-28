package advanced.agent;

import advanced.domain.State;

import java.util.List;

public class HeuristicPrunedMinimaxAgent implements Agent {
    private static final int SEARCH_DEPTH = 1;
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
            long start = System.nanoTime();
            int h = heuristic(state);
            System.out.println(state);
            System.out.println("Heuristic value: " + h + ", time: " + (System.nanoTime() - start) + " ns");
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
    protected static int[] POS_SCORE = new int[]{3, 2, 3, 2, 4, 2, 3, 2, 3};
    protected static int POS_SCALE = 100;

    private int heuristic(State state) {
        int[] lastPos = state.getLastStepPos();
        boolean maxTurn = state.isMaxTurn();
        int boardIndex = lastPos[0];
        int grade = 0;

        // exclude check boards
        if (state.getMaxCheck(boardIndex) > 0 || state.getMinCheck(boardIndex) > 0) {
            grade = maxTurn ? grade - CHECK_SCORE : grade + CHECK_SCORE;
        }
        // choose local pos_advantage
        grade = maxTurn ?
                grade + POS_SCALE * POS_SCORE[lastPos[1] - 1] :
                grade - POS_SCALE * POS_SCORE[lastPos[1] - 1];

        return grade;
    }
}
