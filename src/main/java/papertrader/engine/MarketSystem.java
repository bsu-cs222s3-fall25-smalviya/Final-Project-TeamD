package papertrader.engine;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import papertrader.market.StockConfig;
import papertrader.util.JsonToHashMap;

import java.util.HashMap;

public class MarketSystem {


    public  MarketSystem() {
        String jsonStruct = """
        {
          "APPL": {
            "cost": 5,
            "volatility": 0.3
          },
          "GOOG": {
            "cost": 10,
            "volatility": 0.5
          }
        }
        """;

        JsonElement element = JsonParser.parseString(jsonStruct);
        JsonToHashMap parser = new JsonToHashMap(element);
        HashMap<String, StockConfig> stocks = parser.getHashMap();

        stocks.forEach((symbol, config) -> {
            System.out.println(symbol + " => price: " + config.getPrice() +
                    ", volatility: " + config.getVolatility());
        });
    }

}
