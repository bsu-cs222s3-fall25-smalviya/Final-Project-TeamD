package papertrader.engine;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.HashMap;

public class MarketSystem {

    private static final MarketSystem marketSystem = new MarketSystem();

    public HashMap<String, Stock> stockList;

    public static MarketSystem get() {
        return marketSystem;
    }

    public  MarketSystem() {
        Gson gson = new Gson();

        Type mapType = new TypeToken<HashMap<String, Stock>>(){}.getType();

        try {
            Reader reader = new FileReader(getStockData());
            JsonElement element = JsonParser.parseReader(reader);
            this.stockList = gson.fromJson(element, mapType);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        this.stockList.forEach((symbol, config) -> {
            System.out.println(symbol + " => price: " + config.shareValue +
                    ", #Shares: " + config.shares);
        });
    }

    private File getStockData() {
        String workingDirectory = System.getProperty("user.dir");
        return new File(workingDirectory + "\\data\\StockData.json");
    }

    public static class Stock {
        public double averageGrowth;
        public double deviation;
        public double shareValue;
        public double shares;

        @Override
        public String toString() {
            return "Average Growth: " + averageGrowth + "\n" +
                    "Deviation: " + deviation + "\n" +
                    "Price: " + shareValue + "\n" +
                    "Volume: " + shares;
        }
    }

    public enum TradeType {
        BUY,
        SELL,
        SHORT
    }

    public static class Trade {
        public String name;
        public double amount;
        public TradeType type;

        @Override
        public String toString() {
            return "Stock Name: " + name + "\n" +
                    "Amount: " + amount + "\n" +
                    "Type: " + type;
        }
    }

}
