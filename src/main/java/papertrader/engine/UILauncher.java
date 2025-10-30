package papertrader.engine;

import papertrader.UI.Window;
import javafx.application.Application;
import papertrader.player.Player;

public class UILauncher {


    public static void main(String[] args) {
        MarketSystem.get().loadDefaultData();
        Player.get().loadDefaultData();

        Application.launch(Window.class, args);
    }
}
