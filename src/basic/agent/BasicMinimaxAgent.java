package basic.agent;

import basic.domain.State;

import java.util.List;

public class BasicMinimaxAgent implements Agent {

    @Override
    public State forward(State state) {
        List<State> nextStates = next(state);
        boolean maxTurn = state.isMaxTurn();
        State optimalNextState = nextStates.get(0);
        for (State s : nextStates) {
            calculateCost(s);
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

    private void calculateCost(State state) {
        int optimalCost = state.isMaxTurn() ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        if (!state.isTerminal()) {
            for (State s : next(state)) {
                calculateCost(s);
                if (state.isMaxTurn() ? s.getCost() > optimalCost : s.getCost() < optimalCost) {
                    optimalCost = s.getCost();
                }
            }
            state.setCost(optimalCost);
        }
    }
}
