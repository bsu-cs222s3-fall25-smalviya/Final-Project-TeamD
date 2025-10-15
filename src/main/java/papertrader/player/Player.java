package papertrader.player;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import papertrader.engine.MarketSystem;
import papertrader.market.PlayerStock;

import java.io.*;

import java.util.HashMap;

public class Player {

    public final Portfolio portfolio = new Portfolio();

    public HashMap<String, PlayerStock> stockList = new HashMap<>();

    public void buyStock(String stockName, double Amount) {
        if (!MarketSystem.get().stockList.containsKey(stockName)) {
            System.out.println("Stock Not Found");
            return;
        }
        stockList.computeIfAbsent(stockName, V -> new PlayerStock(stockName, "BUY"));
        stockList.get(stockName).addTrade(Amount, 15, "BUY");
        portfolio.removeMoney((int) (MarketSystem.get().stockList.get(stockName).shares * Amount));
    }

    public static File getSaveFile() {
        String workingDirectory = System.getProperty("user.dir");
        return new File(workingDirectory + "\\data\\PlayerData.json");
    }

    public void SaveData() {
        File playerData = getSaveFile();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(playerData)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Player loadData() {
        File playerData = getSaveFile();
        if (!playerData.exists()) return new Player();

        Gson gson = new Gson();
        try (FileReader reader = new FileReader(playerData)) {
            return gson.fromJson(reader, Player.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Portfolio {
        private  int Money;
        Portfolio() {
            // Data should eventually load
            // in this constructor

            this.Money = 100000;
        };

        public int getMoney() {return this.Money;}

        public void addMoney(int amount) {
            this.Money += amount;
        };

        public void removeMoney(int amount) {
            this.Money -= amount;
        }
    }
}
