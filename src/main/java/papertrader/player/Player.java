package papertrader.player;
import papertrader.engine.MarketSystem;
import papertrader.market.PlayerStock;

import java.util.HashMap;

public class Player {

    public final Portfolio portfolio = new Portfolio();

    public HashMap<String, PlayerStock> stockList = new HashMap<String, PlayerStock>();

    public void buyStock(String stockName, double Amount) {
        if (!MarketSystem.stockList.containsKey(stockName)) {
            System.out.println("Stock Not Found");
            return;
        }
        stockList.computeIfAbsent(stockName, V -> new PlayerStock(stockName, "BUY"));
        stockList.get(stockName).addTrade(Amount, 15);
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
