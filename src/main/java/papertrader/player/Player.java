package papertrader.player;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import papertrader.engine.MarketSystem;

import java.io.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Player {

    private static final Player player = new Player();

    public final Portfolio portfolio;

    public static Player get() {
        return player;
    }

    Player() {
        Gson gson = new Gson();
        Portfolio tempPortfolio;
        try (FileReader reader = new FileReader(getSaveFile())) {
            tempPortfolio = gson.fromJson(reader, Portfolio.class);
        } catch (IOException e) {
            tempPortfolio = new Portfolio();
        }
        this.portfolio = tempPortfolio;
    }

    public static File getSaveFile() {
        String workingDirectory = System.getProperty("user.dir");
        return new File(workingDirectory + "\\data\\PlayerData.json");
    }

    public void SaveData() {
        File playerData = getSaveFile();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(playerData)) {
            gson.toJson(this.portfolio, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Portfolio {
        private double money;
        private final ArrayList<MarketSystem.Trade> trades = new ArrayList<>();
        private final HashMap<String, Double> ownedStocks = new HashMap<>();

        Portfolio() {
            // TODO: load in constructor
            this.money = 100000;
        };

        public double getMoney() { return this.money; }

        public void addMoney(int amount) {
            this.money += amount;
        }

        public ArrayList<MarketSystem.Trade> getTrades() {
            return trades;
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

            // Add trade
            {
                MarketSystem.Trade trade = new MarketSystem.Trade();
                trade.name = stockName;
                trade.shares = amountOfShares;
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

            // Remove from owned stocks
            {
                ownedStocks.compute(stockName, (_, amount) -> amount - amountOfShares);
            }

            // Add trade
            {
                MarketSystem.Trade trade = new MarketSystem.Trade();
                trade.name = stockName;
                trade.shares = amountOfShares;
                trade.type = MarketSystem.TradeType.SELL;

                trades.add(trade);
            }
        }

        public void shortStock(String stockName, double amountOfShares) {

        }

        public void removeMoney(int amount) {
            this.money -= amount;
        }
    }
}
