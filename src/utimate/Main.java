package utimate;

import utimate.agent.Agent;
import utimate.agent.HeuristicPrunedMinimaxAgent;
import utimate.domain.UltimateState;

import java.io.PrintStream;
import java.util.Scanner;

@SuppressWarnings("Duplicates")
public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static PrintStream debug = System.err;
    private static PrintStream error = System.err;
    private static PrintStream info = System.out;

    private static Agent agent = new HeuristicPrunedMinimaxAgent();
    private static UltimateState currentState;
    private static boolean agentTurn;

    public static void main(String[] args) {
        do {
            selectTurn();
            while (!currentState.isTerminal()) {
                if (agentTurn) agentTurn();
                else humanTurn();
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

        currentState = UltimateState.create();
        agentTurn = input.matches("[nN]");
    }

    private static void humanTurn() {
        int[] lastPos = currentState.getLastStepPos();
        int boardIndex = lastPos[1], pos;
        boolean randomTake = boardIndex == 0
                || currentState.getPieceCount(boardIndex) == 9
                || currentState.isLocalTerminal(boardIndex);
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
        info.println(agent.getClass().getSimpleName() + " used " + (System.nanoTime() - start) + " nano seconds");
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

/*
/Library/Java/JavaVirtualMachines/jdk-10.0.2.jdk/Contents/Home/bin/java "-javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=55753:/Applications/IntelliJ IDEA.app/Contents/bin" -Dfile.encoding=UTF-8 -classpath /Users/jp/Documents/Develop/Courseworks/CSC442/ttt/out/production/ttt:/Users/jp/.m2/repository/junit/junit/4.12/junit-4.12.jar:/Users/jp/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar utimate.Main
you play first? (y/n) n
Distinction:     [0.0]
Confidence:      [40]
HeuristicPrunedMinimaxAgent used 15686151425 nano seconds
+-----------+-----------+-----------+
|  X  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
Piece count:     [1, 0, 0, 0, 0, 0, 0, 0, 0]
Max Piece count: [1, 0, 0, 0, 0, 0, 0, 0, 0]
Max check:       [0, 0, 0, 0, 0, 0, 0, 0, 0][0]
Min check:       [0, 0, 0, 0, 0, 0, 0, 0, 0][0]
Last step:       [1, 1]
Depth/Status:    [1][MAX]
Game result:     [0, 0, 0, 0, 0, 0, 0, 0, 0]
Terminated:      [0, 0, 0, 0, 0, 0, 0, 0, 0]

#1> 2
+-----------+-----------+-----------+
|  X  O  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
Piece count:     [2, 0, 0, 0, 0, 0, 0, 0, 0]
Max Piece count: [1, 0, 0, 0, 0, 0, 0, 0, 0]
Max check:       [0, 0, 0, 0, 0, 0, 0, 0, 0][0]
Min check:       [0, 0, 0, 0, 0, 0, 0, 0, 0][0]
Last step:       [1, 2]
Depth/Status:    [2][MIN]
Game result:     [0, 0, 0, 0, 0, 0, 0, 0, 0]
Terminated:      [0, 0, 0, 0, 0, 0, 0, 0, 0]

Distinction:     [1000.0]
Confidence:      [40]
HeuristicPrunedMinimaxAgent used 10369787997 nano seconds
+-----------+-----------+-----------+
|  X  O  -  |  -  X  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
Piece count:     [2, 1, 0, 0, 0, 0, 0, 0, 0]
Max Piece count: [1, 1, 0, 0, 0, 0, 0, 0, 0]
Max check:       [0, 0, 0, 0, 0, 0, 0, 0, 0][0]
Min check:       [0, 0, 0, 0, 0, 0, 0, 0, 0][0]
Last step:       [2, 2]
Depth/Status:    [3][MAX]
Game result:     [0, 0, 0, 0, 0, 0, 0, 0, 0]
Terminated:      [0, 0, 0, 0, 0, 0, 0, 0, 0]

#2> 3
+-----------+-----------+-----------+
|  X  O  -  |  -  X  O  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
Piece count:     [2, 2, 0, 0, 0, 0, 0, 0, 0]
Max Piece count: [1, 1, 0, 0, 0, 0, 0, 0, 0]
Max check:       [0, 0, 0, 0, 0, 0, 0, 0, 0][0]
Min check:       [0, 0, 0, 0, 0, 0, 0, 0, 0][0]
Last step:       [2, 3]
Depth/Status:    [4][MIN]
Game result:     [0, 0, 0, 0, 0, 0, 0, 0, 0]
Terminated:      [0, 0, 0, 0, 0, 0, 0, 0, 0]

Distinction:     [1000.0]
Confidence:      [40]
HeuristicPrunedMinimaxAgent used 14899190525 nano seconds
+-----------+-----------+-----------+
|  X  O  -  |  -  X  O  |  -  -  X  |
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
Piece count:     [2, 2, 1, 0, 0, 0, 0, 0, 0]
Max Piece count: [1, 1, 1, 0, 0, 0, 0, 0, 0]
Max check:       [0, 0, 0, 0, 0, 0, 0, 0, 0][0]
Min check:       [0, 0, 0, 0, 0, 0, 0, 0, 0][0]
Last step:       [3, 3]
Depth/Status:    [5][MAX]
Game result:     [0, 0, 0, 0, 0, 0, 0, 0, 0]
Terminated:      [0, 0, 0, 0, 0, 0, 0, 0, 0]

#3> 4
+-----------+-----------+-----------+
|  X  O  -  |  -  X  O  |  -  -  X  |
|  -  -  -  |  -  -  -  |  O  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
Piece count:     [2, 2, 2, 0, 0, 0, 0, 0, 0]
Max Piece count: [1, 1, 1, 0, 0, 0, 0, 0, 0]
Max check:       [0, 0, 0, 0, 0, 0, 0, 0, 0][0]
Min check:       [0, 0, 0, 0, 0, 0, 0, 0, 0][0]
Last step:       [3, 4]
Depth/Status:    [6][MIN]
Game result:     [0, 0, 0, 0, 0, 0, 0, 0, 0]
Terminated:      [0, 0, 0, 0, 0, 0, 0, 0, 0]

Distinction:     [1000.0]
Confidence:      [40]
HeuristicPrunedMinimaxAgent used 16717531649 nano seconds
+-----------+-----------+-----------+
|  X  O  -  |  -  X  O  |  -  -  X  |
|  -  -  -  |  -  -  -  |  O  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  X  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
Piece count:     [2, 2, 2, 1, 0, 0, 0, 0, 0]
Max Piece count: [1, 1, 1, 1, 0, 0, 0, 0, 0]
Max check:       [0, 0, 0, 0, 0, 0, 0, 0, 0][0]
Min check:       [0, 0, 0, 0, 0, 0, 0, 0, 0][0]
Last step:       [4, 4]
Depth/Status:    [7][MAX]
Game result:     [0, 0, 0, 0, 0, 0, 0, 0, 0]
Terminated:      [0, 0, 0, 0, 0, 0, 0, 0, 0]

#4> 3
+-----------+-----------+-----------+
|  X  O  -  |  -  X  O  |  -  -  X  |
|  -  -  -  |  -  -  -  |  O  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  O  |  -  -  -  |  -  -  -  |
|  X  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
Piece count:     [2, 2, 2, 2, 0, 0, 0, 0, 0]
Max Piece count: [1, 1, 1, 1, 0, 0, 0, 0, 0]
Max check:       [0, 0, 0, 0, 0, 0, 0, 0, 0][0]
Min check:       [0, 0, 0, 0, 0, 0, 0, 0, 0][0]
Last step:       [4, 3]
Depth/Status:    [8][MIN]
Game result:     [0, 0, 0, 0, 0, 0, 0, 0, 0]
Terminated:      [0, 0, 0, 0, 0, 0, 0, 0, 0]

Distinction:     [1000.0]
Confidence:      [1040]
HeuristicPrunedMinimaxAgent used 13994881533 nano seconds
+-----------+-----------+-----------+
|  X  O  -  |  -  X  O  |  -  -  X  |
|  -  -  -  |  -  -  -  |  O  X  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  O  |  -  -  -  |  -  -  -  |
|  X  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
Piece count:     [2, 2, 3, 2, 0, 0, 0, 0, 0]
Max Piece count: [1, 1, 2, 1, 0, 0, 0, 0, 0]
Max check:       [0, 0, 1, 0, 0, 0, 0, 0, 0][1]
Min check:       [0, 0, 0, 0, 0, 0, 0, 0, 0][0]
Last step:       [3, 5]
Depth/Status:    [9][MAX]
Game result:     [0, 0, 0, 0, 0, 0, 0, 0, 0]
Terminated:      [0, 0, 0, 0, 0, 0, 0, 0, 0]

#5> 5
+-----------+-----------+-----------+
|  X  O  -  |  -  X  O  |  -  -  X  |
|  -  -  -  |  -  -  -  |  O  X  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  O  |  -  -  -  |  -  -  -  |
|  X  -  -  |  -  O  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
Piece count:     [2, 2, 3, 2, 1, 0, 0, 0, 0]
Max Piece count: [1, 1, 2, 1, 0, 0, 0, 0, 0]
Max check:       [0, 0, 1, 0, 0, 0, 0, 0, 0][1]
Min check:       [0, 0, 0, 0, 0, 0, 0, 0, 0][0]
Last step:       [5, 5]
Depth/Status:    [10][MIN]
Game result:     [0, 0, 0, 0, 0, 0, 0, 0, 0]
Terminated:      [0, 0, 0, 0, 0, 0, 0, 0, 0]

Distinction:     [1010.0]
Confidence:      [1040]
HeuristicPrunedMinimaxAgent used 19007478054 nano seconds
+-----------+-----------+-----------+
|  X  O  -  |  -  X  O  |  -  -  X  |
|  -  -  -  |  -  -  -  |  O  X  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  O  |  -  -  -  |  -  -  -  |
|  X  -  -  |  -  O  -  |  -  -  -  |
|  -  -  -  |  -  X  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
Piece count:     [2, 2, 3, 2, 2, 0, 0, 0, 0]
Max Piece count: [1, 1, 2, 1, 1, 0, 0, 0, 0]
Max check:       [0, 0, 1, 0, 0, 0, 0, 0, 0][1]
Min check:       [0, 0, 0, 0, 0, 0, 0, 0, 0][0]
Last step:       [5, 8]
Depth/Status:    [11][MAX]
Game result:     [0, 0, 0, 0, 0, 0, 0, 0, 0]
Terminated:      [0, 0, 0, 0, 0, 0, 0, 0, 0]

#8> 7
+-----------+-----------+-----------+
|  X  O  -  |  -  X  O  |  -  -  X  |
|  -  -  -  |  -  -  -  |  O  X  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  O  |  -  -  -  |  -  -  -  |
|  X  -  -  |  -  O  -  |  -  -  -  |
|  -  -  -  |  -  X  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  -  |  O  -  -  |  -  -  -  |
+-----------+-----------+-----------+
Piece count:     [2, 2, 3, 2, 2, 0, 0, 1, 0]
Max Piece count: [1, 1, 2, 1, 1, 0, 0, 0, 0]
Max check:       [0, 0, 1, 0, 0, 0, 0, 0, 0][1]
Min check:       [0, 0, 0, 0, 0, 0, 0, 0, 0][0]
Last step:       [8, 7]
Depth/Status:    [12][MIN]
Game result:     [0, 0, 0, 0, 0, 0, 0, 0, 0]
Terminated:      [0, 0, 0, 0, 0, 0, 0, 0, 0]

Distinction:     [1010.0]
Confidence:      [1040]
HeuristicPrunedMinimaxAgent used 13205897927 nano seconds
+-----------+-----------+-----------+
|  X  O  -  |  -  X  O  |  -  -  X  |
|  -  -  -  |  -  -  -  |  O  X  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  O  |  -  -  -  |  -  -  -  |
|  X  -  -  |  -  O  -  |  -  -  -  |
|  -  -  -  |  -  X  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  X  |  -  -  -  |  -  -  -  |
|  -  -  -  |  O  -  -  |  -  -  -  |
+-----------+-----------+-----------+
Piece count:     [2, 2, 3, 2, 2, 0, 1, 1, 0]
Max Piece count: [1, 1, 2, 1, 1, 0, 1, 0, 0]
Max check:       [0, 0, 1, 0, 0, 0, 0, 0, 0][1]
Min check:       [0, 0, 0, 0, 0, 0, 0, 0, 0][0]
Last step:       [7, 6]
Depth/Status:    [13][MAX]
Game result:     [0, 0, 0, 0, 0, 0, 0, 0, 0]
Terminated:      [0, 0, 0, 0, 0, 0, 0, 0, 0]

#6> 6
+-----------+-----------+-----------+
|  X  O  -  |  -  X  O  |  -  -  X  |
|  -  -  -  |  -  -  -  |  O  X  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  O  |  -  -  -  |  -  -  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  -  -  -  |  -  X  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  X  |  -  -  -  |  -  -  -  |
|  -  -  -  |  O  -  -  |  -  -  -  |
+-----------+-----------+-----------+
Piece count:     [2, 2, 3, 2, 2, 1, 1, 1, 0]
Max Piece count: [1, 1, 2, 1, 1, 0, 1, 0, 0]
Max check:       [0, 0, 1, 0, 0, 0, 0, 0, 0][1]
Min check:       [0, 0, 0, 0, 0, 0, 0, 0, 0][0]
Last step:       [6, 6]
Depth/Status:    [14][MIN]
Game result:     [0, 0, 0, 0, 0, 0, 0, 0, 0]
Terminated:      [0, 0, 0, 0, 0, 0, 0, 0, 0]

Distinction:     [1010.0]
Confidence:      [1040]
HeuristicPrunedMinimaxAgent used 10330705536 nano seconds
+-----------+-----------+-----------+
|  X  O  -  |  -  X  O  |  -  -  X  |
|  -  -  -  |  -  -  -  |  O  X  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  -  -  -  |  -  X  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  X  |  -  -  -  |  -  -  -  |
|  -  -  -  |  O  -  -  |  -  -  -  |
+-----------+-----------+-----------+
Piece count:     [2, 2, 3, 2, 2, 2, 1, 1, 0]
Max Piece count: [1, 1, 2, 1, 1, 1, 1, 0, 0]
Max check:       [0, 0, 1, 0, 0, 0, 0, 0, 0][1]
Min check:       [0, 0, 0, 0, 0, 0, 0, 0, 0][0]
Last step:       [6, 2]
Depth/Status:    [15][MAX]
Game result:     [0, 0, 0, 0, 0, 0, 0, 0, 0]
Terminated:      [0, 0, 0, 0, 0, 0, 0, 0, 0]

#2> 6
+-----------+-----------+-----------+
|  X  O  -  |  -  X  O  |  -  -  X  |
|  -  -  -  |  -  -  O  |  O  X  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  -  -  -  |  -  X  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  X  |  -  -  -  |  -  -  -  |
|  -  -  -  |  O  -  -  |  -  -  -  |
+-----------+-----------+-----------+
Piece count:     [2, 3, 3, 2, 2, 2, 1, 1, 0]
Max Piece count: [1, 1, 2, 1, 1, 1, 1, 0, 0]
Max check:       [0, 0, 1, 0, 0, 0, 0, 0, 0][1]
Min check:       [0, 1, 0, 0, 0, 0, 0, 0, 0][1]
Last step:       [2, 6]
Depth/Status:    [16][MIN]
Game result:     [0, 0, 0, 0, 0, 0, 0, 0, 0]
Terminated:      [0, 0, 0, 0, 0, 0, 0, 0, 0]

Distinction:     [1000.0]
Confidence:      [1040]
HeuristicPrunedMinimaxAgent used 15646260761 nano seconds
+-----------+-----------+-----------+
|  X  O  -  |  -  X  O  |  -  -  X  |
|  -  -  -  |  -  -  O  |  O  X  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  -  -  -  |  -  X  -  |  -  X  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  X  |  -  -  -  |  -  -  -  |
|  -  -  -  |  O  -  -  |  -  -  -  |
+-----------+-----------+-----------+
Piece count:     [2, 3, 3, 2, 2, 3, 1, 1, 0]
Max Piece count: [1, 1, 2, 1, 1, 2, 1, 0, 0]
Max check:       [0, 0, 1, 0, 0, 1, 0, 0, 0][2]
Min check:       [0, 1, 0, 0, 0, 0, 0, 0, 0][1]
Last step:       [6, 8]
Depth/Status:    [17][MAX]
Game result:     [0, 0, 0, 0, 0, 0, 0, 0, 0]
Terminated:      [0, 0, 0, 0, 0, 0, 0, 0, 0]

#8> 9
+-----------+-----------+-----------+
|  X  O  -  |  -  X  O  |  -  -  X  |
|  -  -  -  |  -  -  O  |  O  X  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  -  -  -  |  -  X  -  |  -  X  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  X  |  -  -  -  |  -  -  -  |
|  -  -  -  |  O  -  O  |  -  -  -  |
+-----------+-----------+-----------+
Piece count:     [2, 3, 3, 2, 2, 3, 1, 2, 0]
Max Piece count: [1, 1, 2, 1, 1, 2, 1, 0, 0]
Max check:       [0, 0, 1, 0, 0, 1, 0, 0, 0][2]
Min check:       [0, 1, 0, 0, 0, 0, 0, 1, 0][2]
Last step:       [8, 9]
Depth/Status:    [18][MIN]
Game result:     [0, 0, 0, 0, 0, 0, 0, 0, 0]
Terminated:      [0, 0, 0, 0, 0, 0, 0, 0, 0]

Distinction:     [5.002E7]
Confidence:      [2040]
HeuristicPrunedMinimaxAgent used 17087783452 nano seconds
+-----------+-----------+-----------+
|  X  O  -  |  -  X  O  |  -  -  X  |
|  -  -  -  |  -  -  O  |  O  X  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  -  -  -  |  -  X  -  |  -  X  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  X  |  -  -  -  |  -  -  -  |
|  -  -  -  |  O  -  O  |  -  X  -  |
+-----------+-----------+-----------+
Piece count:     [2, 3, 3, 2, 2, 3, 1, 2, 1]
Max Piece count: [1, 1, 2, 1, 1, 2, 1, 0, 1]
Max check:       [0, 0, 1, 0, 0, 1, 0, 0, 0][2]
Min check:       [0, 1, 0, 0, 0, 0, 0, 1, 0][2]
Last step:       [9, 8]
Depth/Status:    [19][MAX]
Game result:     [0, 0, 0, 0, 0, 0, 0, 0, 0]
Terminated:      [0, 0, 0, 0, 0, 0, 0, 0, 0]

#8> 8
+-----------+-----------+-----------+
|  X  O  -  |  -  X  O  |  -  -  X  |
|  -  -  -  |  -  -  O  |  O  X  -  |
|  -  -  -  |  -  -  -  |  -  -  -  |
+-----------+-----------+-----------+
|  -  -  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  -  -  -  |  -  X  -  |  -  X  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  X  |  -  -  -  |  -  -  -  |
|  -  -  -  |  O  O  O  |  -  X  -  |
+-----------+-----------+-----------+
Piece count:     [2, 3, 3, 2, 2, 3, 1, 3, 1]
Max Piece count: [1, 1, 2, 1, 1, 2, 1, 0, 1]
Max check:       [0, 0, 1, 0, 0, 1, 0, 0, 0][2]
Min check:       [0, 1, 0, 0, 0, 0, 0, 1, 0][2]
Last step:       [8, 8]
Depth/Status:    [20][MIN]
Game result:     [0, 0, 0, 0, 0, 0, 0, MIN, 0]
Terminated:      [0, 0, 0, 0, 0, 0, 0, 1, 0]

Distinction:     [1.0003199E8]
Confidence:      [50012030]
HeuristicPrunedMinimaxAgent used 24628657269 nano seconds
+-----------+-----------+-----------+
|  X  O  -  |  -  X  O  |  -  -  X  |
|  -  -  -  |  -  -  O  |  O  X  -  |
|  -  -  -  |  -  -  -  |  X  -  -  |
+-----------+-----------+-----------+
|  -  -  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  -  -  -  |  -  X  -  |  -  X  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  -  X  |  -  -  -  |  -  -  -  |
|  -  -  -  |  O  O  O  |  -  X  -  |
+-----------+-----------+-----------+
Piece count:     [2, 3, 4, 2, 2, 3, 1, 3, 1]
Max Piece count: [1, 1, 3, 1, 1, 2, 1, 0, 1]
Max check:       [0, 0, 1, 0, 0, 1, 0, 0, 0][2]
Min check:       [0, 1, 0, 0, 0, 0, 0, 1, 0][2]
Last step:       [3, 7]
Depth/Status:    [21][MAX]
Game result:     [0, 0, MAX, 0, 0, 0, 0, MIN, 0]
Terminated:      [0, 0, 1, 0, 0, 0, 0, 1, 0]

#7> 5
+-----------+-----------+-----------+
|  X  O  -  |  -  X  O  |  -  -  X  |
|  -  -  -  |  -  -  O  |  O  X  -  |
|  -  -  -  |  -  -  -  |  X  -  -  |
+-----------+-----------+-----------+
|  -  -  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  -  -  -  |  -  X  -  |  -  X  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  O  X  |  -  -  -  |  -  -  -  |
|  -  -  -  |  O  O  O  |  -  X  -  |
+-----------+-----------+-----------+
Piece count:     [2, 3, 4, 2, 2, 3, 2, 3, 1]
Max Piece count: [1, 1, 3, 1, 1, 2, 1, 0, 1]
Max check:       [0, 0, 1, 0, 0, 1, 0, 0, 0][2]
Min check:       [0, 1, 0, 0, 0, 0, 0, 1, 0][2]
Last step:       [7, 5]
Depth/Status:    [22][MIN]
Game result:     [0, 0, MAX, 0, 0, 0, 0, MIN, 0]
Terminated:      [0, 0, 1, 0, 0, 0, 0, 1, 0]

Distinction:     [2.197495688E9]
Confidence:      [50012040]
HeuristicPrunedMinimaxAgent used 504810917 nano seconds
+-----------+-----------+-----------+
|  X  O  -  |  -  X  O  |  -  -  X  |
|  -  -  -  |  -  -  O  |  O  X  -  |
|  -  -  -  |  -  -  -  |  X  -  -  |
+-----------+-----------+-----------+
|  -  -  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  -  -  -  |  -  X  X  |  -  X  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  O  X  |  -  -  -  |  -  -  -  |
|  -  -  -  |  O  O  O  |  -  X  -  |
+-----------+-----------+-----------+
Piece count:     [2, 3, 4, 2, 3, 3, 2, 3, 1]
Max Piece count: [1, 1, 3, 1, 2, 2, 1, 0, 1]
Max check:       [0, 0, 1, 0, 1, 1, 0, 0, 0][3]
Min check:       [0, 1, 0, 0, 0, 0, 0, 1, 0][2]
Last step:       [5, 9]
Depth/Status:    [23][MAX]
Game result:     [0, 0, MAX, 0, 0, 0, 0, MIN, 0]
Terminated:      [0, 0, 1, 0, 0, 0, 0, 1, 0]

#9> 9
+-----------+-----------+-----------+
|  X  O  -  |  -  X  O  |  -  -  X  |
|  -  -  -  |  -  -  O  |  O  X  -  |
|  -  -  -  |  -  -  -  |  X  -  -  |
+-----------+-----------+-----------+
|  -  -  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  -  -  -  |  -  X  X  |  -  X  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  -  -  |
|  -  O  X  |  -  -  -  |  -  -  -  |
|  -  -  -  |  O  O  O  |  -  X  O  |
+-----------+-----------+-----------+
Piece count:     [2, 3, 4, 2, 3, 3, 2, 3, 2]
Max Piece count: [1, 1, 3, 1, 2, 2, 1, 0, 1]
Max check:       [0, 0, 1, 0, 1, 1, 0, 0, 0][3]
Min check:       [0, 1, 0, 0, 0, 0, 0, 1, 0][2]
Last step:       [9, 9]
Depth/Status:    [24][MIN]
Game result:     [0, 0, MAX, 0, 0, 0, 0, MIN, 0]
Terminated:      [0, 0, 1, 0, 0, 0, 0, 1, 0]

Distinction:     [21000.0]
Confidence:      [50031030]
HeuristicPrunedMinimaxAgent used 354358398 nano seconds
+-----------+-----------+-----------+
|  X  O  -  |  -  X  O  |  -  -  X  |
|  -  -  -  |  -  -  O  |  O  X  -  |
|  -  -  -  |  -  -  -  |  X  -  -  |
+-----------+-----------+-----------+
|  -  -  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  -  -  -  |  -  X  X  |  -  X  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  X  -  |
|  -  O  X  |  -  -  -  |  -  -  -  |
|  -  -  -  |  O  O  O  |  -  X  O  |
+-----------+-----------+-----------+
Piece count:     [2, 3, 4, 2, 3, 3, 2, 3, 3]
Max Piece count: [1, 1, 3, 1, 2, 2, 1, 0, 2]
Max check:       [0, 0, 1, 0, 1, 1, 0, 0, 1][4]
Min check:       [0, 1, 0, 0, 0, 0, 0, 1, 0][2]
Last step:       [9, 2]
Depth/Status:    [25][MAX]
Game result:     [0, 0, MAX, 0, 0, 0, 0, MIN, 0]
Terminated:      [0, 0, 1, 0, 0, 0, 0, 1, 0]

#2> 4
+-----------+-----------+-----------+
|  X  O  -  |  -  X  O  |  -  -  X  |
|  -  -  -  |  O  -  O  |  O  X  -  |
|  -  -  -  |  -  -  -  |  X  -  -  |
+-----------+-----------+-----------+
|  -  -  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  -  -  -  |  -  X  X  |  -  X  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  X  -  |
|  -  O  X  |  -  -  -  |  -  -  -  |
|  -  -  -  |  O  O  O  |  -  X  O  |
+-----------+-----------+-----------+
Piece count:     [2, 4, 4, 2, 3, 3, 2, 3, 3]
Max Piece count: [1, 1, 3, 1, 2, 2, 1, 0, 2]
Max check:       [0, 0, 1, 0, 1, 1, 0, 0, 1][4]
Min check:       [0, 2, 0, 0, 0, 0, 0, 1, 0][2]
Last step:       [2, 4]
Depth/Status:    [26][MIN]
Game result:     [0, 0, MAX, 0, 0, 0, 0, MIN, 0]
Terminated:      [0, 0, 1, 0, 0, 0, 0, 1, 0]

Distinction:     [20000.0]
Confidence:      [50031040]
HeuristicPrunedMinimaxAgent used 224534153 nano seconds
+-----------+-----------+-----------+
|  X  O  -  |  -  X  O  |  -  -  X  |
|  -  -  -  |  O  -  O  |  O  X  -  |
|  -  -  -  |  -  -  -  |  X  -  -  |
+-----------+-----------+-----------+
|  X  -  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  -  -  -  |  -  X  X  |  -  X  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  X  -  |
|  -  O  X  |  -  -  -  |  -  -  -  |
|  -  -  -  |  O  O  O  |  -  X  O  |
+-----------+-----------+-----------+
Piece count:     [2, 4, 4, 3, 3, 3, 2, 3, 3]
Max Piece count: [1, 1, 3, 2, 2, 2, 1, 0, 2]
Max check:       [0, 0, 1, 1, 1, 1, 0, 0, 1][5]
Min check:       [0, 2, 0, 0, 0, 0, 0, 1, 0][2]
Last step:       [4, 1]
Depth/Status:    [27][MAX]
Game result:     [0, 0, MAX, 0, 0, 0, 0, MIN, 0]
Terminated:      [0, 0, 1, 0, 0, 0, 0, 1, 0]

#1> 7
+-----------+-----------+-----------+
|  X  O  -  |  -  X  O  |  -  -  X  |
|  -  -  -  |  O  -  O  |  O  X  -  |
|  O  -  -  |  -  -  -  |  X  -  -  |
+-----------+-----------+-----------+
|  X  -  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  -  -  -  |  -  X  X  |  -  X  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  X  -  |
|  -  O  X  |  -  -  -  |  -  -  -  |
|  -  -  -  |  O  O  O  |  -  X  O  |
+-----------+-----------+-----------+
Piece count:     [3, 4, 4, 3, 3, 3, 2, 3, 3]
Max Piece count: [1, 1, 3, 2, 2, 2, 1, 0, 2]
Max check:       [0, 0, 1, 1, 1, 1, 0, 0, 1][5]
Min check:       [0, 2, 0, 0, 0, 0, 0, 1, 0][2]
Last step:       [1, 7]
Depth/Status:    [28][MIN]
Game result:     [0, 0, MAX, 0, 0, 0, 0, MIN, 0]
Terminated:      [0, 0, 1, 0, 0, 0, 0, 1, 0]

Distinction:     [25000.0]
Confidence:      [50034040]
HeuristicPrunedMinimaxAgent used 121460310 nano seconds
+-----------+-----------+-----------+
|  X  O  -  |  -  X  O  |  -  -  X  |
|  -  -  -  |  O  -  O  |  O  X  -  |
|  O  -  -  |  -  -  -  |  X  -  -  |
+-----------+-----------+-----------+
|  X  -  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  -  -  -  |  -  X  X  |  -  X  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  -  X  -  |
|  -  O  X  |  -  -  -  |  -  -  -  |
|  -  -  X  |  O  O  O  |  -  X  O  |
+-----------+-----------+-----------+
Piece count:     [3, 4, 4, 3, 3, 3, 3, 3, 3]
Max Piece count: [1, 1, 3, 2, 2, 2, 2, 0, 2]
Max check:       [0, 0, 1, 1, 1, 1, 1, 0, 1][6]
Min check:       [0, 2, 0, 0, 0, 0, 0, 1, 0][2]
Last step:       [7, 9]
Depth/Status:    [29][MAX]
Game result:     [0, 0, MAX, 0, 0, 0, 0, MIN, 0]
Terminated:      [0, 0, 1, 0, 0, 0, 0, 1, 0]

#9> 1
+-----------+-----------+-----------+
|  X  O  -  |  -  X  O  |  -  -  X  |
|  -  -  -  |  O  -  O  |  O  X  -  |
|  O  -  -  |  -  -  -  |  X  -  -  |
+-----------+-----------+-----------+
|  X  -  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  -  -  -  |  -  X  X  |  -  X  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  O  X  -  |
|  -  O  X  |  -  -  -  |  -  -  -  |
|  -  -  X  |  O  O  O  |  -  X  O  |
+-----------+-----------+-----------+
Piece count:     [3, 4, 4, 3, 3, 3, 3, 3, 4]
Max Piece count: [1, 1, 3, 2, 2, 2, 2, 0, 2]
Max check:       [0, 0, 1, 1, 1, 1, 1, 0, 1][6]
Min check:       [0, 2, 0, 0, 0, 0, 0, 1, 1][3]
Last step:       [9, 1]
Depth/Status:    [30][MIN]
Game result:     [0, 0, MAX, 0, 0, 0, 0, MIN, 0]
Terminated:      [0, 0, 1, 0, 0, 0, 0, 1, 0]

Distinction:     [25990.0]
Confidence:      [50038030]
HeuristicPrunedMinimaxAgent used 59177827 nano seconds
+-----------+-----------+-----------+
|  X  O  -  |  -  X  O  |  -  -  X  |
|  X  -  -  |  O  -  O  |  O  X  -  |
|  O  -  -  |  -  -  -  |  X  -  -  |
+-----------+-----------+-----------+
|  X  -  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  -  -  -  |  -  X  X  |  -  X  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  O  X  -  |
|  -  O  X  |  -  -  -  |  -  -  -  |
|  -  -  X  |  O  O  O  |  -  X  O  |
+-----------+-----------+-----------+
Piece count:     [4, 4, 4, 3, 3, 3, 3, 3, 4]
Max Piece count: [2, 1, 3, 2, 2, 2, 2, 0, 2]
Max check:       [0, 0, 1, 1, 1, 1, 1, 0, 1][6]
Min check:       [0, 2, 0, 0, 0, 0, 0, 1, 1][3]
Last step:       [1, 4]
Depth/Status:    [31][MAX]
Game result:     [0, 0, MAX, 0, 0, 0, 0, MIN, 0]
Terminated:      [0, 0, 1, 0, 0, 0, 0, 1, 0]

#4> 2
+-----------+-----------+-----------+
|  X  O  -  |  -  X  O  |  -  -  X  |
|  X  -  -  |  O  -  O  |  O  X  -  |
|  O  -  -  |  -  -  -  |  X  -  -  |
+-----------+-----------+-----------+
|  X  O  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  -  -  -  |  -  X  X  |  -  X  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  O  X  -  |
|  -  O  X  |  -  -  -  |  -  -  -  |
|  -  -  X  |  O  O  O  |  -  X  O  |
+-----------+-----------+-----------+
Piece count:     [4, 4, 4, 4, 3, 3, 3, 3, 4]
Max Piece count: [2, 1, 3, 2, 2, 2, 2, 0, 2]
Max check:       [0, 0, 1, 1, 1, 1, 1, 0, 1][6]
Min check:       [0, 2, 0, 0, 0, 0, 0, 1, 1][3]
Last step:       [4, 2]
Depth/Status:    [32][MIN]
Game result:     [0, 0, MAX, 0, 0, 0, 0, MIN, 0]
Terminated:      [0, 0, 1, 0, 0, 0, 0, 1, 0]

Distinction:     [49990.0]
Confidence:      [50051020]
HeuristicPrunedMinimaxAgent used 27598651 nano seconds
+-----------+-----------+-----------+
|  X  O  -  |  X  X  O  |  -  -  X  |
|  X  -  -  |  O  -  O  |  O  X  -  |
|  O  -  -  |  -  -  -  |  X  -  -  |
+-----------+-----------+-----------+
|  X  O  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  -  -  -  |  -  X  X  |  -  X  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  O  X  -  |
|  -  O  X  |  -  -  -  |  -  -  -  |
|  -  -  X  |  O  O  O  |  -  X  O  |
+-----------+-----------+-----------+
Piece count:     [4, 5, 4, 4, 3, 3, 3, 3, 4]
Max Piece count: [2, 2, 3, 2, 2, 2, 2, 0, 2]
Max check:       [0, 0, 1, 1, 1, 1, 1, 0, 1][6]
Min check:       [0, 2, 0, 0, 0, 0, 0, 1, 1][3]
Last step:       [2, 1]
Depth/Status:    [33][MAX]
Game result:     [0, 0, MAX, 0, 0, 0, 0, MIN, 0]
Terminated:      [0, 0, 1, 0, 0, 0, 0, 1, 0]

#1> 8
+-----------+-----------+-----------+
|  X  O  -  |  X  X  O  |  -  -  X  |
|  X  -  -  |  O  -  O  |  O  X  -  |
|  O  O  -  |  -  -  -  |  X  -  -  |
+-----------+-----------+-----------+
|  X  O  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  -  -  -  |  -  X  X  |  -  X  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  O  X  -  |
|  -  O  X  |  -  -  -  |  -  -  -  |
|  -  -  X  |  O  O  O  |  -  X  O  |
+-----------+-----------+-----------+
Piece count:     [5, 5, 4, 4, 3, 3, 3, 3, 4]
Max Piece count: [2, 2, 3, 2, 2, 2, 2, 0, 2]
Max check:       [0, 0, 1, 1, 1, 1, 1, 0, 1][6]
Min check:       [2, 2, 0, 0, 0, 0, 0, 1, 1][4]
Last step:       [1, 8]
Depth/Status:    [34][MIN]
Game result:     [0, 0, MAX, 0, 0, 0, 0, MIN, 0]
Terminated:      [0, 0, 1, 0, 0, 0, 0, 1, 0]

Distinction:     [2.197542678E9]
Confidence:      [50059030]
HeuristicPrunedMinimaxAgent used 893175823 nano seconds
+-----------+-----------+-----------+
|  X  O  -  |  X  X  O  |  -  -  X  |
|  X  -  -  |  O  -  O  |  O  X  -  |
|  O  O  -  |  -  -  -  |  X  -  -  |
+-----------+-----------+-----------+
|  X  O  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  X  -  -  |  -  X  X  |  -  X  -  |
+-----------+-----------+-----------+
|  -  -  -  |  -  -  -  |  O  X  -  |
|  -  O  X  |  -  -  -  |  -  -  -  |
|  -  -  X  |  O  O  O  |  -  X  O  |
+-----------+-----------+-----------+
Piece count:     [5, 5, 4, 5, 3, 3, 3, 3, 4]
Max Piece count: [2, 2, 3, 3, 2, 2, 2, 0, 2]
Max check:       [0, 0, 1, 1, 1, 1, 1, 0, 1][6]
Min check:       [2, 2, 0, 0, 0, 0, 0, 1, 1][4]
Last step:       [4, 7]
Depth/Status:    [35][MAX]
Game result:     [0, 0, MAX, MAX, 0, 0, 0, MIN, 0]
Terminated:      [0, 0, 1, 1, 0, 0, 0, 1, 0]

#7> 1
+-----------+-----------+-----------+
|  X  O  -  |  X  X  O  |  -  -  X  |
|  X  -  -  |  O  -  O  |  O  X  -  |
|  O  O  -  |  -  -  -  |  X  -  -  |
+-----------+-----------+-----------+
|  X  O  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  X  -  -  |  -  X  X  |  -  X  -  |
+-----------+-----------+-----------+
|  O  -  -  |  -  -  -  |  O  X  -  |
|  -  O  X  |  -  -  -  |  -  -  -  |
|  -  -  X  |  O  O  O  |  -  X  O  |
+-----------+-----------+-----------+
Piece count:     [5, 5, 4, 5, 3, 3, 4, 3, 4]
Max Piece count: [2, 2, 3, 3, 2, 2, 2, 0, 2]
Max check:       [0, 0, 1, 1, 1, 1, 1, 0, 1][6]
Min check:       [2, 2, 0, 0, 0, 0, 0, 1, 1][4]
Last step:       [7, 1]
Depth/Status:    [36][MIN]
Game result:     [0, 0, MAX, MAX, 0, 0, 0, MIN, 0]
Terminated:      [0, 0, 1, 1, 0, 0, 0, 1, 0]

Distinction:     [2.197573688E9]
Confidence:      [50090040]
HeuristicPrunedMinimaxAgent used 849862 nano seconds
+-----------+-----------+-----------+
|  X  O  -  |  X  X  O  |  -  -  X  |
|  X  X  -  |  O  -  O  |  O  X  -  |
|  O  O  -  |  -  -  -  |  X  -  -  |
+-----------+-----------+-----------+
|  X  O  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  X  -  -  |  -  X  X  |  -  X  -  |
+-----------+-----------+-----------+
|  O  -  -  |  -  -  -  |  O  X  -  |
|  -  O  X  |  -  -  -  |  -  -  -  |
|  -  -  X  |  O  O  O  |  -  X  O  |
+-----------+-----------+-----------+
Piece count:     [6, 5, 4, 5, 3, 3, 4, 3, 4]
Max Piece count: [3, 2, 3, 3, 2, 2, 2, 0, 2]
Max check:       [2, 0, 1, 1, 1, 1, 1, 0, 1][7]
Min check:       [1, 2, 0, 0, 0, 0, 0, 1, 1][4]
Last step:       [1, 5]
Depth/Status:    [37][MAX]
Game result:     [0, 0, MAX, MAX, 0, 0, 0, MIN, 0]
Terminated:      [0, 0, 1, 1, 0, 0, 0, 1, 0]

#5> 7
+-----------+-----------+-----------+
|  X  O  -  |  X  X  O  |  -  -  X  |
|  X  X  -  |  O  -  O  |  O  X  -  |
|  O  O  -  |  -  -  -  |  X  -  -  |
+-----------+-----------+-----------+
|  X  O  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  X  -  -  |  O  X  X  |  -  X  -  |
+-----------+-----------+-----------+
|  O  -  -  |  -  -  -  |  O  X  -  |
|  -  O  X  |  -  -  -  |  -  -  -  |
|  -  -  X  |  O  O  O  |  -  X  O  |
+-----------+-----------+-----------+
Piece count:     [6, 5, 4, 5, 4, 3, 4, 3, 4]
Max Piece count: [3, 2, 3, 3, 2, 2, 2, 0, 2]
Max check:       [2, 0, 1, 1, 0, 1, 1, 0, 1][6]
Min check:       [1, 2, 0, 0, 1, 0, 0, 1, 1][5]
Last step:       [5, 7]
Depth/Status:    [38][MIN]
Game result:     [0, 0, MAX, MAX, 0, 0, 0, MIN, 0]
Terminated:      [0, 0, 1, 1, 0, 0, 0, 1, 0]

Distinction:     [1.6474836470000002E9]
Confidence:      [2147483647]
HeuristicPrunedMinimaxAgent used 207597 nano seconds
+-----------+-----------+-----------+
|  X  O  -  |  X  X  O  |  -  -  X  |
|  X  X  -  |  O  -  O  |  O  X  -  |
|  O  O  -  |  -  -  -  |  X  -  -  |
+-----------+-----------+-----------+
|  X  O  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  X  -  -  |  O  X  X  |  -  X  -  |
+-----------+-----------+-----------+
|  O  -  -  |  -  -  -  |  O  X  -  |
|  -  O  X  |  -  -  -  |  -  -  -  |
|  X  -  X  |  O  O  O  |  -  X  O  |
+-----------+-----------+-----------+
Piece count:     [6, 5, 4, 5, 4, 3, 5, 3, 4]
Max Piece count: [3, 2, 3, 3, 2, 2, 3, 0, 2]
Max check:       [2, 0, 1, 1, 0, 1, 2, 0, 1][6]
Min check:       [1, 2, 0, 0, 1, 0, 0, 1, 1][5]
Last step:       [7, 7]
Depth/Status:    [39][MAX]
Game result:     [0, 0, MAX, MAX, 0, 0, 0, MIN, 0]
Terminated:      [0, 0, 1, 1, 0, 0, 0, 1, 0]

#7> 2
+-----------+-----------+-----------+
|  X  O  -  |  X  X  O  |  -  -  X  |
|  X  X  -  |  O  -  O  |  O  X  -  |
|  O  O  -  |  -  -  -  |  X  -  -  |
+-----------+-----------+-----------+
|  X  O  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  X  -  -  |  O  X  X  |  -  X  -  |
+-----------+-----------+-----------+
|  O  O  -  |  -  -  -  |  O  X  -  |
|  -  O  X  |  -  -  -  |  -  -  -  |
|  X  -  X  |  O  O  O  |  -  X  O  |
+-----------+-----------+-----------+
Piece count:     [6, 5, 4, 5, 4, 3, 6, 3, 4]
Max Piece count: [3, 2, 3, 3, 2, 2, 3, 0, 2]
Max check:       [2, 0, 1, 1, 0, 1, 2, 0, 1][6]
Min check:       [1, 2, 0, 0, 1, 0, 2, 1, 1][6]
Last step:       [7, 2]
Depth/Status:    [40][MIN]
Game result:     [0, 0, MAX, MAX, 0, 0, 0, MIN, 0]
Terminated:      [0, 0, 1, 1, 0, 0, 0, 1, 0]

Distinction:     [4.294967295E9]
Confidence:      [2147483647]
HeuristicPrunedMinimaxAgent used 152927 nano seconds
+-----------+-----------+-----------+
|  X  O  -  |  X  X  O  |  -  -  X  |
|  X  X  -  |  O  -  O  |  O  X  -  |
|  O  O  -  |  X  -  -  |  X  -  -  |
+-----------+-----------+-----------+
|  X  O  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  X  -  -  |  O  X  X  |  -  X  -  |
+-----------+-----------+-----------+
|  O  O  -  |  -  -  -  |  O  X  -  |
|  -  O  X  |  -  -  -  |  -  -  -  |
|  X  -  X  |  O  O  O  |  -  X  O  |
+-----------+-----------+-----------+
Piece count:     [6, 6, 4, 5, 4, 3, 6, 3, 4]
Max Piece count: [3, 3, 3, 3, 2, 2, 3, 0, 2]
Max check:       [2, 0, 1, 1, 0, 1, 2, 0, 1][6]
Min check:       [1, 2, 0, 0, 1, 0, 2, 1, 1][6]
Last step:       [2, 7]
Depth/Status:    [41][MAX]
Game result:     [0, 0, MAX, MAX, 0, 0, 0, MIN, 0]
Terminated:      [0, 0, 1, 1, 0, 0, 0, 1, 0]

#7> 3
+-----------+-----------+-----------+
|  X  O  -  |  X  X  O  |  -  -  X  |
|  X  X  -  |  O  -  O  |  O  X  -  |
|  O  O  -  |  X  -  -  |  X  -  -  |
+-----------+-----------+-----------+
|  X  O  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  X  -  -  |  O  X  X  |  -  X  -  |
+-----------+-----------+-----------+
|  O  O  O  |  -  -  -  |  O  X  -  |
|  -  O  X  |  -  -  -  |  -  -  -  |
|  X  -  X  |  O  O  O  |  -  X  O  |
+-----------+-----------+-----------+
Piece count:     [6, 6, 4, 5, 4, 3, 7, 3, 4]
Max Piece count: [3, 3, 3, 3, 2, 2, 3, 0, 2]
Max check:       [2, 0, 1, 1, 0, 1, 2, 0, 1][6]
Min check:       [1, 2, 0, 0, 1, 0, 2, 1, 1][6]
Last step:       [7, 3]
Depth/Status:    [42][MIN]
Game result:     [0, 0, MAX, MAX, 0, 0, MIN, MIN, 0]
Terminated:      [0, 0, 1, 1, 0, 0, 1, 1, 0]

Distinction:     [2.0974826070000002E9]
Confidence:      [2147483647]
HeuristicPrunedMinimaxAgent used 8960124 nano seconds
+-----------+-----------+-----------+
|  X  O  -  |  X  X  O  |  -  -  X  |
|  X  X  X  |  O  -  O  |  O  X  -  |
|  O  O  -  |  X  -  -  |  X  -  -  |
+-----------+-----------+-----------+
|  X  O  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  X  -  -  |  O  X  X  |  -  X  -  |
+-----------+-----------+-----------+
|  O  O  O  |  -  -  -  |  O  X  -  |
|  -  O  X  |  -  -  -  |  -  -  -  |
|  X  -  X  |  O  O  O  |  -  X  O  |
+-----------+-----------+-----------+
Piece count:     [7, 6, 4, 5, 4, 3, 7, 3, 4]
Max Piece count: [4, 3, 3, 3, 2, 2, 3, 0, 2]
Max check:       [2, 0, 1, 1, 0, 1, 2, 0, 1][6]
Min check:       [1, 2, 0, 0, 1, 0, 2, 1, 1][6]
Last step:       [1, 6]
Depth/Status:    [43][MAX]
Game result:     [MAX, 0, MAX, MAX, 0, 0, MIN, MIN, 0]
Terminated:      [1, 0, 1, 1, 0, 0, 1, 1, 0]

#6> 9
+-----------+-----------+-----------+
|  X  O  -  |  X  X  O  |  -  -  X  |
|  X  X  X  |  O  -  O  |  O  X  -  |
|  O  O  -  |  X  -  -  |  X  -  -  |
+-----------+-----------+-----------+
|  X  O  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  X  -  -  |  O  X  X  |  -  X  O  |
+-----------+-----------+-----------+
|  O  O  O  |  -  -  -  |  O  X  -  |
|  -  O  X  |  -  -  -  |  -  -  -  |
|  X  -  X  |  O  O  O  |  -  X  O  |
+-----------+-----------+-----------+
Piece count:     [7, 6, 4, 5, 4, 4, 7, 3, 4]
Max Piece count: [4, 3, 3, 3, 2, 2, 3, 0, 2]
Max check:       [2, 0, 1, 1, 0, 1, 2, 0, 1][6]
Min check:       [1, 2, 0, 0, 1, 1, 2, 1, 1][7]
Last step:       [6, 9]
Depth/Status:    [44][MIN]
Game result:     [MAX, 0, MAX, MAX, 0, 0, MIN, MIN, 0]
Terminated:      [1, 0, 1, 1, 0, 0, 1, 1, 0]

Distinction:     [1.6474836470000002E9]
Confidence:      [2147483647]
HeuristicPrunedMinimaxAgent used 137868 nano seconds
+-----------+-----------+-----------+
|  X  O  -  |  X  X  O  |  -  -  X  |
|  X  X  X  |  O  -  O  |  O  X  -  |
|  O  O  -  |  X  -  -  |  X  -  -  |
+-----------+-----------+-----------+
|  X  O  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  X  -  -  |  O  X  X  |  -  X  O  |
+-----------+-----------+-----------+
|  O  O  O  |  -  -  -  |  O  X  -  |
|  -  O  X  |  -  -  -  |  -  -  X  |
|  X  -  X  |  O  O  O  |  -  X  O  |
+-----------+-----------+-----------+
Piece count:     [7, 6, 4, 5, 4, 4, 7, 3, 5]
Max Piece count: [4, 3, 3, 3, 2, 2, 3, 0, 3]
Max check:       [2, 0, 1, 1, 0, 1, 2, 0, 1][6]
Min check:       [1, 2, 0, 0, 1, 1, 2, 1, 1][7]
Last step:       [9, 6]
Depth/Status:    [45][MAX]
Game result:     [MAX, 0, MAX, MAX, 0, 0, MIN, MIN, 0]
Terminated:      [1, 0, 1, 1, 0, 0, 1, 1, 0]

#6> 7
+-----------+-----------+-----------+
|  X  O  -  |  X  X  O  |  -  -  X  |
|  X  X  X  |  O  -  O  |  O  X  -  |
|  O  O  -  |  X  -  -  |  X  -  -  |
+-----------+-----------+-----------+
|  X  O  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  -  O  |
|  X  -  -  |  O  X  X  |  O  X  O  |
+-----------+-----------+-----------+
|  O  O  O  |  -  -  -  |  O  X  -  |
|  -  O  X  |  -  -  -  |  -  -  X  |
|  X  -  X  |  O  O  O  |  -  X  O  |
+-----------+-----------+-----------+
Piece count:     [7, 6, 4, 5, 4, 5, 7, 3, 5]
Max Piece count: [4, 3, 3, 3, 2, 2, 3, 0, 3]
Max check:       [2, 0, 1, 1, 0, 1, 2, 0, 1][6]
Min check:       [1, 2, 0, 0, 1, 1, 2, 1, 1][7]
Last step:       [6, 7]
Depth/Status:    [46][MIN]
Game result:     [MAX, 0, MAX, MAX, 0, 0, MIN, MIN, 0]
Terminated:      [1, 0, 1, 1, 0, 0, 1, 1, 0]

Distinction:     [4.294967295E9]
Confidence:      [2147483647]
HeuristicPrunedMinimaxAgent used 356957 nano seconds
+-----------+-----------+-----------+
|  X  O  -  |  X  X  O  |  -  -  X  |
|  X  X  X  |  O  -  O  |  O  X  -  |
|  O  O  -  |  X  -  -  |  X  -  -  |
+-----------+-----------+-----------+
|  X  O  O  |  -  -  -  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  X  O  |
|  X  -  -  |  O  X  X  |  O  X  O  |
+-----------+-----------+-----------+
|  O  O  O  |  -  -  -  |  O  X  -  |
|  -  O  X  |  -  -  -  |  -  -  X  |
|  X  -  X  |  O  O  O  |  -  X  O  |
+-----------+-----------+-----------+
Piece count:     [7, 6, 4, 5, 4, 6, 7, 3, 5]
Max Piece count: [4, 3, 3, 3, 2, 3, 3, 0, 3]
Max check:       [2, 0, 1, 1, 0, 1, 2, 0, 1][6]
Min check:       [1, 2, 0, 0, 1, 1, 2, 1, 1][7]
Last step:       [6, 5]
Depth/Status:    [47][MAX]
Game result:     [MAX, 0, MAX, MAX, 0, MAX, MIN, MIN, 0]
Terminated:      [1, 0, 1, 1, 0, 1, 1, 1, 0]

#5> 3
+-----------+-----------+-----------+
|  X  O  -  |  X  X  O  |  -  -  X  |
|  X  X  X  |  O  -  O  |  O  X  -  |
|  O  O  -  |  X  -  -  |  X  -  -  |
+-----------+-----------+-----------+
|  X  O  O  |  -  -  O  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  X  O  |
|  X  -  -  |  O  X  X  |  O  X  O  |
+-----------+-----------+-----------+
|  O  O  O  |  -  -  -  |  O  X  -  |
|  -  O  X  |  -  -  -  |  -  -  X  |
|  X  -  X  |  O  O  O  |  -  X  O  |
+-----------+-----------+-----------+
Piece count:     [7, 6, 4, 5, 5, 6, 7, 3, 5]
Max Piece count: [4, 3, 3, 3, 2, 3, 3, 0, 3]
Max check:       [2, 0, 1, 1, 0, 1, 2, 0, 1][6]
Min check:       [1, 2, 0, 0, 1, 1, 2, 1, 1][7]
Last step:       [5, 3]
Depth/Status:    [48][MIN]
Game result:     [MAX, 0, MAX, MAX, MIN, MAX, MIN, MIN, 0]
Terminated:      [1, 0, 1, 1, 1, 1, 1, 1, 0]

Distinction:     [1.6474836470000002E9]
Confidence:      [2147483647]
HeuristicPrunedMinimaxAgent used 177910 nano seconds
+-----------+-----------+-----------+
|  X  O  -  |  X  X  O  |  -  -  X  |
|  X  X  X  |  O  -  O  |  O  X  -  |
|  O  O  -  |  X  -  X  |  X  -  -  |
+-----------+-----------+-----------+
|  X  O  O  |  -  -  O  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  X  O  |
|  X  -  -  |  O  X  X  |  O  X  O  |
+-----------+-----------+-----------+
|  O  O  O  |  -  -  -  |  O  X  -  |
|  -  O  X  |  -  -  -  |  -  -  X  |
|  X  -  X  |  O  O  O  |  -  X  O  |
+-----------+-----------+-----------+
Piece count:     [7, 7, 4, 5, 5, 6, 7, 3, 5]
Max Piece count: [4, 4, 3, 3, 2, 3, 3, 0, 3]
Max check:       [2, 2, 1, 1, 0, 1, 2, 0, 1][7]
Min check:       [1, 1, 0, 0, 1, 1, 2, 1, 1][7]
Last step:       [2, 9]
Depth/Status:    [49][MAX]
Game result:     [MAX, 0, MAX, MAX, MIN, MAX, MIN, MIN, 0]
Terminated:      [1, 0, 1, 1, 1, 1, 1, 1, 0]

#9> 5
+-----------+-----------+-----------+
|  X  O  -  |  X  X  O  |  -  -  X  |
|  X  X  X  |  O  -  O  |  O  X  -  |
|  O  O  -  |  X  -  X  |  X  -  -  |
+-----------+-----------+-----------+
|  X  O  O  |  -  -  O  |  -  X  -  |
|  X  -  -  |  -  O  -  |  -  X  O  |
|  X  -  -  |  O  X  X  |  O  X  O  |
+-----------+-----------+-----------+
|  O  O  O  |  -  -  -  |  O  X  -  |
|  -  O  X  |  -  -  -  |  -  O  X  |
|  X  -  X  |  O  O  O  |  -  X  O  |
+-----------+-----------+-----------+
Piece count:     [7, 7, 4, 5, 5, 6, 7, 3, 6]
Max Piece count: [4, 4, 3, 3, 2, 3, 3, 0, 3]
Max check:       [2, 2, 1, 1, 0, 1, 2, 0, 0][6]
Min check:       [1, 1, 0, 0, 1, 1, 2, 1, 1][7]

Last step:       [9, 5]
Depth/Status:    [50][TERMINAL]
game over, you win!
Game result:     [MAX, 0, MAX, MAX, MIN, MAX, MIN, MIN, MIN]
restart a game? (y/n) Terminated:      [1, 0, 1, 1, 1, 1, 1, 1, 1]

n
bye!

Process finished with exit code 0



 */