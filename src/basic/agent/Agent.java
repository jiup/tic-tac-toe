package basic.agent;

import basic.domain.State;

import java.util.ArrayList;
import java.util.List;

import static basic.constant.Board.SIZE;

public interface Agent {
    State forward(State state);
    default List<State> next(State state) {
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
}
