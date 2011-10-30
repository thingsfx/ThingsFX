package com.thingsfx.widget.swing;

import java.awt.event.MouseEvent;
import java.lang.reflect.Method;

import javax.swing.JComponent;

public class SwingFXEventDispatcher {
    
    private static Method dispatchMethod;

    // grab this idea and the following two methods from CacioCavallo
    static void initReflection() {
        try {
            Class<?> dispatcherCls = Class.forName("java.awt.Component");
            dispatchMethod =
                    dispatcherCls.getDeclaredMethod("processMouseEvent",
                                                    new Class[] {MouseEvent.class});
            dispatchMethod.setAccessible(true);
        
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
    static void dispatchMouseEvent(MouseEvent e, JComponent component) {
        if (dispatchMethod == null) {
            initReflection();
        }
        try {
            dispatchMethod.invoke(component, e);
            
        } catch (Exception ex) {
            InternalError err = new InternalError();
            err.initCause(ex);
            throw err;
        }
    }
}
