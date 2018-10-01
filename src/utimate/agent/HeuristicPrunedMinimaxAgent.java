package utimate.agent;

import advanced.domain.State;
import utimate.domain.UltimateState;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@SuppressWarnings("Duplicates")
public class HeuristicPrunedMinimaxAgent implements Agent {
    private static final int SEARCH_DEPTH = 10;
    private static Random random = new Random(System.nanoTime());

    @Override
    public int handle(State ultimateState) {
        UltimateState state = (UltimateState) ultimateState;
        List<State> actions = actions(state);
        Collections.shuffle(actions, random);
        State optimalNextState = actions.get(0);
        boolean nextStateMaxTurn = optimalNextState.isMaxTurn();
        int worstValue = nextStateMaxTurn ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        if (SEARCH_DEPTH > 0) {
            for (State s : actions) {
                calculateValue(s, Integer.MIN_VALUE, Integer.MAX_VALUE, SEARCH_DEPTH - 1);
                int optimalValue = optimalNextState.getValue();
                if (nextStateMaxTurn ? (s.getValue() > optimalValue) : (s.getValue() < optimalValue)) {
                    optimalNextState = s;
                }
                if (nextStateMaxTurn ? s.getValue() < worstValue : s.getValue() > worstValue) {
                    worstValue = s.getValue();
                }
//                 System.out.println(Arrays.toString(s.getLastStepPos()) + " -> " + s.getValue());
            }
            System.out.println("Distinction:     [" + Math.abs((optimalNextState.getValue() * .1 - worstValue * .1) * 10) + "]");
            System.out.println("Confidence:      [" + ((nextStateMaxTurn ? 1 : -1) * optimalNextState.getValue()) + "]");
        }
        return optimalNextState.getLastStep();
    }

    private void calculateValue(State ultimateState, int alpha, int beta, int depth) {
        UltimateState state = (UltimateState) ultimateState;
        if (state.isTerminal()) return;
        if (depth == 0) {
            state.setValue(heuristic(state));
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

    protected static int LOCAL_LOSE_PENALTY = 1_000_000;
    protected static int[] BOARD_POSITION_SCORE = new int[]{
            30000, 20000, 30000,
            20000, 40000, 20000,
            30000, 20000, 30000};
    protected static int RANDOM_PICK_PENALTY = 10_000;
    protected static int CHECK_COUNT_SCORE = 1_000; // expectation to create more check boards
    protected static int PIECE_COUNT_PENALTY = 100; // avoid sinking in opponent's advantage boards
    protected static int[] POSITION_SCORE = new int[]{30, 20, 30, 20, 40, 20, 30, 20, 30}; // take good places
    protected static int CHECK_BONUS = 5; // encourage check creation

    private int heuristic(UltimateState state) {
        int[] lastPos = state.getLastStepPos();
        boolean maxTurn = state.isMaxTurn();
        int boardIndex = lastPos[0];
        int pos = lastPos[1];
        int grade = 0;
        int terminateCount = 0;

        // #1 don't lose a local game
        if (maxTurn && state.getResult(pos) == -1 || !maxTurn && state.getResult(pos) == 1) {
            grade -= LOCAL_LOSE_PENALTY;
        }

        // #2 pick a better board to win
        for (int i = 1; i <= 9; i++) {
            switch (state.getResult(i)) {
                case 1:
                    grade += maxTurn ? BOARD_POSITION_SCORE[i - 1] : -BOARD_POSITION_SCORE[i - 1];
                    break;
                case -1:
                    grade += !maxTurn ? BOARD_POSITION_SCORE[i - 1] : -BOARD_POSITION_SCORE[i - 1];
                    break;
            }
        }

        // #3 avoid opponent's random state
        if (state.isLocalTerminal(boardIndex) && pos == boardIndex) {
            grade -= RANDOM_PICK_PENALTY;
        }

        // #4 choose better choice if avoid opponent's advantage boards is possible
        grade += CHECK_COUNT_SCORE * (maxTurn ? 1 : -1) * (state.getMaxCheckCount() - state.getMinCheckCount());

        return state.getValue() + (maxTurn ? grade : -grade);
    }
}
