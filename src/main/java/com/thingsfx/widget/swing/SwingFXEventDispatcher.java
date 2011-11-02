package com.thingsfx.widget.swing;

import java.awt.AWTEvent;
import java.awt.Container;
import java.awt.event.MouseEvent;
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

    private static Method processMouseEventMethod;

    // grab this idea and the following two methods from CacioCavallo
    static void initReflection() {
        try {
            Class<?> componentCls = Class.forName("java.awt.Component");
            processMouseEventMethod =
                    componentCls.getDeclaredMethod("processMouseEvent",
                                                    new Class[] {MouseEvent.class});
            processMouseEventMethod.setAccessible(true);
            
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
            enableEvents.invoke(dispatcher, MOUSE_MASK);
            dispatcherField.set(component, dispatcher);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static void dispatchMouseEvent(MouseEvent e, JComponent component) {
        if (processMouseEventMethod == null) {
            initReflection();
        }
        try {
            processMouseEventMethod.invoke(component, e);
            
        } catch (Exception ex) {
            InternalError err = new InternalError();
            err.initCause(ex);
            throw err;
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
    public static void dispatchEvent(AWTEvent awtEvent, JComponent component) {

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
