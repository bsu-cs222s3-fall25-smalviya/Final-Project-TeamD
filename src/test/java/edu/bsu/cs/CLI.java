package edu.bsu.cs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import papertrader.core.MarketSystem;
import papertrader.core.Player;
import papertrader.core.Time;

import java.util.ArrayList;

public class CLI {

    @BeforeEach
    public void setup() {
        // Reset to clean state before each test
        Player.get().loadDefaultData();
        MarketSystem.get().loadDefaultData();
    }

    @Test
    public void DefaultPlayer() {
        Player player = Player.get();
        player.loadDefaultData();
        System.out.println(player.portfolio.getMoney());
        Assertions.assertEquals(100000.0, player.portfolio.getMoney());
    }

    @Test
    public void loadPlayer() {
        Player player = Player.get();
        player.loadData();
        System.out.println(player.portfolio.getMoney());
        Assertions.assertTrue(player.portfolio.getMoney() > 1);
    }

    @Test
    public void LoadStocks() {
        MarketSystem market = MarketSystem.get();
        market.loadDefaultData();
        Assertions.assertFalse(market.stockList.isEmpty());
    }

    @Test
    public void IncrementStocks() {
        MarketSystem market = MarketSystem.get();
        market.loadDefaultData();
        double oldShareValue = market.stockList.get("NVDA").shareValue;

        market.incrementStocks();

        Assertions.assertTrue(oldShareValue != market.stockList.get("NVDA").shareValue);
    }

    @Test
    public void BuyStock() {
        Player player = Player.get();
        player.loadDefaultData();
        MarketSystem market = MarketSystem.get();
        market.loadDefaultData();
        player.portfolio.buyStock("NVDA", 1);
        ArrayList<MarketSystem.Trade> trades = player.portfolio.getTrades();
        System.out.println(trades);
        Assertions.assertEquals(1, trades.size());
    }

    @Test
    public void ShortStock() {
        Player player = Player.get();
        player.loadDefaultData();
        MarketSystem market = MarketSystem.get();
        market.loadDefaultData();
        player.portfolio.shortStock("NVDA", 1);
        player.portfolio.coverShort("NVDA", 1);
        Assertions.assertEquals(2, player.portfolio.getTrades().size());
    }

    @Test
    public void NormalSimulation() {
        Player player = Player.get();
        MarketSystem market = MarketSystem.get();

        market.loadDefaultData();
        player.loadDefaultData();

        Double money = player.portfolio.getMoney();

        player.portfolio.buyStock("NVDA", 100);

        for (int i = 1; i < 100; i++) {
            market.incrementStocks();
        }

        player.portfolio.sellStock("NVDA", 100);

        System.out.println(money);
        System.out.println(player.portfolio.getMoney());

        Assertions.assertTrue(player.portfolio.getMoney() != money);
    }

    @Test
    public void testLeapYears() {
        Assertions.assertTrue(Time.isLeapYear(2024));
    }

    @Test
    public void testCaseToMonths() {
        Assertions.assertEquals("Jan", Time.monthToString(1));
    }

    @Test
    public void testDaysInMonth() {
        Assertions.assertEquals(31, Time.getDaysInMonth(1, 2026));
    }

    @Test
    public void testGetTotalMoneyWithStocks() {
        Player player = Player.get();
        player.portfolio.buyStock("NVDA", 10);
        double totalMoney = player.portfolio.getTotalMoney();
        Assertions.assertTrue(totalMoney > 0);
    }

    @Test
    public void testGetNumberOfShares() {
        Player player = Player.get();
        player.portfolio.buyStock("NVDA", 5);
        Assertions.assertEquals(5.0, player.portfolio.getNumberOfShares("NVDA"));
    }

    @Test
    public void testGetNumberOfSharesNonExistent() {
        Player player = Player.get();
        Assertions.assertEquals(0.0, player.portfolio.getNumberOfShares("FAKE"));
    }

    @Test
    public void testOwnsStock() {
        Player player = Player.get();
        player.portfolio.buyStock("NVDA", 1);
        Assertions.assertTrue(player.portfolio.ownsStock("NVDA"));
    }

    @Test
    public void testDoesNotOwnStock() {
        Player player = Player.get();
        Assertions.assertFalse(player.portfolio.ownsStock("FAKE"));
    }

    @Test
    public void testHasShortPosition() {
        Player player = Player.get();
        player.portfolio.shortStock("NVDA", 1);
        Assertions.assertTrue(player.portfolio.hasShortPosition("NVDA"));
    }

    @Test
    public void testGetShortedShares() {
        Player player = Player.get();
        player.portfolio.shortStock("NVDA", 3);
        Assertions.assertEquals(3.0, player.portfolio.getShortedShares("NVDA"));
    }

    @Test
    public void testGetShortedSharesNonExistent() {
        Player player = Player.get();
        Assertions.assertEquals(0.0, player.portfolio.getShortedShares("FAKE"));
    }

