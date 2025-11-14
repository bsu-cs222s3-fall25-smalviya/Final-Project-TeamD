package papertrader.gui;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

public class KeyValueLabel extends TextFlow {

    private final Text text0, text1;
    private final String string;
    private double value;

    public KeyValueLabel(String key, String value) {
        setTextAlignment(TextAlignment.CENTER);

        this.string = value;

        this.text0 = new Text(key);
        this.text0.getStyleClass().clear();
        this.text0.getStyleClass().setAll("label");
        this.text0.setFill(Color.WHITE);
        this.text1 = new Text();
        this.text1.getStyleClass().clear();
        this.text1.getStyleClass().setAll("label");
        this.text1.setFill(Color.WHITE);

        this.getChildren().addAll(this.text0, this.text1);

        refreshValue();
    }

    public KeyValueLabel(String value) {
        setTextAlignment(TextAlignment.CENTER);

        this.string = value;

        this.text0 = new Text();
        this.text0.getStyleClass().clear();
        this.text0.getStyleClass().setAll("label");
        this.text0.setFill(Color.WHITE);
        this.text1 = new Text();
        this.text1.getStyleClass().clear();
        this.text1.getStyleClass().setAll("label");
        this.text1.setFill(Color.WHITE);

        this.getChildren().addAll(this.text0, this.text1);

        refreshValue();
    }

    public Text getKey() {
        return this.text0;
    }

    public Text getValue() {
        return this.text1;
    }

    public void setKey(String str) {
        this.text0.setText(str);
    }

    public void setValue(double value) {
        this.value = value;
        this.refreshValue();
    }

    public void setValueColor(Paint color) {
        text1.setFill(color);
    }

    private void refreshValue() {
        this.text1.setText(String.format(this.string, this.value));
    }

    public void addChildrenStyle(String str) {
        text0.getStyleClass().add(str);
        text1.getStyleClass().add(str);
    }

    @SafeVarargs
    public final <T extends String> void addAllChildrenStyle(T... collection) {
        text0.getStyleClass().addAll(collection);
        text1.getStyleClass().addAll(collection);
    }

}
