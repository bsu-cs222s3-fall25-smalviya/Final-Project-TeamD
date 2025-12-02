package papertrader.gui;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;

public class KeyValueLabel extends TextFlow {

    private final Text text0 = new Text();
    private final Text text1 = new Text();
    private final Function<Double, String> string;
    private double value;

    public KeyValueLabel(String key, String value) {
        this(_ -> value);
        this.text0.setText(key);
    }

    public KeyValueLabel(String key, Function<Double, String> value) {
        this(value);
        this.text0.setText(key);
    }

    public KeyValueLabel(String value) {
        this(_ -> value);
    }

    public KeyValueLabel(Function<Double, String> value) {
        setTextAlignment(TextAlignment.CENTER);

        this.string = value;

        this.text0.getStyleClass().add("text");
        this.text0.getStyleClass().add("fill");
        this.text1.getStyleClass().add("text");

        this.getChildren().addAll(this.text0, this.text1);

        refreshValue();
    }

    public Text getKey() {
        return this.text0;
    }

    public double getValue() {
        return this.value;
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
        this.text1.setText(String.format(this.string.apply(this.value), this.value));
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
