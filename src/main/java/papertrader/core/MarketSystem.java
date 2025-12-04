package papertrader.core;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class MarketSystem {

    private static final MarketSystem marketSystem = new MarketSystem();

    public TreeMap<String, Stock> stockList = new TreeMap<>();
    public final TreeMap<String, ArrayList<StockDate>> stockHistory = new TreeMap<>();

    public static MarketSystem get() {
        return marketSystem;
    }

    public void saveData() {
        try {
            if (getStockData().createNewFile()) {
                System.out.println("Created Player Data File.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(getStockData())) {
            gson.toJson(this.stockList, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        saveStockHistory(getHistoryFile());
    }

    public void loadDefaultData() {
        Gson gson = new Gson();

        Type mapType = new TypeToken<TreeMap<String, Stock>>(){}.getType();

        try {
            Reader reader = new FileReader(getDefaultStockData());
            JsonElement element = JsonParser.parseReader(reader);
            this.stockList = gson.fromJson(element, mapType);

            this.stockList.forEach((string, stock) -> {
                stock.averageGrowth += 0.025; // Skew average growth
            });

        } catch (FileNotFoundException e) {
            // If no data, create an empty stock list
            System.out.println("No Stock data found!");
            throw new RuntimeException();
        }

        loadStockHistory(getDefaultHistoryFile());
    }

    public void loadData() {
        File file = getStockData();
        if (!file.exists()) {
            loadDefaultData();
            return;
        }

        Gson gson = new Gson();

        Type mapType = new TypeToken<TreeMap<String, Stock>>(){}.getType();

        try {
            Reader reader = new FileReader(file);
            JsonElement element = JsonParser.parseReader(reader);
            this.stockList = gson.fromJson(element, mapType);
        } catch (FileNotFoundException e) {
            // If no data, create an empty stock list
            System.out.println("No Stock data found!");
            throw new RuntimeException();
        }

        loadStockHistory(getHistoryFile());
    }

    public void saveStockHistory(File file) {
        try (DataOutputStream output = new DataOutputStream(new FileOutputStream(file))){
            for (var stockDates : this.stockHistory.entrySet()) {
                output.writeUTF(stockDates.getKey());

                output.writeInt(stockDates.getValue().size());
                for (var stock : stockDates.getValue()) {
                    output.writeByte(stock.date.month);
                    output.writeByte(stock.date.day);
                    output.writeShort(stock.date.year);
                    output.writeDouble(stock.shareValue);
                    output.writeInt((int)stock.shares);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadStockHistory(File file) {
        try (DataInputStream input = new DataInputStream(new FileInputStream(file))){
            while (input.available() != 0) {
                String stockName = input.readUTF();

                int arrLength = input.readInt();
                ArrayList<StockDate> stockDates = new ArrayList<>();
                for (int i = 0; i < arrLength; ++i) {
                    StockDate stock = new StockDate();
                    stock.date.month = input.readByte();
                    stock.date.day = input.readByte();
                    stock.date.year = input.readShort();
                    stock.shareValue = input.readDouble();
                    stock.shares = input.readInt();
                    stockDates.add(stock);
                }
                this.stockHistory.put(stockName, stockDates);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void incrementStocks() {
        Random random = new Random();

        MarketSystem.get().stockList.forEach((string, stock) -> {

            // Add previous date and value before creating new values
            StockDate stockDate = new StockDate();
            stockDate.date.month = Time.getCurrentDate().month;
            stockDate.date.day = Time.getCurrentDate().day;
            stockDate.date.year = Time.getCurrentDate().year;
            stockDate.shareValue = stock.shareValue;
            stockDate.shares = stock.shares;
            this.stockHistory.get(string).addFirst(stockDate);

            double value = stock.averageGrowth + random.nextGaussian() * stock.deviation;
            stock.shareValue += value;

            if (random.nextFloat() < 0.025) { // Small chance to grow or shrink stock
                stock.averageGrowth += (random.nextDouble() * 2.0 - 1.0) * 0.01;
            }
        });
    }

    private File getStockData() {
        String workingDirectory = System.getProperty("user.dir");
        return new File(workingDirectory + "/data/StockData.json");
    }

    private File getDefaultStockData() {
        String defaultDataLocation = Objects.requireNonNull(getClass().getResource("/DefaultStockData.json")).getFile();
        return new File(defaultDataLocation);
    }

    private File getHistoryFile() {
        String workingDirectory = System.getProperty("user.dir");
        return new File(workingDirectory + "/data/StockHistory.bin");
    }

    private File getDefaultHistoryFile() {
        String defaultDataLocation = Objects.requireNonNull(getClass().getResource("/DefaultStockHistory.bin")).getFile();
        return new File(defaultDataLocation);
    }

    public static class Stock {
        public double averageGrowth;
        public double deviation;
        public double shareValue;
        public int shares;

        @Override
        public String toString() {
            return "Average Growth: " + averageGrowth + "\n" +
                    "Deviation: " + deviation + "\n" +
                    "Price: " + shareValue + "\n" +
                    "Volume: " + shares;
        }
    }

    public static class StockDate {
        public Time.Date date = new Time.Date();
        public double shareValue = 0d;
        public double shares = 0d;
    }

    public enum TradeType {
        BUY,
        SELL,
        SHORT,
        COVER
    }

    public static class Trade {
        public String name;
        public StockDate stockDate = new StockDate();
        public TradeType type;

        @Override
        public String toString() {
            return "Stock Name: " + name + "\n" +
                    "Time: " + stockDate.date.toString() + "\n" +
                    "Amount of Shares: " + stockDate.shares + "\n" +
                    "Share Value: " + stockDate.shareValue + "\n" +
                    "Type: " + type;
        }
    }

}
