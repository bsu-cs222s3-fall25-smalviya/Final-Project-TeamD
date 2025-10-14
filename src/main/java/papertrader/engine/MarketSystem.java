package papertrader.engine;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import papertrader.market.StockConfig;
import papertrader.util.JsonToHashMap;

import java.util.HashMap;

public class MarketSystem {


    public static HashMap<String, StockConfig> stockList = new HashMap<>();


    public  MarketSystem() {
        String jsonStruct = """
                {
                  "NVDA": {
                    "sector": "TECHNOLOGY",
                    "averageGrowth": 0.15154999999999944,
                    "deviation": 2.5080674527412543,
                    "shareValue": 188.32,
                    "shares": 153482755
                  }
                }
        """;

        JsonElement element = JsonParser.parseString(jsonStruct);
        JsonToHashMap parser = new JsonToHashMap(element);
        stockList = parser.getHashMap();

        stockList.forEach((symbol, config) -> {
            System.out.println(symbol + " => price: " + config.getPrice() +
                    ", volatility: " + config.getVolatility());
        });
    }

}
