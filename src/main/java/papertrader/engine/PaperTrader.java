package papertrader.engine;
import papertrader.player.Player;
import papertrader.player.Player.Portfolio;
import papertrader.market.PlayerStock;
import papertrader.engine.MarketSystem;

import java.util.Objects;

public class PaperTrader {


    public static void main(String[] args) {
        Player player = new Player();
        OutputStream outputStream = new OutputStream();
        System.out.println(player.portfolio.getMoney());
        MarketSystem marketSystem = new MarketSystem();
        System.out.println(marketSystem());
        player.buyStock("NVDA", 15);
        player.buyStock("MasonParker", 155);
        outputStream.outputStockList(player);
        System.out.println(player.portfolio.getMoney());

    }
}
