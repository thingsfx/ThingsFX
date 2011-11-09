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

import java.awt.AWTEvent;
import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.JComponent;

public class SwingFXEventDispatcher {
    
    private static final long MOUSE_MASK = AWTEvent.MOUSE_EVENT_MASK |
                                           AWTEvent.MOUSE_MOTION_EVENT_MASK |
                                           AWTEvent.MOUSE_WHEEL_EVENT_MASK;
    
    private static Field dispatcherField;
    private static Constructor<?> newLightweightDispatcher;
    private static Method dispatchMethod;
    private static Method enableEvents;

    // grab this idea from CacioCavallo
    static void initReflection() {
        try {
            
            // lightweight dispatcher
            dispatcherField = Container.class.getDeclaredField("dispatcher");
            dispatcherField.setAccessible(true);
            
            Class<?> dispatcherCls = Class.forName("java.awt.LightweightDispatcher");
            newLightweightDispatcher =
                    dispatcherCls.getDeclaredConstructor(new Class[] { Container.class });
            newLightweightDispatcher.setAccessible(true);
            
            dispatchMethod = dispatcherCls.getDeclaredMethod("dispatchEvent", AWTEvent.class);
            dispatchMethod.setAccessible(true);
            
            enableEvents =
                    dispatcherCls.getDeclaredMethod("enableEvents", new Class[] { long.class });
            enableEvents.setAccessible(true);
            
        } catch (Exception ex) {
            
            System.err.println(ex);
            
            InternalError err = new InternalError();
            err.initCause(ex);
            throw err;
        }
    }
 
    static void setLightweightDispatcher(JComponent component) {
        if (dispatcherField == null) {
            initReflection();
        }
        try {
            Object dispatcher = newLightweightDispatcher.newInstance(component);
            enableEvents.invoke(dispatcher, MOUSE_MASK | AWTEvent.KEY_EVENT_MASK);
            dispatcherField.set(component, dispatcher);
            component.addKeyListener(new KeyAdapter(){});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Performs lightweight dispatching for the specified event on this window.
     * This only calls the lightweight dispatcher. We cannot simply
     * call dispatchEvent() because that would also send the event to the
     * Toolkit dispatching mechanism (AWTEventListener, etc), which has ugly
     * side effects, like popups closing too early.
     *
     * @param e the event to be dispatched
     */
    static void dispatchEvent(AWTEvent awtEvent, JComponent component) {

            if (dispatcherField == null) {
                initReflection();
            }
            try {
                Object dispatcher = dispatcherField.get(component);
                if (dispatcher != null) {
                    dispatchMethod.invoke(dispatcher, awtEvent);
                }
            } catch (Exception ex) {
                InternalError err = new InternalError();
                err.initCause(ex);
                throw err;
            }


    }
}
