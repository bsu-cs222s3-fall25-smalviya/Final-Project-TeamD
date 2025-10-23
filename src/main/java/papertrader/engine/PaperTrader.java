package papertrader.engine;
import papertrader.player.Player;

import java.util.*;
import java.util.function.Supplier;

public class PaperTrader {

    private static final List<Map.Entry<String, Supplier<Integer>>> STARTUP_ACTIONS = List.of(
            Map.entry("0) Exit Program", PaperTrader::exit),
            Map.entry("1) New Game", PaperTrader::newGame),
            Map.entry("2) Continue Game", PaperTrader::continueGame)
    );

    private static final List<Map.Entry<String, Supplier<Integer>>> ACTIONS = List.of(
            Map.entry("0) Exit Program", PaperTrader::exit),
            Map.entry("1) View Stock", PaperTrader::viewStock),
            Map.entry("2) Buy Stock", PaperTrader::buyStock),
            Map.entry("3) Sell Stock", PaperTrader::sellStock),
            Map.entry("4) Next Day", PaperTrader::nextDay),
            Map.entry("5) Next Day (100)", () -> {
                for (int i = 0; i < 100; ++i) {
                    PaperTrader.nextDay();
                }
                return 0;
            }),
            Map.entry("6) Next Day (1000)", () -> {
                for (int i = 0; i < 1000; ++i) {
                    PaperTrader.nextDay();
                }
                return 0;
            })
    );

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;
        while (isRunning) {

            for (Map.Entry<String, Supplier<Integer>> action : STARTUP_ACTIONS) {
                System.out.println(action.getKey());
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            Map.Entry<String, Supplier<Integer>> action = STARTUP_ACTIONS.get(choice);

            if (action.getValue().get() != 0) {
                isRunning = false;
            }
        }
    }

    public static int exit() {
        Player.get().saveData();
        MarketSystem.get().saveData();
        return 1;
    }

    public static int newGame() {
        Player.get().loadDefaultData();
        MarketSystem.get().loadDefaultData();
        return runGame();
    }

    public static int continueGame() {
        Player.get().loadData();
        MarketSystem.get().loadData();
        return runGame();
    }

    public static int runGame() {
        Scanner scanner = new Scanner(System.in);
        while (true) {

            for (Map.Entry<String, Supplier<Integer>> action : ACTIONS) {
                System.out.println(action.getKey());
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            Map.Entry<String, Supplier<Integer>> action = ACTIONS.get(choice);

            if (action.getValue().get() != 0) {
                return action.getValue().get();
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

        double amount;
        try {
            amount = scanner.nextDouble();
        } catch (InputMismatchException _) {
            System.out.println("Not a Number!");
            return 0;
        }
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

        double amount;
        try {
            amount = scanner.nextDouble();
        } catch (InputMismatchException _) {
            System.out.println("Not a Number!");
            return 0;
        }
        scanner.nextLine();

        Player.get().portfolio.sellStock(ticker, amount);

        System.out.println();
        System.out.println("Press ENTER to continue.");
        scanner.nextLine();

        return 0;
    }

    public static int nextDay() {
        MarketSystem.get().incrementStocks();
        return 0;
    }
}
