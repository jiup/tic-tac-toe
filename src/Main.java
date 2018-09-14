import agent.Agent;
import agent.BasicMiniMaxAgent;
import domain.State;
import org.junit.Test;

import java.io.PrintStream;
import java.util.Scanner;

import static constant.Board.SIZE;

public class Main {
    private static Agent agent = new BasicMiniMaxAgent();
    private static State currentState = getInitialState();

    private static Scanner sc = new Scanner(System.in);
    private static PrintStream info = System.err;
    private static PrintStream out = System.out;
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

    private static State humanTurn() {
        int input;
        int[] c;
        while (true) {
            info.print("\nyour turn (1-" + (SIZE * SIZE) + "): ");
            c = transCoord(input = sc.nextInt());
            if (input < 1 || input > SIZE * SIZE) {
                info.print("index out of bound, please try again!");
            } else if (currentState.getBoard()[c[0]][c[1]] != 0) {
                info.print("invalid position, please try again!");
            } else {
                break;
            }
        }
        return currentState.forward(c[0], c[1]);
    }

    private static State agentTurn() {
        State nextState = agent.forward(currentState);
        info.println("\nagent chose: " + transCoord(nextState.getLastStep()));
        return nextState;
    }

    private static void printResult() {
        info.print("\ngame over, ");
        switch (currentState.getCost()) {
            case 0:
                info.println("draw!");
                break;
            default:
                info.println(agentTurn ? "you win!" : "you lose!");
        }
    }

    public static void main(String[] args) {
        String input;
        info.print("you play 'X' or 'O'? ");
        while (!(input = sc.nextLine()).matches("[xXoO]"))
            info.print("invalid input, please try again!\nyou play 'X' or 'O'? ");

        agentTurn = input.matches("[oO]");
        while (!currentState.isTerminal()) {
            currentState = agentTurn ? agentTurn() : humanTurn();
            agentTurn = !agentTurn;
            info.println(currentState);
        }

        printResult();
    }

    @Test
    public void terminalStateTest() {
        State s = State.create()
                .forward(transCoord(5))
                .forward(transCoord(1))
                .forward(transCoord(3))
                .forward(transCoord(2))
                .forward(transCoord(7));
        System.out.println(s);
        System.out.println(s.isTerminal());
    }
}
