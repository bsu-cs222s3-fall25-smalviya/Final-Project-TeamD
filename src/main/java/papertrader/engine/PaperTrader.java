package papertrader.engine;
import papertrader.player.Player;

public class PaperTrader {
    private static final boolean GUI_LOADER = false;

    public static void main(String[] args) throws InterruptedException {
        OutputStream outputStream = new OutputStream();
        System.out.println(Player.get().portfolio.getMoney());
        System.out.println(MarketSystem.get().stockList);
        Player.get().buyStock("NVDA", 15);
        Player.get().buyStock("NVDA", 25);
        Player.get().printStockList();
        System.out.println(Player.get().portfolio.getMoney());


        if (!GUI_LOADER) {
            outputStream.CLI();
        } else {
            // UI eventually
        }




    }
}
