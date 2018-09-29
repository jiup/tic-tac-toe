package advanced.agent;

import advanced.domain.State;

import java.util.ArrayList;
import java.util.List;

public interface Agent {
    int handle(State state);
    
    default List<State> actions(State state) {
        List<State> actions = new ArrayList<>();
        if (state.isTerminal()) return actions;

        int[] lastPos = state.getLastStepPos();
        int boardIndex = lastPos[1];
        boolean fullSearch = boardIndex == 0 || state.getPieceCount(boardIndex) == 9;
        if (fullSearch) {
            for (int bI = 1; bI <= 9; bI++) {
                if (bI != boardIndex) {
                    for (int i = 1; i <= 9; i++) {
                        if (state.board(bI, i) == State.EMPTY_PIECE) {
                            actions.add(state.take(bI, i));
                        }
                    }
                }
            }
        } else {
            for (int i = 1; i <= 9; i++) {
                if (state.board(boardIndex, i) == State.EMPTY_PIECE) {
                    actions.add(state.take(boardIndex, i));
                }
            }
        }
        return actions;
    }
}
