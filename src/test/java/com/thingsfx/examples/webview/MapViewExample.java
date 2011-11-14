package com.thingsfx.examples.webview;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.thingsfx.widget.map.MapView;

public class MapViewExample extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        MapView mapView = new MapView("Barcelona", 0.5);
        Scene scene = new Scene(mapView, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

}
