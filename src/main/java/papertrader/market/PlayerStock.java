package papertrader.market;

// This class should be apart of  the player class
// When a player buys a stock, we add to their hash map


public class PlayerStock {
    private String name;
    private double amount;
    private double buyPrice;
    private String position;

    public PlayerStock(String name, double amount, double buyPrice, String position) {
        this.name = name;
        this.amount = amount;
        this.buyPrice = buyPrice;
        this.position = position;
    }


}
