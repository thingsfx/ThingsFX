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
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.swing.JComponent;
import javax.swing.RepaintManager;

class ThingsFXRepaintManager extends RepaintManager {

    private ConcurrentMap<JComponent, SwingView> listeners =
            new ConcurrentHashMap<JComponent, SwingView>();
            
    private WeakHashMap<Component, BufferedImage> map =
            new WeakHashMap<Component, BufferedImage>();

    private Set<Container> trackedComponents = new CopyOnWriteArraySet<Container>();

    public ThingsFXRepaintManager() {
        setDoubleBufferingEnabled(true);
    }

    @Override
    public void addDirtyRegion(JComponent c, int x, int y, int w, int h) {
        
        super.addDirtyRegion(c, x, y, w, h);
        Container container = c;
        boolean found = false;
        do {
            found = rapaintHierarchy(container);
        } while (!found && (container = container.getParent()) != null);
    }
    
    private boolean rapaintHierarchy(Container c) {        
        boolean found = listeners.containsKey(c);
        if (found) {
            trackedComponents.add(c);
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    paintDirtyRegions();
                }
            });
        }
        return found;
    }
    
    @Override
    public void paintDirtyRegions() {
        
        super.paintDirtyRegions();
        
        for (Container component : trackedComponents) {
            if (component.isDoubleBuffered() && listeners.containsKey(component)) {
                Image backBuffer = getBuffer(component, component.getWidth(),
                                             component.getHeight());
                Graphics g = backBuffer.getGraphics();
                component.paint(g);
                g.dispose();
                
                listeners.get(component).getImageView().paintImage((BufferedImage) backBuffer);
            }
        }
        trackedComponents.clear();
    }
    
    private Image getBuffer(Component c, int proposedWidth, int proposedHeight) {
       
        // TODO: check the size of the component as well
        if (!map.containsKey(c)) {
            BufferedImage buffer = new BufferedImage(proposedWidth, proposedHeight,
                                                     BufferedImage.TYPE_INT_ARGB);
            map.put(c, buffer);
        }
        return map.get(c);
    }

    void registerListener(SwingView view, JComponent component) {
        this.listeners.put(component, view);
    }
}
