package ttt.actuator;

import ttt.domain.State;

public interface Actuator {
    State forward(State state);
}
