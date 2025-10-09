package papertrader.player;

public class Player {

    private final Portfolio portfolio = new Portfolio();

    public static class Portfolio {
        Portfolio() {
            System.out.println("Portfolio");
        }
    }
}
