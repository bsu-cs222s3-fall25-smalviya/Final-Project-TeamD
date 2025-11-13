package papertrader.core;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class Player {

    private static final Player player = new Player();

    public Portfolio portfolio = new Portfolio();

    public static Player get() {
        return player;
    }

    public static File getSaveFile() {
        String workingDirectory = System.getProperty("user.dir");
        return new File(workingDirectory + "\\data\\PlayerData.json");
    }

    public void saveData() {
        File playerData = getSaveFile();
        try {
            if (playerData.createNewFile()) {
                System.out.println("Created Player Data File.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(playerData)) {
            gson.toJson(this.portfolio, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadData() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(getSaveFile())) {
            this.portfolio = gson.fromJson(reader, Portfolio.class);
        } catch (IOException e) {
            System.out.println("Error loading portfolio!");
        }
    }

    public void loadDefaultData() {
        this.portfolio.money = 100000;
        this.portfolio.trades.clear();
        this.portfolio.ownedStocks.clear();
        this.portfolio.shortedStocks.clear();
    }



    public static class Portfolio {
        private double money;
        private final ArrayList<MarketSystem.Trade> trades = new ArrayList<>();
        private final HashMap<String, Double> ownedStocks = new HashMap<>();
        private final HashMap<String, ShortPosition> shortedStocks = new HashMap<>();

        public double getMoney() { return this.money; }

        public double getTotalMoney() {

            // Get value of all stocks after incrementing them by a day
            AtomicReference<Double> totalInvestment = new AtomicReference<>();
            totalInvestment.set(0.0);

            MarketSystem.get().stockList.forEach((string, _) -> {
                totalInvestment.set(totalInvestment.get() + Player.get().portfolio.getMoneyInStock(string));
            });

            AtomicReference<Double> shortLiabilities = new AtomicReference<>();
            shortLiabilities.set(0.0);

            shortedStocks.forEach((stockName, shortPosition) -> {
                double currentPrice = MarketSystem.get().stockList.get(stockName).shareValue;
                double currentValue = shortPosition.shares * currentPrice;
                shortLiabilities.set(shortLiabilities.get() + currentValue);
            });

            return this.money + totalInvestment.get() - shortLiabilities.get();
        }

        public void addMoney(int amount) {
            this.money += amount;
        }

        public ArrayList<MarketSystem.Trade> getTrades() {
            return trades;
        }

        public double getNumberOfShares(String stockName) {
            return ownedStocks.getOrDefault(stockName, 0.0);
        }

        public double getShortedShares(String stockName) {
            ShortPosition pos = shortedStocks.get(stockName);
            return pos != null ? pos.shares : 0.0;
        }

        public double getMoneyInStock(String stockName) {
            return ownedStocks.getOrDefault(stockName, 0.0) * MarketSystem.get().stockList.get(stockName).shareValue;
        }

        public boolean ownsStock(String stockName) {
            return ownedStocks.containsKey(stockName);
        }

        public boolean hasShortPosition(String stockName) {
            return shortedStocks.containsKey(stockName);
        }

        public boolean isEmpty() {
            return ownedStocks.isEmpty();
        }

        public void buyStock(String stockName, double amountOfShares) {
            if (!MarketSystem.get().stockList.containsKey(stockName)) {
                System.out.println("Stock " + stockName + " does not exist!");
                return;
            }

            MarketSystem.Stock stock = MarketSystem.get().stockList.get(stockName);

            double moneyRequired = amountOfShares * stock.shareValue;

            if (this.money < moneyRequired) {
                System.out.println("Not enough money!");
                return;
            }

            this.money -= moneyRequired;

            // Add to owned stocks
            {
                double value = amountOfShares;

                if (ownedStocks.containsKey(stockName)) {
                    value += ownedStocks.get(stockName);
                }

                ownedStocks.put(stockName, value);
            }

            {
                MarketSystem.Trade trade = new MarketSystem.Trade();
                trade.name = stockName;
                trade.shares = amountOfShares;
                trade.shareValue = stock.shareValue;
                trade.type = MarketSystem.TradeType.BUY;

                trades.add(trade);
            }
        }

        public void sellStock(String stockName, double amountOfShares) {
            if (!MarketSystem.get().stockList.containsKey(stockName)) {
                System.out.println("Stock " + stockName + " does not exist!");
                return;
            }

            if (!this.ownedStocks.containsKey(stockName)) {
                System.out.println("You do not own any of " + stockName + "!");
                return;
            }

            if (this.ownedStocks.get(stockName) < amountOfShares) {
                System.out.println("You do not have enough of " + stockName + "!");
                return;
            }

            MarketSystem.Stock stock = MarketSystem.get().stockList.get(stockName);

            double moneyGained = amountOfShares * stock.shareValue;

            this.money += moneyGained;

            {
                ownedStocks.compute(stockName, (_, amount) -> amount  - amountOfShares);
            }

            {
                MarketSystem.Trade trade = new MarketSystem.Trade();
                trade.name = stockName;
                trade.shares = amountOfShares;
                trade.shareValue = stock.shareValue;
                trade.type = MarketSystem.TradeType.SELL;

                trades.add(trade);
            }
        }

        public void shortStock(String stockName, double amountOfShares) {
            if (!MarketSystem.get().stockList.containsKey(stockName)) {
                System.out.println("Stock " + stockName + " does not exist!");
                return;
            }

            MarketSystem.Stock stock = MarketSystem.get().stockList.get(stockName);

            double moneyReceived = amountOfShares * stock.shareValue;
            this.money += moneyReceived;

            if (shortedStocks.containsKey(stockName)) {
                ShortPosition existing = shortedStocks.get(stockName);
                double totalShares = existing.shares + amountOfShares;
                double weightedAvgPrice = ((existing.shares * existing.initialPrice) + (amountOfShares * stock.shareValue)) / totalShares;
                existing.shares = totalShares;
                existing.initialPrice = weightedAvgPrice;
            } else {
                ShortPosition newPosition = new ShortPosition();
                newPosition.shares = amountOfShares;
                newPosition.initialPrice = stock.shareValue;
                shortedStocks.put(stockName, newPosition);
            }

            {
                MarketSystem.Trade trade = new MarketSystem.Trade();
                trade.name = stockName;
                trade.shares = amountOfShares;
                trade.shareValue = stock.shareValue;
                trade.type = MarketSystem.TradeType.SHORT;

                trades.add(trade);
            }
        }

        public void coverShort(String stockName, double amountOfShares) {
            if (!MarketSystem.get().stockList.containsKey(stockName)) {
                System.out.println("Stock " + stockName + " does not exist!");
                return;
            }

            if (!this.shortedStocks.containsKey(stockName)) {
                System.out.println("You do not have a short position in " + stockName + "!");
                return;
            }

            ShortPosition position = shortedStocks.get(stockName);
            if (position.shares < amountOfShares) {
                System.out.println("You only have " + position.shares + " shares shorted!");
                return;
            }

            MarketSystem.Stock stock = MarketSystem.get().stockList.get(stockName);

            double moneyRequired = amountOfShares * stock.shareValue;

            if (this.money < moneyRequired) {
                System.out.println("Not enough money to cover short position!");
                return;
            }

            this.money -= moneyRequired;

            position.shares -= amountOfShares;
            if (position.shares <= 0) {
                shortedStocks.remove(stockName);
            }

            {
                MarketSystem.Trade trade = new MarketSystem.Trade();
                trade.name = stockName;
                trade.shares = amountOfShares;
                trade.shareValue = stock.shareValue;
                trade.type = MarketSystem.TradeType.COVER;

                trades.add(trade);
            }
        }

        public void removeMoney(int amount) {
            this.money -= amount;
        }

        public static class ShortPosition {
            public double shares;
            public double initialPrice;
        }
    }
}