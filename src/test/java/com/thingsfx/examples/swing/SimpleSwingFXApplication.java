package com.thingsfx.examples.swing;

import java.awt.Dimension;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.thingsfx.widget.swing.SwingFX;
import com.thingsfx.widget.swing.SwingView;

public class SimpleSwingFXApplication {
    
    public static void main(String[] args) {
        
        SwingFX.init();
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                try {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                SimpleSwingFXApplication login = new SimpleSwingFXApplication();
                login.initAndShowGUI();
            }
        });
    }

    protected void initAndShowGUI() {
        JFrame frame = new JFrame("SimpleSwingFXApplication");
        
        frame.setMinimumSize(new Dimension(300, 100));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final JFXPanel fxPanel = new JFXPanel();
        frame.add(fxPanel);
        frame.setVisible(true);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(fxPanel);
            }

       });
    }
    
    private void initFX(JFXPanel fxPanel) {
        Group root = new Group();
        Scene scene = new Scene(root, 200, 50, Color.BLACK);
        fxPanel.setScene(scene);
        
        final HBox hbox = new HBox();
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.setSpacing(10);

        Button fxButton = new Button("FX Button");
        fxButton.setPrefSize(100, 20);

        hbox.getChildren().addAll(fxButton);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final JButton jbutton = new JButton("JButton");
                jbutton.setPreferredSize(new Dimension(100, 20));
                jbutton.setSize(new Dimension(100, 20));
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        SwingView swingViewButton = new SwingView(jbutton);
                        hbox.getChildren().add(swingViewButton);
                    }
                });
            }
        });
        
        root.getChildren().add(hbox);   
    }
}
