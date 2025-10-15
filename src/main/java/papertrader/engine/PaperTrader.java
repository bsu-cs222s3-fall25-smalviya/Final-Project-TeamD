package papertrader.engine;
import papertrader.player.Player;
import papertrader.player.Player.Portfolio;
import papertrader.market.PlayerStock;
import papertrader.engine.MarketSystem;

import java.util.Objects;
import java.util.Scanner;

public class PaperTrader {
    private static final boolean GUI_LOADER = false;

    public static void main(String[] args) throws InterruptedException {
        Player player = Player.loadData();
        OutputStream outputStream = new OutputStream();
        System.out.println(player.portfolio.getMoney());
        System.out.println(MarketSystem.get().stockList);
        player.buyStock("NVDA", 15);
        player.buyStock("NVDA", 25);
        outputStream.outputStockList(player);
        System.out.println(player.portfolio.getMoney());


        if (!GUI_LOADER) {
            outputStream.CLI(player);
        } else {
            // UI eventually
        }




    }
}
