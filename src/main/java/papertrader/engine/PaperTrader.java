package papertrader.engine;
import papertrader.player.Player;

public class PaperTrader {
    private static final boolean GUI_LOADER = false;

    public static void main(String[] args) {
        OutputStream outputStream = new OutputStream();

        if (!GUI_LOADER) {
            outputStream.CLI();
        } else {
            // UI eventually
        }

    }
}
