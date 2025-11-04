package papertrader.gui;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class SwitchPane extends AnchorPane {

    public void set(Node node) {
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
        getChildren().setAll(node);
    }
}
