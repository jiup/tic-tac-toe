package utimate.agent;

import advanced.domain.State;
import utimate.domain.UltimateState;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
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
//        Collections.shuffle(actions, random);
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
                 System.out.println(Arrays.toString(optimalNextState.getLastStepPos()) + " -> " + optimalNextState.getValue());
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
            int h = heuristic(state);
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

    protected static int LOCAL_WIN_SCORE = 100_000;
    protected static int[] BOARD_POSITION_SCORE = new int[]{3, 2, 3, 2, 4, 2, 3, 2, 3};
    protected static int TERMINATE_PENALTY = 10_000;
    protected static int CHECK_COUNT_SCORE = 1_000; // expectation to create more check boards
    protected static int PIECE_COUNT_PENALTY = 100; // avoid sinking in opponent's advantage boards
    protected static int[] POSITION_SCORE = new int[]{30, 20, 30, 20, 40, 20, 30, 20, 30}; // take good places
    protected static int CHECK_BONUS = 5; // encourage check creation

    @SuppressWarnings("Duplicates")
    private int heuristic(UltimateState state) {
        int[] lastPos = state.getLastStepPos();
        boolean maxTurn = state.isMaxTurn();
        int boardIndex = lastPos[0];
        int pos = lastPos[1];
        int grade = 0;
        int terminateCount = 0;

        if (state.getValue() != 0) {
            return state.getValue(); // skip value assignment if value was given
        }

        for (int i = 1; i <= 9; i++) {
            if ((maxTurn && state.getResult(i) == 1) || (!maxTurn && state.getResult(i) == -1)) {
                grade += LOCAL_WIN_SCORE * BOARD_POSITION_SCORE[i - 1];
            }
            if (state.isLocalTerminal(i)) {
                terminateCount++;
//                grade += (maxTurn ? 1 : -1) * LOCAL_WIN_SCORE * BOARD_POSITION_SCORE[i - 1];
            }
        }

//        grade -= terminateCount * TERMINATE_PENALTY;

        // #1 cutoff choosing opponent's check boards otherwise they'll win
//        if (maxTurn && state.getMinCheck(pos) > 0 || !maxTurn && state.getMaxCheck(pos) > 0) {
//            return maxTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
//        }

        // #2 expect for more checkmate count versus opponent's
        grade += CHECK_COUNT_SCORE * (maxTurn ? 1 : -1) * (state.getMaxCheckCount() - state.getMinCheckCount());

        // #3 avoid taking opponent's advantage boards (with more pieces)
        grade -= PIECE_COUNT_PENALTY * (maxTurn ? state.getPieceCount(pos) - state.getMaxPieceCount(pos) : state.getMaxPieceCount(pos));

        // #4 choose local pos_advantage
        grade += POSITION_SCORE[pos - 1];


        // #5 encourage check advantage
        if (maxTurn && state.getMaxCheck(boardIndex) > 0 || !maxTurn && state.getMinCheck(boardIndex) > 0) {
            grade += CHECK_BONUS; // 5? 6? 10?
        }

        return maxTurn ? grade : -grade;
    }

    static class StdPrintStream extends PrintStream {
        public StdPrintStream(OutputStream out) {
            super(out);
        }

        @Override
        public void println(String x) {
            try {
                Thread.sleep(10);
                super.println(x);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
