/*
 * This file is part of ThingsFX.
 *
 * ThingsFX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ThingsFX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ThingsFX. If not, see <http://www.gnu.org/licenses/>.
 */

package com.thingsfx.widget.swing;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javafx.event.EventHandler;

import javax.swing.JComponent;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;

public class SwingView extends BufferedImageView {
    
    private JComponent component;
    public SwingView(JComponent component) {
        
        RepaintManager repaintManager = RepaintManager.currentManager(component);
        if (!(repaintManager instanceof ThingsFXRepaintManager)) {
            throw new IllegalStateException("SwingFX.init() method must be " +
                                            "called before any Swing rendering" +
                                            "and any JavaFX routine call");
        }
        
        ((ThingsFXRepaintManager) repaintManager).registerListener(this, component);
        
        this.component = component;
        
        registerEvents();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                JComponent component = SwingView.this.component;
                component.addNotify();
                
                
                SwingFXEventDispatcher.setLightweightDispatcher(component);                
                component.setVisible(true);
                component.setDoubleBuffered(true);
                component.doLayout();
                component.repaint();
                
                ProxyWindow proxy = new ProxyWindow(SwingView.this);
                proxy.add(component);
            }
        });
    }
    
    private void registerEvents() {
        // ah, I can do infinite nesting here :)
        // TODO: factor those methods out into a single one
        setOnMousePressed(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(final javafx.scene.input.MouseEvent event) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {

                        MouseEvent awtEvent =
                                new MouseEvent(component,
                                               MouseEvent.MOUSE_PRESSED,
                                               System.currentTimeMillis(),
                                               MouseEvent.BUTTON1_MASK,
                                               (int) event.getX(),
                                               (int) event.getY(),
                                               (int) event.getScreenX(),
                                               (int) event.getScreenY(),
                                               event.getClickCount(), false,
                                               MouseEvent.BUTTON1);
                        SwingFXEventDispatcher.dispatchEvent(awtEvent, component);
                    }
                });
            }
        });
        
        setOnMouseReleased(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(final javafx.scene.input.MouseEvent event) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        
                        MouseEvent awtEvent =
                                new MouseEvent(component,
                                               MouseEvent.MOUSE_RELEASED,
                                               System.currentTimeMillis(),
                                               MouseEvent.BUTTON1_MASK,
                                               (int) event.getX(),
                                               (int) event.getY(),
                                               (int) event.getScreenX(),
                                               (int) event.getScreenY(),
                                               event.getClickCount(), false,
                                               MouseEvent.BUTTON1);
                        SwingFXEventDispatcher.dispatchEvent(awtEvent, component);
                    }
                });
            }
        }); 
    }

    Component getComponent() {
        return component;
    }
}
