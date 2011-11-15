package com.thingsfx.widget.map;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Skin;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;

import com.thingsfx.widget.map.MapViewer.MapProvider;
import com.thingsfx.widget.map.MapViewer.MapType;

class MapViewSkin implements Skin<MapView> {

    private WebView webView;
    private WebEngine webEngine;
    private MapView control;
    private TextField locationTextField;
    private ChoiceBox<MapProvider> mapProviderChoiceBox;
    private BorderPane topPanel;
    private Slider zoomSlider;
    private ChoiceBox<MapType> mapTypeChoiceBox;
    private BorderPane bottomPanel;
    private BorderPane root;
    private AnchorPane anchorPane;
    private Timeline animationTimeline;

    public MapViewSkin(MapView control) {
        this.control = control;

        webView = new WebView();
        webView.setMouseTransparent(true);
        webEngine = webView.getEngine();

        locationTextField = new TextField();
        locationTextField.setPromptText("Location (currently works only for Google Maps)");

        mapProviderChoiceBox = new ChoiceBox<MapViewer.MapProvider>();
        mapProviderChoiceBox.getItems().addAll(MapProvider.values());

        topPanel = new BorderPane();
        topPanel.setCenter(locationTextField);
        topPanel.setRight(mapProviderChoiceBox);

        zoomSlider = new Slider();
        zoomSlider.setMin(0);
        zoomSlider.setMax(1);

        mapTypeChoiceBox = new ChoiceBox<MapViewer.MapType>();
        mapTypeChoiceBox.getItems().addAll(MapType.values());

        bottomPanel = new BorderPane();
        bottomPanel.setCenter(zoomSlider);
        bottomPanel.setRight(mapTypeChoiceBox);

        anchorPane = new AnchorPane();

        anchorPane.getChildren().addAll(webView, topPanel, bottomPanel);

        AnchorPane.setTopAnchor(topPanel, 8.0);
        AnchorPane.setRightAnchor(topPanel, 5.0);
        AnchorPane.setLeftAnchor(topPanel, 5.0);

        AnchorPane.setTopAnchor(webView, 0.0);
        AnchorPane.setLeftAnchor(webView, 0.0);
        AnchorPane.setBottomAnchor(webView, 0.0);
        AnchorPane.setRightAnchor(webView, 0.0);

        AnchorPane.setBottomAnchor(bottomPanel, 8.0);
        AnchorPane.setRightAnchor(bottomPanel, 5.0);
        AnchorPane.setLeftAnchor(bottomPanel, 5.0);

        root = new BorderPane();
        root.setCenter(anchorPane);

        animationTimeline = new Timeline(500);
        animationTimeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(topPanel.opacityProperty(), 0.9, Interpolator.EASE_BOTH),
                        new KeyValue(bottomPanel.opacityProperty(), 0.9, Interpolator.EASE_BOTH)

                ),
                new KeyFrame(new Duration(500),
                        new KeyValue(topPanel.opacityProperty(), 0.2, Interpolator.EASE_BOTH),
                        new KeyValue(bottomPanel.opacityProperty(), 0.2, Interpolator.EASE_BOTH)
                )
                );

        setControlsVisible(false);
    }

    private boolean areChoiceBoxShowing() {
        return getMapProviderChoiceBox().isShowing() || getMapTypeChoiceBox().isShowing();
    }

    protected void setControlsVisible(Boolean visible) {
        if (areChoiceBoxShowing()) {
            return;
        }
        if (visible) {
            animationTimeline.setCycleCount(2);
            animationTimeline.setAutoReverse(true);
            animationTimeline.playFrom(new Duration(500));
        } else {
            animationTimeline.setCycleCount(1);
            animationTimeline.play();
        }
    }

    @Override
    public void dispose() {
    }

    @Override
    public Node getNode() {
        return root;
    }

    @Override
    public MapView getSkinnable() {
        return control;
    }

    protected WebEngine getWebEngine() {
        return webEngine;
    }

    protected Slider getZoomSlider() {
        return zoomSlider;
    }

    protected TextField getLocationTextField() {
        return locationTextField;
    }

    protected ChoiceBox<MapType> getMapTypeChoiceBox() {
        return mapTypeChoiceBox;
    }

    protected ChoiceBox<MapProvider> getMapProviderChoiceBox() {
        return mapProviderChoiceBox;
    }
}
