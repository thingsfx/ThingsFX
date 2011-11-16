package com.thingsfx.widget.text;

import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.TextBuilder;

public class ResetableTextFieldSkin implements Skin<ResetableTextField> {

    ResetableTextField control;
    private AnchorPane anchorPane;
    private TextField textField;
    private Shape resetButton;
    private Shape resetButtonText;

    public ResetableTextFieldSkin(ResetableTextField control) {
        this.control = control;
        anchorPane = new AnchorPane();
        textField = new TextField();
        resetButton = RectangleBuilder.create().fill(Color.GREY).opacity(0.5).width(13).height(13).arcWidth(5).arcHeight(5).build();
        resetButtonText = TextBuilder.create().fill(Color.WHITE).opacity(1).text("X").font(new Font(10)).build();
        resetButtonText.setMouseTransparent(true);

        anchorPane.getChildren().addAll(textField, resetButton, resetButtonText);

        AnchorPane.setTopAnchor(textField, 0.0);
        AnchorPane.setLeftAnchor(textField, 0.0);
        AnchorPane.setBottomAnchor(textField, 0.0);
        AnchorPane.setRightAnchor(textField, 0.0);

        AnchorPane.setTopAnchor(resetButton, 4.5);
        AnchorPane.setRightAnchor(resetButton, 4.5);

        AnchorPane.setTopAnchor(resetButtonText, 5.0);
        AnchorPane.setRightAnchor(resetButtonText, 8.0);

    }

    @Override
    public void dispose() {
    }

    @Override
    public Node getNode() {
        return anchorPane;
    }

    @Override
    public ResetableTextField getSkinnable() {
        return control;
    }

    protected TextField getTextField() {
        return textField;
    }

    protected Shape getResetButton() {
        return resetButton;
    }

    protected Shape getResetButtonText() {
        return resetButtonText;
    }

}
