package papertrader.market;

// Apart of the player stock class
// Player can buy a stock multiple times, have to
// track trades for stock

public class Trade {
    private final double amount;
    private final double price;
    private final String position;

    public Trade(double amount, double price, String position) {
        this.amount = amount;
        this.price = price;
        this.position = position;
    }

    public double getAmount() { return this.amount; }
    public double getPrice() { return this.price; }
    public String getPosition() { return this.position; }
}
