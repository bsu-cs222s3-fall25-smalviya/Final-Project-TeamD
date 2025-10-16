package papertrader.UI;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StateMachine {

    private UIState currentState = new Stocks();
    private final List<Consumer<UIState>> listeners = new ArrayList<>();

    public UIState getState() {
        return currentState;
    }

    public void changeState(UIState newState) {
        if (!currentState.getClass().equals(newState.getClass())) {
            currentState = newState;
            notifyListeners();
        }
    }

    public void addListener(Consumer<UIState> listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (Consumer<UIState> listener : listeners) {
            listener.accept(currentState);
        }
    }
}
