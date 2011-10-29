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
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;

import javafx.event.EventHandler;

import javax.swing.JButton;
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
        
        registerEvents();

        this.component = component;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SwingView.this.component.addNotify();
                SwingView.this.component.setVisible(true);
                SwingView.this.component.setDoubleBuffered(true);
                SwingView.this.component.repaint();
            }
        });
    }

    private void registerEvents() {
        // ah, I can do infinite nesting here :)
        setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(final javafx.scene.input.MouseEvent event) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        System.err.println("###################");
                        if (SwingView.this.component instanceof JButton) {
                            JButton jbutton = (JButton) component;
                            jbutton.doClick();
                        }

//                        EventQueue eventQueue =
//                                Toolkit.getDefaultToolkit().getSystemEventQueue();
//                        MouseEvent awtEvent = new MouseEvent(component,
//                                                             MouseEvent.MOUSE_CLICKED,
//                                                             System.currentTimeMillis(),
//                                                             MouseEvent.BUTTON1_DOWN_MASK,
//                                                             (int) event.getSceneX(),
//                                                             (int) event.getSceneY(),
//                                                             event.getClickCount(), false);
//                        eventQueue.postEvent(awtEvent);
                        //component.invalidate();
                        //component.repaint();
                    }
                });
            }
        });
    }
}
