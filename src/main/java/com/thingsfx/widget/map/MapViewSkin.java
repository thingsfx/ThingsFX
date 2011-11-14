package com.thingsfx.widget.map;

import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

class MapViewSkin implements Skin<MapView> {

    private WebView webView;
    private WebEngine webEngine;
    private MapView control;
    private BorderPane root;

    public MapViewSkin(MapView control) {
        this.control = control;
        webView = new WebView();
        webEngine = webView.getEngine();
        root = new BorderPane();
        root.setCenter(webView);
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

}
