package advanced;

import advanced.agent.Agent;
import advanced.agent.HeuristicPrunedMinimaxAgent;
import advanced.domain.AdvanceState;
import advanced.domain.State;

import java.io.PrintStream;
import java.util.Scanner;

public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static PrintStream debug = System.err;
    private static PrintStream error = System.err;
    private static PrintStream info = System.out;

    private static Agent agent = new HeuristicPrunedMinimaxAgent();
    private static State currentState;
    private static boolean agentTurn;

    public static void main(String[] args) {
        do {
            selectTurn();
            while (!currentState.isTerminal()) {
                if (agentTurn) agentTurn(); else humanTurn();
                info.println(currentState.toStringWithStatus());
                agentTurn = !agentTurn;
            }
            debug.print("\ngame over, ");
            debug.println(currentState.getValue() == 0 ? "drawn" : (agentTurn ? "you win!" : "you lose!"));
        } while (askForAnotherTurn());
    }

    private static void selectTurn() {
        String input;
        debug.print("you play first? (y/n) ");
        while (!(input = sc.nextLine()).matches("[yYnN]"))
            error.print("invalid input!\nyou play first? (y/n) ");

        currentState = AdvanceState.create();
        agentTurn = input.matches("[nN]");
    }

    private static void humanTurn() {
        int[] lastPos = currentState.getLastStepPos();
        int boardIndex = lastPos[1], pos;
        boolean randomTake = boardIndex == 0 || currentState.getZoneCount(boardIndex) == 9;
        while (true) {
            info.print((randomTake ? "" : "#" + boardIndex) + "> ");
            try {
                String[] input = sc.nextLine().split(" +");
                if (input.length > 1) {
                    pos = Integer.parseInt(input[1]);
                    int inputBoardIndex = Integer.parseInt(input[0]);
                    if (!randomTake && inputBoardIndex != boardIndex) {
                        throw new IllegalStateException("invalid board index (should be " + boardIndex + ")");
                    }
                    if (randomTake) {
                        boardIndex = inputBoardIndex;
                    }
                } else {
                    pos = Integer.parseInt(input[0]);
                    if (randomTake) {
                        throw new IllegalStateException("you should specify board index & pos in this step (eg: 5 " + pos + ")");
                    }
                }
                currentState = currentState.take(boardIndex, pos);
                break;
            } catch (Exception e) {
                error.println(e.getMessage());
            }
        }
    }

    private static void agentTurn() {
        long start = System.nanoTime();
        int step = agent.handle(currentState);
        currentState = currentState.take(step);
        info.println(agent.getClass().getCanonicalName() + " used " + (System.nanoTime() - start) + " nano seconds");
    }

    private static boolean askForAnotherTurn() {
        String input;
        debug.print("restart a game? (y/n) ");
        while (!(input = sc.nextLine()).matches("[yYnN]"))
            error.print("invalid input!\nrestart a game? (y/n) ");

        if (input.matches("[nN]")) {
            debug.println("bye!");
            sc.close();
            return false;
        }
        return true;
    }
}
