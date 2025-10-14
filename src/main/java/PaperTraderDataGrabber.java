import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PaperTraderDataGrabber {

    private static final String APIKEY = "20CQ8O1XLIZ620N6";

    private static final int startingPlace = 0; //0 to 20

    private static final Map<Integer, Integer> MONTHS_TO_DAYS = Map.ofEntries(
            Map.entry(1, 31),
            Map.entry(2, 28), //Leap Years
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

    public static final List<String> SP_TICKERS = List.of(
            "NVDA","MSFT","AAPL","GOOGL","AMZN","META","AVGO","TSLA","TSM","BRK.B","ORCL","JPM","WMT","LLY","V","NFLX","MA","XOM","JNJ","PLTR","COST","ABBV","BABA","ASML","HD","AMD","BAC","PG","UNH","SAP","GE","CVX","KO","CSCO","IBM","AZN","NVO","NVS","TMUS","WFC","TM","MS","GS","PM","CAT","CRM","ABT","HSBC","AXP","RTX","MRK","LIN","SHEL","MU","SHOP","MCD","RY","APP","TMO","UBER","DIS","ANET","BX","PEP","NOW","T","PDD","SONY","INTU","BLK","ARM","LRCX","INTC","C","QCOM","MUFG","AMAT","NEE","VZ","GEV","SCHW","HDB","BKNG","BA","TXN","ISRG","AMGN","ACN","TJX","APH","SPGI","SAN","DHR","ETN","UL","PANW","GILD","BSX","ADBE","PFE","BHP","PGR","SYK","SPOT","TD","UNP","KLAC","COF","HOOD","LOW","HON","UBS","TTE","CRWD","MDT","DE","SNY","LMT","DASH","CEG","ADP","ADI","RIO","BUD","COP","CB","MELI","WELL","BTI","CMCSA","IBN","MO","SE","KKR","BBVA","PLD","ENB","VRTX","SO","SCCO","DELL","BN","SMFG","MMC","NKE","COIN","HCA","NTES","CVS","DUK","CME","BAM","TT","MCK","CDNS","PH","NEM","GD","MSTR","NOC","BMO","ICE","BMY","SBUX","SNPS","BP","MCO","WM","GSK","RBLX","AMT","ORLY","SNOW","RCL","SHW","RELX","CI","ELV","AEM","MMM","MFG","MDLZ","MRVL","CRH","EQIX","BNS","AON","ECL","CVNA","AJG","CTAS","WMB","NET","PBR","PBR.A","HWM","MSI","PNC","BK","NU","CM","GLW","EMR","ABNB","USB","MAR","RACE","CRWV","NGG","ITW","PYPL","UPS","TDG","BCS","CP","ITUB","JCI","APO","INFY","VST","RSG","ING","FI","TRI","CNQ","LYG","EPD","DB","CSX","MNST","SPG","NSC","ADSK","AZO","FTNT","TEL","PWR","WDAY","AMX","VRT","ZTS","CL","URI","AEP","TRV","KMI","EQNR","FCX","HLT","COR","DLR","EOG","CNI","SRE","ALNY","AFL","NWG","CMI","REGN","CPNG","APD","AXON","B","DDOG","MPC","TFC","ET","LHX","TRP","FDX","ROP","NXPI","CMG","ALL","MFC","MET","BDX","E","O","FAST","GM","DEO","PSX","NDAQ","PSA","D","LNG","IDXX","VLO","PCAR","EA","ZS","SU","CARR","GRMN","SLB","MPLX","VEEV","XYZ","ARGX","WPM","ROST","XEL","JD","ARES","TTWO","STX","VALE","EXC","BKR","MPWR","GWW","TCOM","AMP","AIG","F","FERG","RKT","IMO","DHI","BIDU","OKE","PAYX","CBRE","KR","TAK","EW","WCN","HEI","WBD","OXY","FLUT","MSCI","FER","CPRT","ETR","CTVA","AME","CCI","FANG","UI","EBAY","FNV","FICO","WDC","CHTR","PEG","HLN","RMD","TGT","HMC","VMC","YUM","CCEP","KMB","A","RDDT","DAL","SYM","HEI.A","TEAM","HSY","GFI","ROK","MLM","ALC","ALAB","CCJ","CAH","SYY","CCL","TKO","ONC","TME","WEC","HIG","ED","AU","PRU","PCG","FIS","XYL","LYV","HUM","LVS","MCHP","OTIS","KDP","TRGP","SMCI","PUK","SLF","ABEV","INSM","IQV","CRCL","HPE","EQT","ACGL","EL","SOFI","CHT","VRSK","GEHC","WAB","CTSH","FIG","STT","VICI","CSGP","WTW","RJF","RKLB","UAL","DD","NRG","IBKR","KVUE","NBIS","CVE","NUE","ASTS","BRO","EXR","RYAAY","STLA","FMX","IR","BBD","MT","VTR","IRM","VG","KGC","PSTG","EME","QSR","KB","LEN","ODFL","ADM","KHC","MTB","WRB","NTR","CLS","DTE","KEYS","FIX","WIT","FITB","EFX","K","TSCO","AWK","TEF","WDS","ERIC","ATO","IX","AEE","ROL","NOK","PPL","BR","VOD","FE","TDY","ES","MTD","MDB","PHG","EXPE","SYF","DXCM","FWONA","FOXA","VIK","AVB","TTD","GIS","LPLA","CRDO","CYBR","CNP","STM","VLTO","TW","CQP","ASX","ULTA","CINF","FOX","FTS","BNTX","CBOE","GRAB","LDOS","FSLR","HPQ"
    );

    public static void main(String[] args) {

        String currentWorkingDirectory = System.getProperty("user.dir");
        String dataDirectory = currentWorkingDirectory + "\\jsondata\\";

        String symbolFile = dataDirectory + "data.json";

        File file = new File(symbolFile);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();

        Gson gson = gsonBuilder.create();

        Map<String, CompressedStock> stockMap = new HashMap<>();
        int number = startingPlace * 25;
        for (int i = number; i < number + 25; ++i) {

            String symbol = SP_TICKERS.get(i);

            System.out.println("Starting to create data for " + symbol);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            URLConnection stockData;
            URLConnection stockOverview;
            try {
                stockData = getRawStockData(symbol, true);
                stockOverview = getStockCompanyOverview(symbol);
            } catch (NoSuchURLException e) {
                System.out.println("URL was not found.");
                return;
            } catch (BadConnectionException e) {
                System.out.println("Could not connect to URL.");
                return;
            }
            String stockJsonData;
            String overviewJsonData;
            try {
                stockJsonData = connectionAsString(stockData);
                overviewJsonData = connectionAsString(stockOverview);
            } catch (CouldNotConvertToStringException e) {
                System.out.println("Could not convert json to string");
                return;
            }

            convertToReadableJson(gson, stockMap, symbol, overviewJsonData, stockJsonData);
        }

        try {
            boolean fileExists = file.createNewFile(); // Create file
            try (PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8)) {
                writer.write(gson.toJson(stockMap));
            } catch(IOException e){
                System.out.println("Could not write to file.");
            }
        } catch (IOException e) {
            System.out.println("Could not create file " + symbolFile);
        }
    }

    private static void convertToReadableJson(Gson gson, Map<String, CompressedStock> stockMap, String symbol, String overviewJsonData, String jsonData) {
        JsonElement jsonElement = JsonParser.parseString(jsonData);
        JsonElement pages = jsonElement.getAsJsonObject().get("Time Series (Daily)");

        if (pages == null) {
            System.out.println("Json String " + jsonData + " is invalid!");
            return;
        } else if (pages.isJsonNull() || !pages.isJsonObject()) {
            System.out.println("Improper json data " + pages.toString());
            return;
        }

        Set<Map.Entry<String, JsonElement>> entrySet = pages.getAsJsonObject().entrySet();

        ArrayList<Stock> stockData = new ArrayList<>();

        for (Map.Entry<String, JsonElement> map : entrySet) {
            Stock stock = gson.fromJson(map.getValue(), Stock.class);
            String time = map.getKey();

            int firstHyphen = time.indexOf('-');
            int years = Integer.parseInt(time.substring(0, firstHyphen));

            String leftAfterYears = time.substring(firstHyphen + 1);
            int secondHyphen = leftAfterYears.indexOf('-');
            int months = Integer.parseInt(leftAfterYears.substring(0, secondHyphen));

            int days = Integer.parseInt(leftAfterYears.substring(secondHyphen + 1));

            long inDays = ((years - 1970) * 365L) + days;

            for (int month = 1; month < months; ++month) {
                inDays += getDaysInMonth(month, years);
            }

            stock.days = inDays;

            stockData.add(stock);
        }

        stockMap.put(symbol, new CompressedStock(overviewJsonData, stockData));
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

    private static URLConnection getRawStockData(String symbol, boolean compact) throws NoSuchURLException, BadConnectionException {
        String encodedURL = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY" +
                "&symbol=" + symbol + "&outputsize=" + (compact ? "compact" : "full") + "&apikey=" + APIKEY;

        return getUrlConnection(encodedURL);
    }

    private static URLConnection getStockCompanyOverview(String symbol) throws NoSuchURLException, BadConnectionException {
        String encodedURL = "https://www.alphavantage.co/query?function=OVERVIEW" +
                "&symbol=" + symbol + "&apikey=" + APIKEY;

        return getUrlConnection(encodedURL);
    }

    private static URLConnection getUrlConnection(String encodedURL) throws NoSuchURLException, BadConnectionException {
        URI uri;
        try {
            uri = new URI(encodedURL);
        } catch (URISyntaxException e) {
            throw new NoSuchURLException();
        }

        URLConnection connection;
        try {
            connection = uri.toURL().openConnection();
            connection.setRequestProperty("User-Agent", "PaperTrader/0.1 (academic use; mason.parker@bsu.edu)");
            connection.connect();
        } catch (IOException e) {
            throw new BadConnectionException();
        }
        return connection;
    }

    private static String connectionAsString(URLConnection connection) throws CouldNotConvertToStringException {
        try {
            return new String(connection.getInputStream().readAllBytes(), Charset.defaultCharset());
        } catch (IOException e) {
            throw new CouldNotConvertToStringException();
        }
    }

    public static class NoSuchURLException extends Exception {}

    public static class BadConnectionException extends Exception {}

    public static class CouldNotConvertToStringException extends Exception {}

    public static class Stock {
        long days = 0;
        @SerializedName(value = "open", alternate = {"1. open"})
        double open = 0d;
        @SerializedName(value = "close", alternate = {"4. close"})
        double close = 0d;
        @SerializedName(value = "volume", alternate = {"5. volume"})
        long volume = 0L;
    }

    public static class CompressedStock {

        String sector = "";
        double averageGrowth = 0.0;
        double deviation = 0.0;
        double shareValue = 0.0;
        long shares = 0;

        CompressedStock(String overviewJsonData, ArrayList<Stock> stocks) {

            JsonElement jsonElement = JsonParser.parseString(overviewJsonData);
            JsonElement sect = jsonElement.getAsJsonObject().get("Sector");

            sector = sect.getAsString();

            Stock latestStock = stocks.getFirst();
            shareValue = latestStock.close;
            shares = latestStock.volume;

            Stock oldestStock = stocks.getLast();

            // Calculate mean
            for (Stock stock : stocks) {
                averageGrowth += (stock.close - stock.open);
            }
            averageGrowth /= stocks.size();
            //averageGrowth /= daysBetween;

            // Calculate standard deviation
            for (Stock stock : stocks) {
                double dist = (stock.close - stock.open) - averageGrowth;
                deviation += (dist * dist);
            }
            deviation /= stocks.size();
            deviation = Math.sqrt(deviation);
        }
    }
}
