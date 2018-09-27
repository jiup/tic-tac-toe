package advanced.agent;

import advanced.domain.State;

import java.util.List;

public class HeuristicPrunedMinimaxAgent implements Agent {
    private static final int SEARCH_DEPTH = 2;

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
        return optimalNextState.getLastStep();
    }

    private void calculateValue(State state, int alpha, int beta, int depth) {
        if (state.isTerminal() || depth == 0) return;

        boolean maxTurn = state.isMaxTurn();
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
}
