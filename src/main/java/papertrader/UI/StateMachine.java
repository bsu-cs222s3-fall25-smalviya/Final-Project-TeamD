package papertrader.UI;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StateMachine {

    private String State = "Stocks";
    private final List<Consumer<String>> listeners = new ArrayList<>();

    public String GetState() {
        return State;
    }

    public void ChangeState(String state) {
        if (!State.equals(state)) {
            State = state;
            notifyListeners();
        }
    }

    public void addListener(Consumer<String> listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (Consumer<String> listener : listeners) {
            listener.accept(State);
        }
    }
}
