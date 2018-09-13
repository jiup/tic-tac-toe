package actuator;

import domain.State;

public interface Actuator {
    State forward(State state);
}
