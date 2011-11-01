package com.thingsfx.examples.swing;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
//import javax.swing.UIManager;

import com.thingsfx.widget.swing.SwingFX;
import com.thingsfx.widget.swing.SwingView;

public class SimpleSwingFXApplication {
    
    public static void main(String[] args) {
        
        SwingFX.init();
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                
//                try {
//                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                
                SimpleSwingFXApplication login = new SimpleSwingFXApplication();
                login.initAndShowGUI();
            }
        });
    }

    protected void initAndShowGUI() {
        JFrame frame = new JFrame("SimpleSwingFXApplication");
        
        frame.setMinimumSize(new Dimension(300, 300));
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
        Scene scene = new Scene(root, 300, 300, Color.BLACK);
        fxPanel.setScene(scene);
        
        final VBox hbox = new VBox();
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.setSpacing(10);

        Button fxButton = new Button("FX Button");
        fxButton.setPrefSize(100, 20);

        hbox.getChildren().addAll(fxButton);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                final JPanel swingPane = new JPanel();
                swingPane.setLayout(new GridLayout(3, 1));
                
                final JButton jbutton1 = new JButton("JButton1");
                jbutton1.setMinimumSize(new Dimension(100, 30));
                jbutton1.setPreferredSize(new Dimension(100, 30));
                jbutton1.setSize(new Dimension(100, 30));
                
                JButton jbutton2 = new JButton("JButton2");
                jbutton2.setMinimumSize(new Dimension(100, 30));
                jbutton2.setPreferredSize(new Dimension(100, 30));
                jbutton2.setSize(new Dimension(100, 30));
                
                JTextField textField = new JTextField();
                textField.setMinimumSize(new Dimension(100, 30));
                textField.setPreferredSize(new Dimension(100, 30));
                textField.setSize(new Dimension(100, 30));
                
                swingPane.add(jbutton1);
                swingPane.add(jbutton2);
                swingPane.add(textField);
                
                swingPane.setSize(swingPane.getPreferredSize());
                
                swingPane.setVisible(true);
                
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        SwingView swingViewButton = new SwingView(swingPane);
                        hbox.getChildren().add(swingViewButton);
                    }
                });
            }
        });
        
        root.getChildren().add(hbox);   
    }
}
