package com.thingsfx.widget.map;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Skin;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;

import com.sun.javafx.geom.Dimension2D;
import com.thingsfx.widget.map.MapViewer.MapProvider;
import com.thingsfx.widget.map.MapViewer.MapType;
import com.thingsfx.widget.text.ResetableTextField;

class MapViewSkin implements Skin<MapView> {

    private static final Dimension2D PROGESS_BAR_DIMENSION = new Dimension2D(150, 18);
    private WebView webView;
    private WebEngine webEngine;
    private MapView control;
    private ProgressBar progressBar;
    private TextField locationTextField;
    private ChoiceBox<MapProvider> mapProviderChoiceBox;
    private BorderPane topPanel;
    private Slider zoomSlider;
    private ChoiceBox<MapType> mapTypeChoiceBox;
    private BorderPane leftPanel;
    private BorderPane bottomPanel;
    private BorderPane root;
    private AnchorPane anchorPane;
    private Timeline animationTimeline;

    public MapViewSkin(MapView control) {
        this.control = control;

        webView = new WebView();
        webView.setMouseTransparent(true);
        webEngine = webView.getEngine();

        locationTextField = new ResetableTextField();
        locationTextField.setPromptText("Location");

        mapProviderChoiceBox = new ChoiceBox<MapViewer.MapProvider>();
        mapProviderChoiceBox.getItems().addAll(MapProvider.values());

        topPanel = new BorderPane();
        topPanel.setLeft(locationTextField);
        topPanel.setRight(mapProviderChoiceBox);

        zoomSlider = new Slider();
        zoomSlider.setOrientation(Orientation.VERTICAL);
        zoomSlider.setMin(0);
        zoomSlider.setMax(1);

        mapTypeChoiceBox = new ChoiceBox<MapViewer.MapType>();
        mapTypeChoiceBox.getItems().addAll(MapType.values());

        progressBar = new ProgressBar();
        progressBar.setPrefSize(PROGESS_BAR_DIMENSION.width, PROGESS_BAR_DIMENSION.height);

        bottomPanel = new BorderPane();
        bottomPanel.setRight(mapTypeChoiceBox);

        leftPanel = new BorderPane();
        leftPanel.setCenter(zoomSlider);

        anchorPane = new AnchorPane();

        anchorPane.getChildren().addAll(webView, topPanel, leftPanel, bottomPanel, progressBar);

        AnchorPane.setTopAnchor(topPanel, 10.0);
        AnchorPane.setRightAnchor(topPanel, 10.0);
        AnchorPane.setLeftAnchor(topPanel, 10.0);

        AnchorPane.setTopAnchor(webView, 0.0);
        AnchorPane.setLeftAnchor(webView, 0.0);
        AnchorPane.setBottomAnchor(webView, 0.0);
        AnchorPane.setRightAnchor(webView, 0.0);

        AnchorPane.setTopAnchor(leftPanel, 50.0);
        AnchorPane.setLeftAnchor(leftPanel, 10.0);
        AnchorPane.setBottomAnchor(leftPanel, 50.0);

        AnchorPane.setLeftAnchor(progressBar, 10.0);
        AnchorPane.setBottomAnchor(progressBar, 10.0);

        AnchorPane.setBottomAnchor(bottomPanel, 10.0);
        AnchorPane.setRightAnchor(bottomPanel, 10.0);
        AnchorPane.setLeftAnchor(bottomPanel, 10.0);

        root = new BorderPane();
        root.setCenter(anchorPane);

        animationTimeline = new Timeline(500);
        animationTimeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(progressBar.opacityProperty(), 0.9, Interpolator.EASE_BOTH),
                        new KeyValue(topPanel.opacityProperty(), 0.9, Interpolator.EASE_BOTH),
                        new KeyValue(leftPanel.opacityProperty(), 0.9, Interpolator.EASE_BOTH),
                        new KeyValue(bottomPanel.opacityProperty(), 0.9, Interpolator.EASE_BOTH)

                ),
                new KeyFrame(new Duration(500),
                        new KeyValue(progressBar.opacityProperty(), 0.2, Interpolator.EASE_BOTH),
                        new KeyValue(topPanel.opacityProperty(), 0.2, Interpolator.EASE_BOTH),
                        new KeyValue(leftPanel.opacityProperty(), 0.2, Interpolator.EASE_BOTH),
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
        animationTimeline.stop();
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

    protected ProgressIndicator getProgressIndicator() {
        return progressBar;
    }
}
