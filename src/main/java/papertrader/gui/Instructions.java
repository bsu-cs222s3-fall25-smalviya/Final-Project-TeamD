package papertrader.gui;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import papertrader.core.MarketSystem;
import papertrader.core.Player;

public class Instructions extends BorderPane implements IRefreshable {

    private final WindowGUI window;

    public Instructions(WindowGUI window) {
        this.window = window;
        refresh(null);

        Label label = new Label("""
                The Game is simple in concept: Make money in the stock market.
                How well you do is determined by your business savvy and smarts.
                
                The Stock Menu is where you manage stocks.  You can buy one by entering a number in the text box, and clicking 'Buy.'
                The same goes for selling, shorting, and covering.  Shorting a stock is when you make money when it goes down, and covering is like selling a short.
                
                The History Menu is pretty self-explanatory, here you view your purchase history and evaluate what you have done.
                
                The Simulate button simulates a day passing in the game.  It opens a menu that shows the results of the day.
                You can use this menu to determine which stocks have increased in value, and which have not.
                
                Good Luck!
                """);
        label.setWrapText(true);

        VBox vBox = new VBox(label);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setPadding(new Insets(10.0, 0.0, 10.0, 0.0));

        this.setCenter(vBox);
    }

    @Override
    public void refresh(ActionEvent event) {}
}
