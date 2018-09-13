import agent.Agent;
import agent.BasicMiniMaxAgent;
import domain.State;

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
        do {
            info.print("\nyour turn (1-" + (SIZE * SIZE) + "): ");
            c = transCoord(input = sc.nextInt());
            if (input > SIZE * SIZE || input < 1) {
                info.print("index out of bound, please try again!");
            } else if (currentState.getBoard()[c[0]][c[1]] != 0) {
                info.print("piece crash, please try again!");
            } else {
                break;
            }
        } while (true);
        return currentState.forward(c[0], c[1]);
    }

    private static State agentTurn() {
        info.println("\nagent chose: ?");
        return agent.forward(currentState);
    }

    private static void printResult() {
        info.print("\nGame over, ");
        switch (currentState.getCost()) {
            case -1:
                info.println(agentTurn ? "you lose!" : "you win!");
                break;
            case 1:
                info.println(agentTurn ? "you win!" : "you lose!");
                break;
            case 0:
                info.println("draw!");
                break;
        }
        info.println(currentState);
    }

    public static void main(String[] args) {
        String input;
        info.print("you play 'X' or 'O'? ");
        while (!(input = sc.nextLine()).matches("[xXoO]"))
            info.print("invalid input\nyou play 'X' or 'O'? ");

        agentTurn = !input.matches("[xX]");
        while (!currentState.isTerminal()) {
            currentState = agentTurn ? agentTurn() : humanTurn();
            agentTurn = !agentTurn;
            info.println(currentState);
        }

        printResult();
    }
}
