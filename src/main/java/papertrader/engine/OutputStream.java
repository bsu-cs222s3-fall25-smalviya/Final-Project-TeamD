package papertrader.engine;
import papertrader.player.Player;

// Output stream should serve all purpose for CLI
// should be instanciated in sub classes and do it output
// an example is buying stock, CLI class should output that
// if successful

public class OutputStream {

    public void ThrowErr(String message) {
        System.out.println("ERROR : " + message);
    }

    public void outputStockList(Player player) {
        for (String stock : player.stockList.keySet()) {
            System.out.println("STOCKS OWNED " + stock);
        }
    }

    public void outputMenu() {
        System.out.println("0) Exit Program");
        System.out.println("1) View Data");
    }
}
