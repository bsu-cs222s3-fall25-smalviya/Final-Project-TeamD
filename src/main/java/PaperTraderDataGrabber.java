import com.google.gson.*;
import com.google.gson.annotations.Expose;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PaperTraderDataGrabber {

    private static final String APIKEY = "PQVbisd28MxeTJJSWyEVHCerZnNkbkvI";

    public static final List<String> TICKERS = List.of(
            "AAPL", "TSLA", "AMZN", "MSFT", "NVDA", "GOOGL", "META", "NFLX",
            "JPM", "V", "BAC", "AMD", "PYPL", "DIS", "T", "PFE", "COST", "INTC",
            "KO", "TGT", "NKE", "SPY", "BA", "BABA", "XOM", "WMT", "GE", "CSCO",
            "VZ", "JNJ", "CVX", "PLTR", "SQ", "SHOP", "SBUX", "SOFI", "HOOD",
            "RBLX", "SNAP", "UBER", "FDX", "ABBV", "ETSY", "MRNA", "LMT", "GM",
            "F", "RIVN", "LCID", "CCL", "DAL", "UAL", "AAL", "TSM", "SONY", "ET",
            "NOK", "MRO", "COIN", "SIRI", "RIOT", "CPRX", "VWO", "SPYG", "ROKU",
            "BIDU", "DOCU", "ZM", "PINS", "TLRY", "MGM",
            "NIO", "C", "GS", "WFC", "ADBE", "PEP", "UNH", "CARR", "FUBO", "HCA",
            "BILI", "RKT"
    );

    public static void main(String[] args) {

        Gson gson = getGson();

        Map<String, CompressedStock> stockMap = new TreeMap<>();

        Map<String, ArrayList<SplitStock>> nestedStockHistory = new TreeMap<>();
        for (String symbol : TICKERS) {
            System.out.println("Starting to create data for " + symbol);

            String encodedURL = "https://financialmodelingprep.com/stable/historical-price-eod/full" +
                    "?symbol=" + symbol + "&from=2024-1-1" + "&to=2025-1-1" + "&apikey=" + APIKEY;

            URLConnection connection = getUrlConnection(encodedURL);

            assert connection != null;
            String stockJsonData = connectionAsString(connection);

            ArrayList<SplitStock> stockData = new ArrayList<>();
            if (!convertToReadableJson(gson, stockData, stockJsonData)) continue;
            nestedStockHistory.put(symbol, stockData);

            stockMap.put(symbol, new CompressedStock(stockData));
        }

        writeToDataFile(gson.toJson(stockMap));
        writeToHistoryFile(gson.toJson(nestedStockHistory));
        writeToBinaryHistoryFile(nestedStockHistory);

        /*
        File file = getBinaryHistoryFile();
        try (DataInputStream input = new DataInputStream(new FileInputStream(file))){
            String stockName = input.readUTF();

            int arrLength = input.readInt();
            ArrayList<SplitStock> stocks = new ArrayList<>();
            for (int i = 0; i < arrLength; ++i) {
                SplitStock stock = new SplitStock();
                stock.month = input.readByte();
                stock.day = input.readByte();
                stock.year = input.readShort();
                stock.shareValue = input.readDouble();
                stock.shares = input.readLong();
                stocks.add(stock);
            }

            Map<String, ArrayList<SplitStock>> stocks2 = new TreeMap<>();
            stocks2.put(stockName, stocks);

            System.out.println(gson.toJson(stocks2));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
         */

    }

    private static void writeToDataFile(String string) {
        File file = getDataFile();
        try {
            if (file.createNewFile()) {
                System.out.println("Created new data file.");
            }
            try (PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8)) {
                writer.write(string);
            } catch(IOException e){
                System.out.println("Could not write to file.");
            }
        } catch (IOException e) {
            System.out.println("Could not create file.");
        }
    }

    private static File getDataFile() {
        String currentWorkingDirectory = System.getProperty("user.dir");
        String dataDirectory = currentWorkingDirectory + "\\jsondata\\";

        String symbolFile = dataDirectory + "DefaultStockData.json";

        return new File(symbolFile);
    }

    private static void writeToHistoryFile(String string) {
        File file = getHistoryFile();
        try {
            if (file.createNewFile()) {
                System.out.println("Created new history file.");
            }
            try (PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8)) {
                writer.write(string);
            } catch(IOException e){
                System.out.println("Could not write to file.");
            }
        } catch (IOException e) {
            System.out.println("Could not create file.");
        }
    }

    private static void writeToBinaryHistoryFile(Map<String, ArrayList<SplitStock>> src) {
        File file = getBinaryHistoryFile();
        try {
            if (file.createNewFile()) {
                System.out.println("Created new history file.");
            }
            try (DataOutputStream writer = new DataOutputStream(new FileOutputStream(file))) {
                for (Map.Entry<String, ArrayList<SplitStock>> entry : src.entrySet()) {
                    writer.writeUTF(entry.getKey());

                    writer.writeInt(entry.getValue().size()); //Decoder needs to know how long the array is so it knows when to stop reading
                    for (SplitStock stock : entry.getValue()) {
                        writer.writeByte(stock.month);
                        writer.writeByte(stock.day);
                        writer.writeShort(stock.year);
                        writer.writeDouble(stock.shareValue);
                        writer.writeInt(stock.shares);
                    }
                }
            } catch(IOException e){
                System.out.println("Could not write to file.");
            }
        } catch (IOException e) {
            System.out.println("Could not create file.");
        }
    }

    private static File getHistoryFile() {
        String currentWorkingDirectory = System.getProperty("user.dir");
        String dataDirectory = currentWorkingDirectory + "\\jsondata\\";

        String symbolFile = dataDirectory + "StockHistory.json";

        return new File(symbolFile);
    }

    private static File getBinaryHistoryFile() {
        String currentWorkingDirectory = System.getProperty("user.dir");
        String dataDirectory = currentWorkingDirectory + "\\jsondata\\";

        String symbolFile = dataDirectory + "StockHistory.bin";

        return new File(symbolFile);
    }

    private static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();

        return gsonBuilder.create();
    }

    private static boolean convertToReadableJson(Gson gson, ArrayList<SplitStock> stockData, String jsonData) {
        JsonElement jsonElement = JsonParser.parseString(jsonData);

        JsonArray array = jsonElement.getAsJsonArray();

        List<JsonElement> elements = array.asList();

        for (JsonElement element : elements) {
            Stock stock = gson.fromJson(element, Stock.class);

            String time = stock.date;

            int firstHyphen = time.indexOf('-');
            short years = Short.parseShort(time.substring(0, firstHyphen));

            String leftAfterYears = time.substring(firstHyphen + 1);
            int secondHyphen = leftAfterYears.indexOf('-');
            byte months = Byte.parseByte(leftAfterYears.substring(0, secondHyphen));

            byte days = Byte.parseByte(leftAfterYears.substring(secondHyphen + 1));

            long inDays = ((years - 1970) * 365L) + days;

            for (int month = 1; month < months; ++month) {
                inDays += getDaysInMonth(month, years);
            }

            stock.days = inDays;

            SplitStock splitStock = new SplitStock();
            splitStock.day = days;
            splitStock.month = months;
            splitStock.year = years;
            splitStock.shareValue = stock.close;
            splitStock.shares = stock.volume;

            splitStock.open = stock.open;
            splitStock.close = stock.close;

            // Skip data that isn't 2024
            if (years < 2024 || years > 2025) {
                continue;
            }

            stockData.add(splitStock);
        }

        if (stockData.isEmpty()) {
            System.out.println("Error Creating array: output \n" + jsonData);
            return false;
        }
        return true;
    }

    private static int getDaysInMonth(int month, int year) {
        Map<Integer, Integer> MONTHS_TO_DAYS = Map.ofEntries(
                Map.entry(1, 31),
                Map.entry(3, 31),
                Map.entry(4, 30),
                Map.entry(5, 31),
                Map.entry(6, 30),
                Map.entry(7, 31),
                Map.entry(8, 31),
                Map.entry(9, 30),
                Map.entry(10, 31),
                Map.entry(11, 30),
                Map.entry(12, 31)
        );

        // February has an extra day on leap years
        if (month == 2) {
            return isLeapYear(year) ? 29 : 28;
        }
        if (!MONTHS_TO_DAYS.containsKey(month)) {
            System.out.println("Month " + month + " does not exist!");
            return 0;
        }
        return MONTHS_TO_DAYS.get(month);
    }

    private static boolean isLeapYear(int year) {
        return year % 400 == 0 || (year % 4 == 0 && year % 100 != 0);
    }

    private static URLConnection getUrlConnection(String encodedURL) {
        URI uri;
        try {
            uri = new URI(encodedURL);
        } catch (URISyntaxException e) {
            System.out.println("URL Not Found");
            return null;
        }

        URLConnection connection;
        try {
            connection = uri.toURL().openConnection();
            connection.setRequestProperty("User-Agent", "PaperTrader/0.1 (academic use; mason.parker@bsu.edu)");
            connection.connect();
        } catch (IOException e) {
            System.out.println("Bad Connection. Check internet.");
            return null;
        }
        return connection;
    }

    private static String connectionAsString(URLConnection connection) {
        try {
            return new String(connection.getInputStream().readAllBytes(), Charset.defaultCharset());
        } catch (IOException e) {
            System.out.println("Could not convert to string!");
            return "";
        }
    }

    public static class Stock {
        public long days = 0;
        public String date;
        public double open = 0d;
        public double close = 0d;
        public int volume = 0;
    }

    public static class SplitStock {
        public byte month = 0;
        public byte day = 0;
        public short year = 0;
        public double shareValue = 0d;
        public int shares = 0;

        @Expose(serialize = false, deserialize = false)
        public double open = 0d;
        @Expose(serialize = false, deserialize = false)
        public double close = 0d;

        public long getDaysSinceJan1st1970() {
            return getDaysSince((short) 1970);
        }

        public long getDaysSince(short year) {
            long inDays = ((this.year - year) * 365L) + this.day;

            for (int m = 1; m < this.month; ++m) {
                inDays += getDaysInMonth(m, this.year);
            }

            return inDays;
        }
    }

    public static class CompressedStock {

        double averageGrowth = 0.0;
        double deviation = 0.0;
        double shareValue = 0.0;
        int shares = 0;

        CompressedStock(ArrayList<SplitStock> stocks) {

            SplitStock latestStock = stocks.getFirst();
            shareValue = latestStock.close;
            shares = latestStock.shares;

            SplitStock oldestStock = stocks.getLast();

            // Calculate mean
            for (SplitStock stock : stocks) {
                averageGrowth += (stock.close - stock.open);
            }
            averageGrowth /= stocks.size();
            //averageGrowth /= daysBetween;

            // Calculate standard deviation
            for (SplitStock stock : stocks) {
                double dist = (stock.close - stock.open) - averageGrowth;
                deviation += (dist * dist);
            }
            deviation /= stocks.size();
            deviation = Math.sqrt(deviation);
        }
    }
}
