package com.thingsfx.widget.text;

import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineBuilder;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.shape.Shape;

public class ResetableTextFieldSkin implements Skin<ResetableTextField> {

    protected static final int RESET_BUTTON_CROSS_WIDTH = 6;
    protected static final int RESET_BUTTON_CROSS_LINE_WIDTH = 2;
    protected static final double RESET_BUTTON_OPACITY_UNHOVERED = 0.60;
    protected static final double RESET_BUTTON_OPACITY_HOVERED = 0.75;
    protected static final double RESET_BUTTON_OPACITY_CLICKED = 1.0;

    private ResetableTextField control;
    private AnchorPane anchorPane;
    private AnchorPane resetButtonPane;
    private TextField textField;
    private Shape resetButton;
    private Shape resetButtonCrossPart1;
    private Shape resetButtonCrossPart2;

    public ResetableTextFieldSkin(ResetableTextField control) {
        this.control = control;
        anchorPane = new AnchorPane();
        textField = new TextField();
        resetButton = RectangleBuilder.create().fill(new Color(0.3, 0.3, 0.3, 1)).opacity(RESET_BUTTON_OPACITY_UNHOVERED).width(13).height(13).arcWidth(5)
                .arcHeight(5).build();

        resetButtonCrossPart1 = LineBuilder.create().stroke(Color.WHITE).strokeWidth(RESET_BUTTON_CROSS_LINE_WIDTH).startX(0).startY(0)
                .endX(RESET_BUTTON_CROSS_WIDTH).endY(RESET_BUTTON_CROSS_WIDTH).build();
        resetButtonCrossPart2 = LineBuilder.create().stroke(Color.WHITE).strokeWidth(RESET_BUTTON_CROSS_LINE_WIDTH).startX(0).startY(RESET_BUTTON_CROSS_WIDTH)
                .endX(RESET_BUTTON_CROSS_WIDTH).endY(0).build();

        resetButtonPane = new AnchorPane();
        resetButtonPane.getChildren().addAll(resetButton, resetButtonCrossPart1, resetButtonCrossPart2);
        resetButtonPane.setOpacity(RESET_BUTTON_OPACITY_UNHOVERED);

        AnchorPane.setTopAnchor(resetButton, 0.0);
        AnchorPane.setRightAnchor(resetButton, 0.0);

        AnchorPane.setTopAnchor(resetButtonCrossPart1, 2.0);
        AnchorPane.setLeftAnchor(resetButtonCrossPart1, 2.0);

        AnchorPane.setTopAnchor(resetButtonCrossPart2, 2.0);
        AnchorPane.setLeftAnchor(resetButtonCrossPart2, 2.0);

        anchorPane.getChildren().addAll(textField, resetButtonPane);

        AnchorPane.setTopAnchor(textField, 0.0);
        AnchorPane.setLeftAnchor(textField, 0.0);
        AnchorPane.setBottomAnchor(textField, 0.0);
        AnchorPane.setRightAnchor(textField, 0.0);

        AnchorPane.setTopAnchor(resetButtonPane, 4.5);
        AnchorPane.setRightAnchor(resetButtonPane, 4.5);

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

    protected AnchorPane getResetButton() {
        return resetButtonPane;
    }
}
