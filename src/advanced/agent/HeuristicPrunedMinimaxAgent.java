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


//    /**
//     * A private function to see if a sub-board(3X3) is a restricted board for a player.
//     *
//     * @param An array of integer represents a sub-board(3X3), and current player(true --> max, false --> min)
//     * @return An integer: -1 -- opponent is going to win regardless of whether or not
//     * current player is going to win; 1 --- current player is going to win and opponent
//     * is not going to win; 2 --- none of players are going to win.
//     *
//     */
//    private int restrictedOrNot(int[] SubBoard, boolean player){
//        if (player == true){ // currently max turn
//            if(SubBoard[0]+SubBoard[1]+SubBoard[2] == -2 || SubBoard[3]+SubBoard[4]+SubBoard[5] == -2
//                    || SubBoard[6]+SubBoard[7]+SubBoard[8] == -2 || SubBoard[0]+SubBoard[3]+SubBoard[6] == -2
//                    || SubBoard[1]+SubBoard[5]+SubBoard[7] == -2 || SubBoard[2]+SubBoard[5]+SubBoard[8] == -2
//                    || SubBoard[0]+SubBoard[4]+SubBoard[8] == -2 || SubBoard[2]+SubBoard[4]+SubBoard[6] == -2){ // opponent(min) takes control
//                return -1;
//            } else if(SubBoard[0]+SubBoard[1]+SubBoard[2] == 2 || SubBoard[3]+SubBoard[4]+SubBoard[5] == 2
//                    || SubBoard[6]+SubBoard[7]+SubBoard[8] == 2 || SubBoard[0]+SubBoard[3]+SubBoard[6] == 2
//                    || SubBoard[1]+SubBoard[5]+SubBoard[7] == 2 || SubBoard[2]+SubBoard[5]+SubBoard[8] == 2
//                    || SubBoard[0]+SubBoard[4]+SubBoard[8] == 2 || SubBoard[2]+SubBoard[4]+SubBoard[6] == 2){ // itself(max) takes control
//                return 1;
//            }
//        } else{ // currently min turn
//            if(SubBoard[0]+SubBoard[1]+SubBoard[2] == -2 || SubBoard[3]+SubBoard[4]+SubBoard[5] == -2
//                    || SubBoard[6]+SubBoard[7]+SubBoard[8] == -2 || SubBoard[0]+SubBoard[3]+SubBoard[6] == -2
//                    || SubBoard[1]+SubBoard[5]+SubBoard[7] == -2 || SubBoard[2]+SubBoard[5]+SubBoard[8] == -2
//                    || SubBoard[0]+SubBoard[4]+SubBoard[8] == -2 || SubBoard[2]+SubBoard[4]+SubBoard[6] == -2){ // itself(min) takes control
//                return 1;
//            } else if(SubBoard[0]+SubBoard[1]+SubBoard[2] == 2 || SubBoard[3]+SubBoard[4]+SubBoard[5] == 2
//                    || SubBoard[6]+SubBoard[7]+SubBoard[8] == 2 || SubBoard[0]+SubBoard[3]+SubBoard[6] == 2
//                    || SubBoard[1]+SubBoard[5]+SubBoard[7] == 2 || SubBoard[2]+SubBoard[5]+SubBoard[8] == 2
//                    || SubBoard[0]+SubBoard[4]+SubBoard[8] == 2 || SubBoard[2]+SubBoard[4]+SubBoard[6] == 2){ // opponent(max) takes control
//                return -1;
//            }
//        }
//        return 2;
//    }
//
//    /**
//     * A private function to count the number of opponent in a sub-board(3X3)
//     *
//     * @param An array of integer represents a sub-board, and current player(true --> max, false --> min)
//     * @return An integer that represents the number of opponents in a sub-board(3X3)
//     *
//     */
//    private int opponentNumber(int[] SubBoard, boolean player){
//        int playerInt = 0;
//        if (player == true){ // max
//            playerInt = 1;
//        } else{ // min
//            playerInt = -1;
//        }
//        int counter = 0;
//        for(int i=0; i<9; i++){
//            if(SubBoard[i] != playerInt) {
//                counter ++;
//            }
//        }
//        return counter;
//    }
//
//
//    /**
//     * A private function that give an estimation of value for one location in a sub-board(3X3)
//     *
//     * @param An array of integer represents a sub-board, and current player(true --> max,
//     * 		false --> min), an integer represents specific place in the sub-board.
//     * @return An integer that represents the value of a place in a sub-board(3X3)
//     *
//     */
//    private int valueOfPlace(int[] subBoard, boolean player, int place){
//        // currently simple version
//        if(place == 5 && subBoard[4] == 0) {
//            return 3;
//        } else if((place == 1 && subBoard[0] == 0) || (place == 3 && subBoard[2] == 0) ||
//                (place == 7 && subBoard[6] == 0) || (place == 9 && subBoard[8] == 0)) {
//            return 2;
//        }else if((place == 2 && subBoard[1] == 0) || (place == 4 && subBoard[3] == 0) ||
//                (place == 6 && subBoard[5] == 0) || (place == 8 && subBoard[7] == 0)) {
//            return 1;
//        }
//        return 0;
//    }
//
//    // helper function smae as pieceValue in state.java
//    int pieceValue2(char c) {
//        if (c == 'X') return 1;
//        if (c == 'O') return -1;
//        return 0;
//    }
//
//
//
//    private int heuristic2(State state) {
//        int Score = 0;
//        for(int i=1; i<10; i++){
//            int[] subBoard = new int[9];
//            int tempS = 0; // temporary int that keep track of highest score of 9 sub-boards
//            int tempPlaceMax = 0; // temporary int that keep track of highest score of a place in a sub-board
//            for(int j=1; j<10; j++){ // go through 9 sub-boards to get score
//                int P = pieceValue2( state.board(i, j) );
//                subBoard[j-1] = P;
//                int placeValue = valueOfPlace(subBoard, state.isMaxTurn(), j);
//                if (placeValue > tempPlaceMax){
//                    tempPlaceMax = placeValue;
//                }
//            }
//            tempS += restrictedOrNot(subBoard, state.isMaxTurn())*1000;
//            tempS += opponentNumber(subBoard, state.isMaxTurn())*100;
//            tempS += tempPlaceMax;
//            if(tempS > Score){
//                Score = tempS;
//            }
//        }
//        return Score;
//    }
}
