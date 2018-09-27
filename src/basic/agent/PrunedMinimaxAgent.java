package basic.agent;

import basic.domain.State;

import java.util.List;

public class PrunedMinimaxAgent implements Agent {
    @Override
    public State forward(State state) {
        List<State> nextStates = next(state);
        boolean maxTurn = state.isMaxTurn();
        State optimalNextState = nextStates.get(0);
        for (State s : nextStates) {
            calculateCost(s, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (maxTurn) {
                if (s.getCost() > optimalNextState.getCost()) {
                    optimalNextState = s;
                }
            } else {
                if (s.getCost() < optimalNextState.getCost()) {
                    optimalNextState = s;
                }
            }
        }
        return optimalNextState;
    }

    private void calculateCost(State state, int alpha, int beta) {
        int optimalCost = state.isMaxTurn() ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        if (state.isTerminal()) {
            return;
        }

        for (State s : next(state)) {
            calculateCost(s, alpha, beta);
            if (state.isMaxTurn() ? s.getCost() > optimalCost : s.getCost() < optimalCost) {
                optimalCost = s.getCost();
            }
            if (state.isMaxTurn() ? optimalCost >= beta : optimalCost <= alpha) {
                break;
            }
            if (state.isMaxTurn()) {
                alpha = optimalCost;
            } else {
                beta = optimalCost;
            }
        }
        state.setCost(optimalCost);
    }
}
