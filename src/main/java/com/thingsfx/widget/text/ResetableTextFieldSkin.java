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

    protected static final double RESET_BUTTON_OPACITY_UNHOVERED = 0.60;
    protected static final double RESET_BUTTON_OPACITY_HOVERED = 0.75;
    protected static final double RESET_BUTTON_OPACITY_CLICKED = 1.0;

    private ResetableTextField control;
    private AnchorPane anchorPane;
    private TextField textField;
    private AnchorPane resetButtonPane;
    private Shape resetButton;
    private Shape resetButtonCrossPart1;
    private Shape resetButtonCrossPart2;

    public ResetableTextFieldSkin(ResetableTextField control) {
        this.control = control;

        anchorPane = new AnchorPane();
        textField = new TextField();
        resetButtonPane = new AnchorPane();

        anchorPane.getChildren().addAll(textField, resetButtonPane);

        AnchorPane.setTopAnchor(textField, 0.0);
        AnchorPane.setLeftAnchor(textField, 0.0);
        AnchorPane.setBottomAnchor(textField, 0.0);
        AnchorPane.setRightAnchor(textField, 0.0);
    }

    protected void installResetButton(double buttonHeight) {
        // Starting with the values for standard height of 22.0, formulas found out empirically to give a good scaling effect
        final double internalCrossPadding = buttonHeight / 6.0;
        final double crossLineWidth = internalCrossPadding;
        final double arc = buttonHeight / 2.5;
        final double crossWidth = buttonHeight - (internalCrossPadding * 2 + crossLineWidth * Math.sqrt(2));

        resetButtonPane.getChildren().clear();

        resetButton = RectangleBuilder.create()
                .fill(new Color(0.3, 0.3, 0.3, 1))
                .opacity(RESET_BUTTON_OPACITY_UNHOVERED)
                .width(buttonHeight).height(buttonHeight)
                .arcWidth(arc).arcHeight(arc)
                .build();

        resetButtonCrossPart1 = LineBuilder.create()
                .stroke(Color.WHITE)
                .strokeWidth(crossLineWidth)
                .startX(0).startY(0)
                .endX(crossWidth).endY(crossWidth)
                .build();

        resetButtonCrossPart2 = LineBuilder.create()
                .stroke(Color.WHITE)
                .strokeWidth(crossLineWidth)
                .startX(0).startY(crossWidth)
                .endX(crossWidth).endY(0)
                .build();

        resetButtonPane.getChildren().addAll(resetButton, resetButtonCrossPart1, resetButtonCrossPart2);
        resetButtonPane.setOpacity(RESET_BUTTON_OPACITY_UNHOVERED);

        AnchorPane.setTopAnchor(resetButton, 0.0);
        AnchorPane.setRightAnchor(resetButton, 0.0);

        AnchorPane.setTopAnchor(resetButtonCrossPart1, internalCrossPadding);
        AnchorPane.setLeftAnchor(resetButtonCrossPart1, internalCrossPadding);

        AnchorPane.setTopAnchor(resetButtonCrossPart2, internalCrossPadding);
        AnchorPane.setLeftAnchor(resetButtonCrossPart2, internalCrossPadding);
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
