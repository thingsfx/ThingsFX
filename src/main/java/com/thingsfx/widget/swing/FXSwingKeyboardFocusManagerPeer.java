package com.thingsfx.widget.swing;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.peer.KeyboardFocusManagerPeer;
import java.util.logging.Logger;

import sun.awt.AppContext;
import sun.awt.CausedFocusEvent;
import sun.awt.CausedFocusEvent.Cause;
import sun.awt.SunToolkit;

class FXSwingKeyboardFocusManagerPeer implements KeyboardFocusManagerPeer {

    private static Logger log = Logger.getLogger(FXSwingKeyboardFocusManagerPeer.class.getName());

    private static FXSwingKeyboardFocusManagerPeer instance;

    static FXSwingKeyboardFocusManagerPeer getInstance() {
        if (instance == null) {
            instance = new FXSwingKeyboardFocusManagerPeer();
        }
        return instance;
    }

    private FXSwingKeyboardFocusManagerPeer() {
    }

    private Window currentFocusedWindow;
    private Component currentFocusOwner;

    /**
     * Returns the currently focused window.
     *
     * @return the currently focused window
     *
     * @see KeyboardFocusManager#getNativeFocusedWindow()
     */
    @Override
    public Window getCurrentFocusedWindow() {
        return currentFocusedWindow;
    }

    /**
     * Sets the component that should become the focus owner.
     *
     * @param comp the component to become the focus owner
     *
     * @see KeyboardFocusManager#setNativeFocusOwner(Component)
     */
    @Override
    public void setCurrentFocusOwner(Component comp) {
        currentFocusOwner = comp;
        if (currentFocusOwner instanceof ProxyWindow) {
            ((ProxyWindow) currentFocusOwner).getProxyView().requestFocus();
        }
    }

    /**
     * Returns the component that currently owns the input focus.
     *
     * @return the component that currently owns the input focus
     *
     * @see KeyboardFocusManager#getNativeFocusOwner()
     */
    @Override
    public Component getCurrentFocusOwner() {
        return currentFocusOwner;
    }

    /**
     * Clears the current global focus owner.
     *
     * @param activeWindow
     *
     * @see KeyboardFocusManager#clearGlobalFocusOwner()
     */
    @Override
    public void clearGlobalFocusOwner(Window activeWindow) {
        // TODO: Implement.
    }

    public void setCurrentFocusedWindow(Window w) {
        currentFocusedWindow = w;
        if (currentFocusedWindow instanceof ProxyWindow) {
            ((ProxyWindow) currentFocusedWindow).getProxyView().requestFocus();
        }
    }

    boolean requestFocus(Component target, Component lightweightChild, boolean temporary,
                         boolean focusedWindowChangeAllowed, long time, Cause cause) {

        if (target instanceof ProxyWindow) {
            ((ProxyWindow) target).getProxyView().requestFocus();
        } else {
            log.warning("Cannot handle non-ProxyWindow component");
        }
        return false;
    }

    private void postEvent(AWTEvent ev) {
        SunToolkit.postEvent(AppContext.getAppContext(), ev);
    }

    void focusGained(ProxyWindow w) {
        log.fine("Focus gained: " + w);
        FocusEvent fg = new CausedFocusEvent(w, FocusEvent.FOCUS_GAINED, false, currentFocusOwner, Cause.NATIVE_SYSTEM);
        postEvent(fg);
    }

    void focusLost(ProxyWindow w) {
        log.fine("Focus lost: " + w);
        FocusEvent fl = new CausedFocusEvent(w, FocusEvent.FOCUS_LOST, false, null, Cause.NATIVE_SYSTEM);
        postEvent(fl);
    }

}
