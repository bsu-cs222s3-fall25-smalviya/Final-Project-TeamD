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

    private double volatilityMultiplier = 1.0;
    private int simulationSpeed = 1;
    private boolean tradingFeesEnabled = true;
    private boolean realTimeMode = false;
    private double tradingFeePercentage = 0.01;

    public static MarketSystem get() {
        return marketSystem;
    }

    public void setVolatility(double multiplier) {
        this.volatilityMultiplier = Math.max(0.1, Math.min(3.0, multiplier));
    }

    public double getVolatility() {
        return this.volatilityMultiplier;
    }

    public void setSimulationSpeed(int speed) {
        this.simulationSpeed = Math.max(1, Math.min(10, speed));
    }

    public int getSimulationSpeed() {
        return this.simulationSpeed;
    }

    public void setTradingFeesEnabled(boolean enabled) {
        this.tradingFeesEnabled = enabled;
    }

    public boolean areTradingFeesEnabled() {
        return this.tradingFeesEnabled;
    }

    public void setTradingFeePercentage(double percentage) {
        this.tradingFeePercentage = Math.max(0.0, Math.min(0.1, percentage));
    }

    public double calculateTradingFee(double transactionAmount) {
        if (!tradingFeesEnabled) {
            return 0.0;
        }
        return transactionAmount * tradingFeePercentage;
    }

    public void setRealTimeMode(boolean enabled) {
        this.realTimeMode = enabled;
    }

    public boolean isRealTimeMode() {
        return this.realTimeMode;
    }

    public boolean isMarketOpen() {
        if (!realTimeMode) {
            return true;
        }

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            return false;
        }

        int currentMinutes = hour * 60 + minute;
        int marketOpen = 9 * 60 + 30;
        int marketClose = 16 * 60;

        return currentMinutes >= marketOpen && currentMinutes < marketClose;
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
        saveSettings();
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
        resetSettings();
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
        loadSettings();
    }

    private void saveSettings() {
        File settingsFile = getSettingsFile();
        try (DataOutputStream output = new DataOutputStream(new FileOutputStream(settingsFile))) {
            output.writeDouble(volatilityMultiplier);
            output.writeInt(simulationSpeed);
            output.writeBoolean(tradingFeesEnabled);
            output.writeBoolean(realTimeMode);
            output.writeDouble(tradingFeePercentage);
        } catch (IOException e) {
            System.err.println("Failed to save settings: " + e.getMessage());
        }
    }

    private void loadSettings() {
        File settingsFile = getSettingsFile();
        if (!settingsFile.exists()) {
            resetSettings();
            return;
        }

        try (DataInputStream input = new DataInputStream(new FileInputStream(settingsFile))) {
            volatilityMultiplier = input.readDouble();
            simulationSpeed = input.readInt();
            tradingFeesEnabled = input.readBoolean();
            realTimeMode = input.readBoolean();
            tradingFeePercentage = input.readDouble();
        } catch (IOException e) {
            System.err.println("Failed to load settings: " + e.getMessage());
            resetSettings();
        }
    }

    private void resetSettings() {
        volatilityMultiplier = 1.0;
        simulationSpeed = 1;
        tradingFeesEnabled = true;
        realTimeMode = false;
        tradingFeePercentage = 0.01;
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

            StockDate stockDate = new StockDate();
            stockDate.date.month = Time.getCurrentDate().month;
            stockDate.date.day = Time.getCurrentDate().day;
            stockDate.date.year = Time.getCurrentDate().year;
            stockDate.shareValue = stock.shareValue;
            stockDate.shares = stock.shares;
            this.stockHistory.get(string).addFirst(stockDate);

            double baseValue = stock.averageGrowth + random.nextGaussian() * stock.deviation;
            double value = baseValue * volatilityMultiplier;
            stock.shareValue += value;

            double changeChance = 0.025 * volatilityMultiplier;
            if (random.nextFloat() < changeChance) {
                stock.averageGrowth += (random.nextDouble() * 2.0 - 1.0) * 0.01 * volatilityMultiplier;
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

    private File getSettingsFile() {
        String workingDirectory = System.getProperty("user.dir");
        return new File(workingDirectory + "/data/Settings.bin");
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