package papertrader.engine;
import papertrader.player.Player;

import java.util.Scanner;

// Output stream should serve all purpose for CLI
// should be instanciated in sub classes and do it output
// an example is buying stock, CLI class should output that
// if successful

public class OutputStream {

    public void ThrowErr(String message) {
        System.out.println("ERROR : " + message);
    }
    private static final Scanner scanner = new Scanner(System.in);

    public void outputStockList(Player player) {
        for (String stock : player.stockList.keySet()) {
            System.out.println("STOCKS OWNED " + stock);
        }
    }

    public void outputMenu() {
        System.out.println("0) Exit Program");
        System.out.println("1) View Stock List");
        System.out.println("2) Buy Stock");
    }

    public void CLI(Player player) {
        while (true) {
            this.outputMenu();
            int Choice = scanner.nextInt();
            switch (Choice) {
                case 0:
                    player.SaveData();
                    System.exit(0);
                case 1: System.out.println("Enter Portfolio Name:");
            }
            System.out.println("Hi");
        }
    }
}
