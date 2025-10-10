package papertrader.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import papertrader.market.StockConfig;

import java.lang.reflect.Type;
import java.util.HashMap;

public class JsonToHashMap {
    private final HashMap<String, StockConfig> stockList;

    public JsonToHashMap(JsonElement jsonElement) {
        Type mapType = new TypeToken<HashMap<String, StockConfig>>(){}.getType();
        Gson gson = new Gson();
        this.stockList = gson.fromJson(jsonElement, mapType);
    }

    public HashMap<String, StockConfig> getHashMap() {
        return stockList;
    }
}
