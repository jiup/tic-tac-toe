package agent;

import domain.State;

import java.util.ArrayList;
import java.util.List;

import static constant.Board.SIZE;

public class BasicMiniMaxAgent implements Agent {

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

    private List<State> next(State state) {
        List<State> nextStates = new ArrayList<>();
        if (state.isTerminal())
            return nextStates;

        int[][] board = state.getBoard();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == 0) {
                    nextStates.add(State.forward(state.clone(), i, j));
                }
            }
        }
        return nextStates;
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
