package papertrader.UI;

import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StateMachine {

    private Pane currentState = new Stocks();
    private final List<Consumer<Pane>> listeners = new ArrayList<>();

    public Pane getState() {
        return currentState;
    }

    public void changeState(Pane newState) {
        if (!currentState.getClass().equals(newState.getClass())) {
            currentState = newState;
            notifyListeners();
        }
    }

    public void addListener(Consumer<Pane> listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (Consumer<Pane> listener : listeners) {
            listener.accept(currentState);
        }
    }
}
