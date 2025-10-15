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

    public static Player get() {
        return player;
    }

    Player() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(getSaveFile())) {
            this.portfolio = gson.fromJson(reader, Portfolio.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public final Portfolio portfolio;

    public void printStockList() {
        for (MarketSystem.Trade trade : portfolio.trades) {
            System.out.println("STOCKS OWNED " + trade.toString());
        }
    }

    public void buyStock(String stockName, double amount) {
        if (!MarketSystem.get().stockList.containsKey(stockName)) {
            System.out.println("Stock Not Found");
            return;
        }

        portfolio.makeTrade(stockName, amount, MarketSystem.TradeType.BUY);
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

        public void makeTrade(String stockName, double amount, MarketSystem.TradeType tradeType) {
            if (this.money < amount) {
                System.out.println("Not enough money!");
                return;
            }

            this.money -= amount;

            MarketSystem.Trade trade = new MarketSystem.Trade();
            trade.name = stockName;
            trade.amount = amount;
            trade.type = tradeType;

            trades.add(trade);
        }

        public void removeMoney(int amount) {
            this.money -= amount;
        }
    }
}
