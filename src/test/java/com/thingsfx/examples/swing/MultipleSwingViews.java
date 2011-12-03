package com.thingsfx.examples.swing;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import com.thingsfx.widget.swing.SwingFX;
import com.thingsfx.widget.swing.SwingView;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MultipleSwingViews extends Application {
    
    public static void main(String[] args) {
        SwingFX.init();
        launch(args);
    }
    
    public void start(Stage stage) {        
        initFX(stage);
        stage.show();
    }

    private void initFX(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root, 300, 300, Color.BLACK);
        stage.setScene(scene);
        
        final VBox hbox = new VBox();
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.setSpacing(10);

        Button fxButton = new Button("FX Button");
        fxButton.setPrefSize(100, 20);
        hbox.getChildren().addAll(fxButton);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                final JPanel swingPane1 = new JPanel();
                swingPane1.setLayout(new GridLayout(1, 1));
                
                final JPanel swingPane2 = new JPanel();
                swingPane2.setLayout(new GridLayout(1, 1));
                
                JTextField textField1 = new JTextField();
                textField1.setMinimumSize(new Dimension(100, 30));
                textField1.setPreferredSize(new Dimension(100, 30));
                textField1.setSize(new Dimension(100, 30));
                textField1.setText("I lost focus!");

                JScrollPane scrollPane = new JScrollPane();
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                
                JTextArea textArea = new JTextArea();
                textArea.setText("I have focus!");
                scrollPane.setViewportView(textArea);

                swingPane1.add(textField1);                
                swingPane2.add(scrollPane);
                
                swingPane1.setSize(swingPane1.getPreferredSize());
                swingPane2.setSize(new Dimension(200, 200));
                
                swingPane1.setVisible(true);
                swingPane2.setVisible(true);
                
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        SwingView swingView1 = new SwingView(swingPane1);
                        SwingView swingView2 = new SwingView(swingPane2);
                        hbox.getChildren().addAll(swingView1, swingView2);
                    }
                });
            }
        });
        
        root.getChildren().add(hbox);
    }    
}
