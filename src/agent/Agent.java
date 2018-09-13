package agent;

import domain.State;

public interface Agent {
    State forward(State state);
}
