package papertrader.engine;
import papertrader.player.Player;
import papertrader.player.Player.Portfolio;

public class PaperTrader {


    public static void main(String[] args) {
        Player player = new Player();
        System.out.println(player.portfolio.getMoney());
    }
}
