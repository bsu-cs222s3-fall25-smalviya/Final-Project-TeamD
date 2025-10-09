package papertrader.player;
import papertrader.market.PlayerStock;

import java.lang.reflect.Array;
import java.util.HashMap;

public class Player {

    public final Portfolio portfolio = new Portfolio();

    public HashMap<String, PlayerStock> stockList = new HashMap<String, PlayerStock>();

    public void buyStock() {
    }

    public static class Portfolio {
        private  int Money;
        Portfolio() {
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
