package advanced.agent;

import advanced.domain.State;

import java.util.List;

public class HeuristicPrunedMinimaxAgent implements Agent {
    private static final int SEARCH_DEPTH = 10;
//    private static Random random = new Random(System.nanoTime());

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

        boolean maxTurn = !state.isMaxTurn(); // switch to next states
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
    protected static int CHECK_PENALTY = 10000; // cutoff stupid actions
    protected static int CHECK_COUNT_SCORE = 1000; // expectation to create more check boards
    protected static int PIECE_COUNT_PENALTY = 100; // avoid sinking in opponent's advantage boards
    protected static int[] POSITION_SCORE = new int[]{30, 20, 30, 20, 40, 20, 30, 20, 30}; // take good places
    protected static int CHECK_BONUS = 5; // duplicate with heuristic #1 ???

    private int heuristic(State state) {
        int[] lastPos = state.getLastStepPos();
        boolean maxTurn = state.isMaxTurn();
        int boardIndex = lastPos[0];
        int pos = lastPos[1];
        int grade = 0;

        // cutoff choosing opponent's check boards otherwise they'll win
        if (maxTurn && state.getMinCheck(pos) > 0 || !maxTurn && state.getMaxCheck(pos) > 0) {
            grade -= CHECK_PENALTY;
        }

        // expect for more checkmate count versus opponent's
        int maxCheckCount = 0, minCheckCount = 0;
        for (int i = 1; i <= 9; i++) {
            if (state.getMaxCheck(i) > 0) maxCheckCount++;
            if (state.getMinCheck(i) > 0) minCheckCount++;
        }
        if (maxTurn) {
            grade += maxCheckCount * CHECK_COUNT_SCORE; // 0.5?
            grade -= minCheckCount * CHECK_COUNT_SCORE; // 0.5?
        } else {
            grade += minCheckCount * CHECK_COUNT_SCORE; // 0.5?
            grade -= maxCheckCount * CHECK_COUNT_SCORE; // 0.5?
        }

        // avoid taking opponent's advantage boards (with more pieces)
        int count = 0;
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                if (state.board(pos, i, j) == (maxTurn ? State.O_PIECE : State.X_PIECE)) {
                    count++;
                }
            }
        }
        grade -=  count * PIECE_COUNT_PENALTY;

        // choose local pos_advantage
        grade = grade + POSITION_SCORE[pos - 1];

        // encourage check advantage
        if (maxTurn && state.getMaxCheck(boardIndex) > 0 || !maxTurn && state.getMinCheck(boardIndex) > 0) {
            grade += CHECK_BONUS;
        }

        return maxTurn ? grade : -grade;
    }
}
