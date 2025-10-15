package papertrader.engine;
import papertrader.player.Player;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Supplier;

public class PaperTrader {
    private static final List<Map.Entry<String, Supplier<Integer>>> ACTIONS = List.of(
            Map.entry("0) Exit Program", () -> 1),
            Map.entry("1) View Stock", PaperTrader::viewStock),
            Map.entry("2) Buy Stock", PaperTrader::buyStock),
            Map.entry("3) Sell Stock", PaperTrader::sellStock)
    );

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;
        while (isRunning) {

            for (Map.Entry<String, Supplier<Integer>> action : ACTIONS) {
                System.out.println(action.getKey());
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            Map.Entry<String, Supplier<Integer>> action = ACTIONS.get(choice);

            if (action.getValue().get() != 0) {
                isRunning = false;
            }
        }
    }

    public static int viewStock() {
        System.out.println();
        System.out.print("Enter stock ticker: ");

        Scanner scanner = new Scanner(System.in);

        String ticker = scanner.nextLine();

        if (!MarketSystem.get().stockList.containsKey(ticker)) {
            System.out.println("Stock not found!");
            return 0;
        }

        MarketSystem.Stock stock = MarketSystem.get().stockList.get(ticker);

        String stockData = stock.toString();

        System.out.println();
        System.out.println(stockData);
        System.out.println();

        System.out.println("Press ENTER to continue.");
        scanner.nextLine();

        return 0;
    }

    public static int buyStock() {
        System.out.println();
        System.out.print("Enter stock ticker: ");

        Scanner scanner = new Scanner(System.in);

        String ticker = scanner.nextLine();

        if (!MarketSystem.get().stockList.containsKey(ticker)) {
            System.out.println("Stock not found!");
            return 0;
        }

        System.out.println();
        System.out.println();
        System.out.print("Enter number of shares: ");

        double amount = scanner.nextDouble();
        scanner.nextLine();

        Player.get().portfolio.buyStock(ticker, amount);

        System.out.println();
        System.out.println("Press ENTER to continue.");
        scanner.nextLine();

        return 0;
    }

    public static int sellStock() {
        System.out.println();
        System.out.print("Enter stock ticker: ");

        Scanner scanner = new Scanner(System.in);

        String ticker = scanner.nextLine();

        if (!MarketSystem.get().stockList.containsKey(ticker)) {
            System.out.println("Stock not found!");
            return 0;
        }

        System.out.println();
        System.out.println();
        System.out.print("Enter number of shares: ");

        double amount = scanner.nextDouble();
        scanner.nextLine();

        Player.get().portfolio.sellStock(ticker, amount);

        System.out.println();
        System.out.println("Press ENTER to continue.");
        scanner.nextLine();

        return 0;
    }
}
