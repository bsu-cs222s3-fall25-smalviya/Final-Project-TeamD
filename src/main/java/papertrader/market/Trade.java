package papertrader.market;

// Apart of the player stock class
// Player can buy a stock multiple times, have to
// track trades for stock

public class Trade {
    private final double amount;
    private final double price;

    public Trade(double amount, double price) {
        this.amount = amount;
        this.price = price;
    }

    public double getAmount() { return this.amount; }
    public double getPrice() { return this.price; }
}
