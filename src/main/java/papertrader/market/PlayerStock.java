package papertrader.market;

import java.util.ArrayList;
import java.util.List;

public class PlayerStock {
    private final String name;
    private final String position;
    private final List<Trade> trades = new ArrayList<Trade>();

    public PlayerStock(String name, String position) {
        this.name = name;
        this.position = position;
    }

    public void addTrade(double amount, double price) {
        trades.add(new Trade(amount, price));
    }

    public double getTotalAmount() {
        return trades.stream().mapToDouble(Trade::getAmount).sum();
    }

    public double getAveragePrice() {
        double totalCost = 0;
        double totalAmount = 0;

        for (Trade trade : trades) {
            totalCost += trade.getPrice() * trade.getAmount();
            totalAmount += trade.getAmount();
        }

        return totalAmount > 0 ? totalCost / totalAmount : 0;
    }

    public String getName() { return name; }
    public String getPosition() { return position; }

}