    @Test
    public void testSellStockReducesShares() {
        Player player = Player.get();
        player.portfolio.buyStock("NVDA", 10);
        player.portfolio.sellStock("NVDA", 5);
        Assertions.assertEquals(5.0, player.portfolio.getNumberOfShares("NVDA"));
    }

    @Test
    public void testCannotSellMoreThanOwned() {
        Player player = Player.get();
        player.portfolio.buyStock("NVDA", 5);
        double beforeMoney = player.portfolio.getMoney();
        player.portfolio.sellStock("NVDA", 10);
        Assertions.assertEquals(5.0, player.portfolio.getNumberOfShares("NVDA"));
        Assertions.assertEquals(beforeMoney, player.portfolio.getMoney());
    }

    @Test
    public void testCannotBuyWithInsufficientFunds() {
        Player player = Player.get();
        double initialMoney = player.portfolio.getMoney();
        player.portfolio.buyStock("NVDA", 100000);
        Assertions.assertEquals(initialMoney, player.portfolio.getMoney());
    }

    @Test
    public void testCannotShortWithInsufficientMargin() {
        Player player = Player.get();
        player.portfolio.removeMoney(99000);
        player.portfolio.shortStock("NVDA", 100);
        Assertions.assertEquals(0.0, player.portfolio.getShortedShares("NVDA"));
    }

    @Test
    public void testCoverShortRemovesPosition() {
        Player player = Player.get();
        player.portfolio.shortStock("NVDA", 5);
        player.portfolio.coverShort("NVDA", 5);
        Assertions.assertFalse(player.portfolio.hasShortPosition("NVDA"));
    }

    @Test
    public void testPartialCoverShort() {
        Player player = Player.get();
        player.portfolio.shortStock("NVDA", 10);
        player.portfolio.coverShort("NVDA", 6);
        Assertions.assertEquals(4.0, player.portfolio.getShortedShares("NVDA"));
    }

    @Test
    public void testMultipleShortPositionsInSameStock() {
        Player player = Player.get();
        player.portfolio.shortStock("NVDA", 5);
        player.portfolio.shortStock("NVDA", 3);
        Assertions.assertEquals(8.0, player.portfolio.getShortedShares("NVDA"));
    }

    @Test
    public void testAddMoney() {
        Player player = Player.get();
        double before = player.portfolio.getMoney();
        player.portfolio.addMoney(5000);
        Assertions.assertEquals(before + 5000, player.portfolio.getMoney());
    }

    @Test
    public void testRemoveMoney() {
        Player player = Player.get();
        double before = player.portfolio.getMoney();
        player.portfolio.removeMoney(5000);
        Assertions.assertEquals(before - 5000, player.portfolio.getMoney());
    }

    @Test
    public void testIsEmpty() {
        Player player = Player.get();
        Assertions.assertTrue(player.portfolio.isEmpty());
        player.portfolio.buyStock("NVDA", 1);
        Assertions.assertFalse(player.portfolio.isEmpty());
    }

    @Test
    public void testSetVolatility() {
        MarketSystem market = MarketSystem.get();
        market.setVolatility(2.0);
        Assertions.assertEquals(2.0, market.getVolatility());
    }

    @Test
    public void testVolatilityBounds() {
        MarketSystem market = MarketSystem.get();
        market.setVolatility(5.0);
        Assertions.assertEquals(3.0, market.getVolatility());

        market.setVolatility(0.05);
        Assertions.assertEquals(0.1, market.getVolatility());
    }

    @Test
    public void testSetSimulationSpeed() {
        MarketSystem market = MarketSystem.get();
        market.setSimulationSpeed(5);
        Assertions.assertEquals(5, market.getSimulationSpeed());
    }

    @Test
    public void testSimulationSpeedBounds() {
        MarketSystem market = MarketSystem.get();
        market.setSimulationSpeed(15); // Too high
        Assertions.assertEquals(10, market.getSimulationSpeed());

        market.setSimulationSpeed(0); // Too low
        Assertions.assertEquals(1, market.getSimulationSpeed());
    }

    @Test
    public void testTradingFeesEnabled() {
        MarketSystem market = MarketSystem.get();
        market.setTradingFeesEnabled(false);
        Assertions.assertFalse(market.areTradingFeesEnabled());

        market.setTradingFeesEnabled(true);
        Assertions.assertTrue(market.areTradingFeesEnabled());
    }

    @Test
    public void testCalculateTradingFee() {
        MarketSystem market = MarketSystem.get();
        market.setTradingFeesEnabled(true);
        market.setTradingFeePercentage(0.01); // 1%

        double fee = market.calculateTradingFee(1000.0);
        Assertions.assertEquals(10.0, fee, 0.01);
    }

    @Test
    public void testCalculateTradingFeeDisabled() {
        MarketSystem market = MarketSystem.get();
        market.setTradingFeesEnabled(false);

        double fee = market.calculateTradingFee(1000.0);
        Assertions.assertEquals(0.0, fee);
    }

