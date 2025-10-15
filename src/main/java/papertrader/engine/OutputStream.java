package papertrader.engine;
import papertrader.player.Player;

import java.util.Scanner;

// Output stream should serve all purpose for CLI
// should be instanciated in sub classes and do it output
// an example is buying stock, CLI class should output that
// if successful

public class OutputStream {

    public void outputMenu() {
        System.out.println("0) Exit Program");
        System.out.println("1) View Stock");
        System.out.println("2) Buy Stock");
    }

    public void viewStock() {
        System.out.println();
        System.out.print("Enter stock ticker: ");

        Scanner scanner = new Scanner(System.in);

        String ticker = scanner.nextLine();

        if (!MarketSystem.get().stockList.containsKey(ticker)) {
            System.out.println("Stock not found!");
            return;
        }

        MarketSystem.Stock stock = MarketSystem.get().stockList.get(ticker);

        String stockData = stock.toString();

        System.out.println();
        System.out.println(stockData);
        System.out.println();

        System.out.println("Press ENTER to continue.");
        scanner.nextLine();
    }

    public void CLI() {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;
        while (isRunning) {
            this.outputMenu();
            int Choice = scanner.nextInt();
            switch (Choice) {
                case 0:
                    Player.get().SaveData();
                    isRunning = false;
                    break;
                case 1:
                    viewStock();
                    break;
            }
        }
    }
}
