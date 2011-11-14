package com.thingsfx.widget.map;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.scene.control.Control;
import javafx.scene.web.WebEngine;

public class MapView extends Control implements MapViewer {

    private WebEngine webEngine;
    private MapProvider provider;
    private MapType type;
    private MapViewSkin skin;

    private List<Runnable> pendingScripts = new ArrayList<Runnable>();

    // -----------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------

    public MapView() {
        this(MapProvider.GOOGLE);
    }

    public MapView(final MapProvider provider) {
        this(provider, "Barcelona", 0.1);
    }

    public MapView(final String location, final double zoomLevel) {
        this(MapProvider.GOOGLE, location, zoomLevel);
    }

    public MapView(final MapProvider provider, final String location, final double zoomLevel) {
        skin = new MapViewSkin(this);
        this.setSkin(skin);
        webEngine = skin.getWebEngine();
        setProvider(provider);
        webEngine.getLoadWorker().workDoneProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> value, Number oldValue, Number newValue) {
                if (newValue.doubleValue() >= 100.0) {
                    processPendingScripts();
                }
            }
        });
        setLocation(location);
        setZoom(zoomLevel);
    }


    // -----------------------------------------------------------
    // MapViewer API Methods
    // -----------------------------------------------------------

    @Override
    public void setLocation(final String location) {
        queueScript("document.goToLocation(\"" + location + "\")");
    }

    @Override
    public void setProvider(MapProvider provider) {
        this.provider = provider;
        webEngine.load(getClass().getResource("html/" + MapView.this.provider.toString() + "map.html").toString());
    }

    @Override
    public MapProvider getProvider() {
        return provider;
    }

    @Override
    public void setType(final MapType type) {
        this.type = type;
        queueScript("document.setMapType(\"" + type.toString() + "\")");
    }

    @Override
    public MapType getType() {
        return type;
    }

    @Override
    public void reload() {
        webEngine.reload();
    }

    @Override
    public void zoomIn() {
        queueScript("document.zoomIn()");
    }

    @Override
    public void zoomOut() {
        queueScript("document.zoomOut()");
    }

    @Override
    public void setZoom(double zoomLevel) {
        double correctedZoomLevel = zoomLevel;
        if (zoomLevel < 0) {
            correctedZoomLevel = 0.0;
        } else if (zoomLevel > 1) {
            correctedZoomLevel = 1.0;
        }
        queueScript("document.setZoom(" + correctedZoomLevel + ")");
    }

    // -----------------------------------------------------------
    // Script Queuing
    // -----------------------------------------------------------

    private void queueScript(final String script) {
        pendingScripts.add(new Runnable() {

            @Override
            public void run() {
                webEngine.executeScript(script);
            }
        });
        processPendingScripts();
    }

    private void processPendingScripts() {
        if (webEngine.getLoadWorker().getState() != State.SUCCEEDED) {
            return;
        }
        for (Runnable script : pendingScripts) {
            script.run();
        }
        pendingScripts.clear();
    }
}
