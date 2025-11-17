package papertrader.gui;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import papertrader.core.Player;

public class Display extends HBox implements IRefreshable {

    private final Window window;

    public Display(Window window) {
        super(30);
        this.window = window;
        this.setAlignment(Pos.CENTER);
    }

    @Override
    public void refresh(ActionEvent event) {
        this.getChildren().clear();

        KeyValueLabel moneyLabel = new KeyValueLabel("Cash: ", "$%.2f");
        moneyLabel.addChildrenStyle("small");
        moneyLabel.setValue(Player.get().portfolio.getMoney());
        moneyLabel.setValueColor(Color.GREEN);

        KeyValueLabel totalLabel = new KeyValueLabel("Net Worth: ", "$%.2f");
        totalLabel.addChildrenStyle("small");
        totalLabel.setValue(Player.get().portfolio.getTotalMoney());
        totalLabel.setValueColor(Color.GREEN);

        this.getChildren().addAll(moneyLabel, totalLabel);
    }
}
