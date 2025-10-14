package papertrader.engine;
import papertrader.player.Player;
import papertrader.player.Player.Portfolio;
import papertrader.market.PlayerStock;
import papertrader.engine.MarketSystem;

import java.util.Objects;
import java.util.Scanner;

public class PaperTrader {

    private static final Scanner scanner = new Scanner(System.in);
    private static boolean keepGoing = true;

    public static void main(String[] args) throws InterruptedException {
        Player player = Player.loadData();
        OutputStream outputStream = new OutputStream();
        System.out.println(player.portfolio.getMoney());
        MarketSystem marketSystem = new MarketSystem();
        System.out.println(MarketSystem.stockList);
        player.buyStock("NVDA", 15);
        player.buyStock("NVDA", 25);
        outputStream.outputStockList(player);
        System.out.println(player.portfolio.getMoney());

        while (keepGoing) {
            outputStream.outputMenu();
            int Choice = scanner.nextInt();
            switch (Choice) {
                case 0:
                    player.SaveData();
                    System.exit(0);
                case 1:
                    System.out.println("Enter Portfolio Name:");
            }
            System.out.println("Hi");
        }

    }
}
