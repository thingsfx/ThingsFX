package com.thingsfx.widget.map;

import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Skin;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

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

        root = new BorderPane();
        root.setCenter(webView);

    }

    private boolean areChoiceBoxShowing() {
        return getMapProviderChoiceBox().isShowing() || getMapTypeChoiceBox().isShowing();
    }

    protected void setControlsVisible(Boolean visible) {
        if (areChoiceBoxShowing()) {
            return;
        }
        root.setTop(visible ? topPanel : null);
        root.setBottom(visible ? bottomPanel : null);
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
