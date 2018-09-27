package basic;

import basic.agent.Agent;
import basic.agent.PrunedMinimaxAgent;
import basic.domain.State;

import java.io.PrintStream;
import java.util.Scanner;

import static basic.constant.Board.SIZE;

public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static PrintStream debug = System.out;
    private static PrintStream info = System.err;
    private static PrintStream out = System.out;

    private static Agent agent = new PrunedMinimaxAgent();
    private static State currentState;
    private static boolean agentTurn;

    private static State getInitialState() {
        return State.create();
    }

    private static int[] transCoord(int index) {
        return new int[]{(index - 1) / SIZE, (index - 1) % SIZE};
    }

    private static int transCoord(int[] index) {
        return index[0] * SIZE + index[1] + 1;
    }

    private static void selectTurn() {
        String input;
        info.print("you play 'X' or 'O'? ");
        while (!(input = sc.nextLine()).matches("[xXoO]"))
            info.print("invalid input, please try again!\nyou play 'X' or 'O'? ");

        currentState = getInitialState();
        agentTurn = input.matches("[oO]");
    }

    private static State humanTurn() {
        int[] c;
        while (true) {
            int input = sc.nextInt();
            c = transCoord(input);
            if (input < 1 || input > SIZE * SIZE) {
                info.println("index out of bound, please try again!");
                continue;
            } else if (currentState.getBoard()[c[0]][c[1]] != 0) {
                info.println("non-empty cell, please try again!");
                continue;
            }
            break;
        }
        return State.forward(currentState, c[0], c[1]);
    }

    private static State agentTurn() {
        long start = System.nanoTime();
        State nextState = agent.forward(currentState);
        out.println("basic.agent chose [" + transCoord(nextState.getLastStep())
                + "] in " + (System.nanoTime() - start) + " nano seconds");
        return nextState;
    }

    private static void printResult() {
        info.print("\ngame over, ");
        if (currentState.getCost() != 0) {
            info.println(agentTurn ? "you win!" : "you lose!");
        } else {
            info.println("draw!");
        }
    }

    private static boolean askForAnotherTurn() {
        String input;
        sc.nextLine();
        info.print("restart a game? (y/n) ");
        while (!(input = sc.nextLine()).matches("[yYnN]"))
            info.print("invalid input, please try again!\nrestart a game? (y/n) ");

        if (input.matches("[nN]")) {
            info.println("bye!");
            sc.close();
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        do {
            selectTurn();
            while (!currentState.isTerminal()) {
                currentState = agentTurn ? agentTurn() : humanTurn();
                agentTurn = !agentTurn;
                debug.println(currentState);
            }
            printResult();
        } while (askForAnotherTurn());
    }
}