    @Test
    public void testRealTimeMode() {
        MarketSystem market = MarketSystem.get();
        market.setRealTimeMode(true);
        Assertions.assertTrue(market.isRealTimeMode());

        market.setRealTimeMode(false);
        Assertions.assertFalse(market.isRealTimeMode());
    }

    @Test
    public void testMarketAlwaysOpenInSimulationMode() {
        MarketSystem market = MarketSystem.get();
        market.setRealTimeMode(false);
        Assertions.assertTrue(market.isMarketOpen());
    }

    @Test
    public void testStockHistoryCreated() {
        MarketSystem market = MarketSystem.get();
        market.incrementStocks();
        Assertions.assertFalse(market.stockHistory.isEmpty());
    }

    @Test
    public void testStockHistoryGrowsWithIncrements() {
        MarketSystem market = MarketSystem.get();
        market.incrementStocks();
        int firstSize = market.stockHistory.get("NVDA").size();

        market.incrementStocks();
        int secondSize = market.stockHistory.get("NVDA").size();

        Assertions.assertEquals(firstSize + 1, secondSize);
    }

    @Test
    public void testVolatilityAffectsStockMovement() {
        MarketSystem market = MarketSystem.get();

        market.setVolatility(0.1);
        double price1 = market.stockList.get("NVDA").shareValue;
        market.incrementStocks();
        double price2 = market.stockList.get("NVDA").shareValue;
        double lowVolChange = Math.abs(price2 - price1);

        market.loadDefaultData();
        market.setVolatility(3.0);
        double price3 = market.stockList.get("NVDA").shareValue;
        market.incrementStocks();
        double price4 = market.stockList.get("NVDA").shareValue;
        double highVolChange = Math.abs(price4 - price3);

        System.out.println("Low vol change: " + lowVolChange + ", High vol change: " + highVolChange);
    }



    @Test
    public void testNonLeapYear() {
        Assertions.assertFalse(Time.isLeapYear(2023));
        Assertions.assertFalse(Time.isLeapYear(1900));
    }

    @Test
    public void testLeapYearDivisibleBy400() {
        Assertions.assertTrue(Time.isLeapYear(2000));
    }

    @Test
    public void testFebruaryDaysInLeapYear() {
        Assertions.assertEquals(29, Time.getDaysInMonth(2, 2024));
    }

    @Test
    public void testFebruaryDaysInNonLeapYear() {
        Assertions.assertEquals(28, Time.getDaysInMonth(2, 2023));
    }

    @Test
    public void testAllMonthNames() {
        Assertions.assertEquals("Jan", Time.monthToString(1));
        Assertions.assertEquals("Feb", Time.monthToString(2));
        Assertions.assertEquals("Dec", Time.monthToString(12));
    }

    @Test
    public void testTradeTypeRecorded() {
        Player player = Player.get();
        player.portfolio.buyStock("NVDA", 1);

        Assertions.assertEquals(MarketSystem.TradeType.BUY,
                player.portfolio.getTrades().get(0).type);
    }

    @Test
    public void testAllTradeTypesRecorded() {
        Player player = Player.get();

        player.portfolio.buyStock("NVDA", 2);
        player.portfolio.sellStock("NVDA", 1);
        player.portfolio.shortStock("AAPL", 1);
        player.portfolio.coverShort("AAPL", 1);

        ArrayList<MarketSystem.Trade> trades = player.portfolio.getTrades();
        Assertions.assertEquals(4, trades.size());
        Assertions.assertEquals(MarketSystem.TradeType.BUY, trades.get(0).type);
        Assertions.assertEquals(MarketSystem.TradeType.SELL, trades.get(1).type);
        Assertions.assertEquals(MarketSystem.TradeType.SHORT, trades.get(2).type);
        Assertions.assertEquals(MarketSystem.TradeType.COVER, trades.get(3).type);
    }

    @Test
    public void testCannotBuyNonExistentStock() {
        Player player = Player.get();
        double beforeMoney = player.portfolio.getMoney();
        player.portfolio.buyStock("FAKESTOCKXYZ", 10);
        Assertions.assertEquals(beforeMoney, player.portfolio.getMoney());
    }

    @Test
    public void testCannotSellNonExistentStock() {
        Player player = Player.get();
        double beforeMoney = player.portfolio.getMoney();
        player.portfolio.sellStock("FAKESTOCKXYZ", 10);
        Assertions.assertEquals(beforeMoney, player.portfolio.getMoney());
    }

    @Test
    public void testCannotShortNonExistentStock() {
        Player player = Player.get();
        double beforeMoney = player.portfolio.getMoney();
        player.portfolio.shortStock("FAKESTOCKXYZ", 10);
        Assertions.assertEquals(beforeMoney, player.portfolio.getMoney());
    }

    @Test
    public void testCannotCoverNonExistentShort() {
        Player player = Player.get();
        double beforeMoney = player.portfolio.getMoney();
        player.portfolio.coverShort("NVDA", 10);
        Assertions.assertEquals(beforeMoney, player.portfolio.getMoney());
    }
}