package papertrader.engine;
import papertrader.player.Player;
import papertrader.player.Player.Portfolio;
import papertrader.market.PlayerStock;
import papertrader.engine.MarketSystem;

import java.util.List;
import java.util.Objects;

public class PaperTrader {

    public static final List<String> USED_TICKERS = List.of(
            "AAPL", "TSLA", "AMZN", "MSFT", "NVDA", "GOOGL", "META", "NFLX",
            "JPM", "V", "BAC", "AMD", "PYPL", "DIS", "T", "PFE", "COST", "INTC",
            "KO", "TGT", "NKE", "SPY", "BA", "BABA", "XOM", "WMT", "GE", "CSCO",
            "VZ", "JNJ", "CVX", "PLTR", "SQ", "SHOP", "SBUX", "SOFI", "HOOD",
            "RBLX", "SNAP", "UBER", "FDX", "ABBV", "ETSY", "MRNA", "LMT", "GM",
            "F", "RIVN", "LCID", "CCL", "DAL", "UAL", "AAL", "TSM", "SONY", "ET",
            "NOK", "MRO", "COIN", "SIRI", "RIOT", "CPRX", "VWO", "SPYG", "ROKU",
            "VIAC", "ATVI", "BIDU", "DOCU", "ZM", "PINS", "TLRY", "WBA", "MGM",
            "NIO", "C", "GS", "WFC", "ADBE", "PEP", "UNH", "CARR", "FUBO", "HCA",
            "TWTR", "BILI", "RKT"
    );

    public static void main(String[] args) {
        Player player = new Player();
        OutputStream outputStream = new OutputStream();
        System.out.println(player.portfolio.getMoney());
        MarketSystem marketSystem = new MarketSystem();
        System.out.println(marketSystem());
        player.buyStock("NVDA", 15);
        player.buyStock("MasonParker", 155);
        outputStream.outputStockList(player);
        System.out.println(player.portfolio.getMoney());

    }
}
