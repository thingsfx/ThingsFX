package com.thingsfx.widget.map;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;

public class MapView extends Control implements MapViewer {

    private WebEngine webEngine;
    private MapViewSkin skin;

    private DoubleProperty zoomLevelProperty = new SimpleDoubleProperty();
    private StringProperty locationProperty = new SimpleStringProperty();
    private ObjectProperty<MapType> mapTypProperty = new SimpleObjectProperty<MapViewer.MapType>();
    private ObjectProperty<MapProvider> mapProviderProperty = new SimpleObjectProperty<MapViewer.MapProvider>();
    // private BooleanProperty showControlsProperty = new SimpleBooleanProperty(false);

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
        webEngine.getLoadWorker().workDoneProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> value, Number oldValue, Number newValue) {
                if (newValue.doubleValue() >= 100.0) {
                    if (webEngine.getLoadWorker().getProgress() > 0) {
                        processPendingScripts();
                    }
                }
            }
        });

        zoomLevelProperty.bindBidirectional(skin.getZoomSlider().valueProperty());
        zoomLevelProperty.addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                double correctedZoomLevel = (Double) arg2;
                if (zoomLevel < 0) {
                    correctedZoomLevel = 0.0;
                } else if (zoomLevel > 1) {
                    correctedZoomLevel = 1.0;
                }
                queueScript("document.setZoom(" + correctedZoomLevel + ")");
            }

        });

        locationProperty.bindBidirectional(skin.getLocationTextField().textProperty());
        locationProperty.addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
                if (arg2.length() > 0) {
                    queueScript("document.goToLocation(\"" + arg2 + "\")");
                }
            }

        });

        mapTypProperty.bind(skin.getMapTypeChoiceBox().getSelectionModel().selectedItemProperty());
        mapTypProperty.addListener(new ChangeListener<MapType>() {

            @Override
            public void changed(ObservableValue<? extends MapType> arg0, MapType arg1, MapType arg2) {
                queueScript("document.setMapType(\"" + arg2.toString() + "\")");
            }
        });

        mapProviderProperty.bind(skin.getMapProviderChoiceBox().getSelectionModel().selectedItemProperty());
        mapProviderProperty.addListener(new ChangeListener<MapProvider>() {

            @Override
            public void changed(ObservableValue<? extends MapProvider> arg0, MapProvider arg1, MapProvider arg2) {
                webEngine.load(getClass().getResource("html/" + arg2.toString() + "map.html").toString());
                setZoom(getZoom()); // TODO: not yet working since the selection doesn't change, and thus the event is not fired
                setType(getType()); // TODO: not yet working since the selection doesn't change, and thus the event is not fired
                // setLocation(getLocation()); // TODO: will not work yet with others than Google
            }
        });

        final EventHandler<MouseEvent> mouseEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                final Boolean mouseEntered = mouseEvent.getEventType().equals(MouseEvent.MOUSE_ENTERED);
                skin.setControlsVisible(mouseEntered);
            }
        };
        skin.getNode().setOnMouseExited(mouseEventHandler);
        skin.getNode().setOnMouseEntered(mouseEventHandler);

        webEngine.getLoadWorker().workDoneProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                skin.getProgressIndicator().progressProperty().set(arg2.doubleValue() / webEngine.getLoadWorker().getTotalWork());
            }
        });

        skin.getProgressIndicator().progressProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                skin.getProgressIndicator().setVisible(arg2.doubleValue() < 1.0);
            }
        });

        setProvider(provider);
        setLocation(location);
        setZoom(zoomLevel);
        setType(MapType.ROAD);
    }


    // -----------------------------------------------------------
    // MapViewer API Methods
    // -----------------------------------------------------------

    @Override
    public void setLocation(final String location) {
        locationProperty.set(location);
    }

    @Override
    public void setProvider(MapProvider provider) {
        // mapProviderProperty.set(provider);
        skin.getMapProviderChoiceBox().getSelectionModel().select(provider);
        // TODO:
        // skin.getMapProviderChoiceBox().getSelectionModel().selectedItemProperty()
        // is ReadOnly i.e. it cannot be bound
    }

    @Override
    public MapProvider getProvider() {
        return mapProviderProperty.get();
    }

    @Override
    public void setType(final MapType type) {
        // mapTypProperty.set(type);
        skin.getMapTypeChoiceBox().getSelectionModel().select(type);
        // TODO:
        // skin.getMapTypeChoiceBox().getSelectionModel().selectedItemProperty()
        // is ReadOnly i.e. it cannot be bound
    }

    @Override
    public MapType getType() {
        return mapTypProperty.get();
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
        zoomLevelProperty.set(zoomLevel);
    }

    @Override
    public double getZoom() {
        return zoomLevelProperty.get();
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
