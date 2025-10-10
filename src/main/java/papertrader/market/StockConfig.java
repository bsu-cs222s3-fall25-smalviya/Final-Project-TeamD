package papertrader.market;

import com.google.gson.annotations.SerializedName;

public class StockConfig {
    @SerializedName("cost")
    private double price = 100;
    private double volatility = 0.3;

    public double getVolatility() {
        return volatility;
    }

    public void setVolatility(double volatility) {
        this.volatility = volatility;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
