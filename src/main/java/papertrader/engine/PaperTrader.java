package papertrader.engine;
import papertrader.player.Player;
import papertrader.player.Player.Portfolio;
import papertrader.market.PlayerStock;

import java.util.Objects;

public class PaperTrader {


    public static void main(String[] args) {
        Player player = new Player();
        OutputStream outputStream = new OutputStream();
        System.out.println(player.portfolio.getMoney());
        player.buyStock("APPL");
        outputStream.outputStockList(player);
    }
}
