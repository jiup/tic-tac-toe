package ttt;

import ttt.actuator.BasicMiniMaxActuator;
import ttt.domain.State;

public class Main {
    private static State getInitialState() {
        return State.create();
    }

    public static void main(String[] args) {
        State s = getInitialState();
        System.out.println(s.takeStep(0,0));
        System.out.println("!!!!!!!!!!");
        System.out.println(s = new BasicMiniMaxActuator().forward(s));
        System.out.println(s.takeStep(2,1));
        System.out.println("!!!!!!!!!!");
        System.out.println(s = new BasicMiniMaxActuator().forward(s));
//        System.out.println(s.takeStep(1,2));
        System.out.println(s.getCost());
        System.out.println(s.isTerminal());
//        while (!s.isTerminal()) {
//
//        }
    }
}
