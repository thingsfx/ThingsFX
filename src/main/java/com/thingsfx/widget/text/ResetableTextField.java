package com.thingsfx.widget.text;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class ResetableTextField extends TextField {

    private ResetableTextFieldSkin skin;

    public ResetableTextField() {
        super();
        initSkin();
    }

    public ResetableTextField(String arg0) {
        super(arg0);
        initSkin();
    }

    private void initSkin() {
        skin = new ResetableTextFieldSkin(this);
        setSkin(skin);

        textProperty().bindBidirectional(skin.getTextField().textProperty());
        promptTextProperty().bindBidirectional(skin.getTextField().promptTextProperty());
        contextMenuProperty().bindBidirectional(skin.getTextField().contextMenuProperty());
        disableProperty().bindBidirectional(skin.getNode().disableProperty());
        editableProperty().bindBidirectional(skin.getTextField().editableProperty());
        effectProperty().bindBidirectional(skin.getNode().effectProperty());
        maxHeightProperty().bindBidirectional(skin.getTextField().maxHeightProperty());
        minHeightProperty().bindBidirectional(skin.getTextField().minHeightProperty());
        maxWidthProperty().bindBidirectional(skin.getTextField().maxWidthProperty());
        minWidthProperty().bindBidirectional(skin.getTextField().minWidthProperty());
        opacityProperty().bindBidirectional(skin.getNode().opacityProperty());
        scaleXProperty().bindBidirectional(skin.getNode().scaleXProperty());
        scaleYProperty().bindBidirectional(skin.getNode().scaleYProperty());
        scaleZProperty().bindBidirectional(skin.getNode().scaleZProperty());

        skin.getTextField().textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
                skin.getResetButton().setVisible(!arg2.isEmpty());
            }
        });

        skin.getTextField().setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
                    skin.getTextField().clear();
                }
            }
        });

        skin.getResetButton().hoverProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean hovered) {
                skin.getResetButton().setOpacity(
                        hovered ? ResetableTextFieldSkin.RESET_BUTTON_OPACITY_HOVERED : ResetableTextFieldSkin.RESET_BUTTON_OPACITY_UNHOVERED);
                skin.getResetButton().setCursor(Cursor.DEFAULT);
            }

        });
        skin.getResetButton().setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                skin.getResetButton().setOpacity(ResetableTextFieldSkin.RESET_BUTTON_OPACITY_CLICKED);
            }
        });

        skin.getResetButton().setOnMouseReleased(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                skin.getResetButton().setOpacity(ResetableTextFieldSkin.RESET_BUTTON_OPACITY_HOVERED);
                skin.getTextField().clear();
            }
        });
    }
}
