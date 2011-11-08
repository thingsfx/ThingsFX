package com.thingsfx.widget.swing;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.peer.KeyboardFocusManagerPeer;

import sun.awt.AppContext;
import sun.awt.CausedFocusEvent;
import sun.awt.CausedFocusEvent.Cause;
import sun.awt.SunToolkit;

class FXSwingKeyboardFocusManagerPeer implements KeyboardFocusManagerPeer {

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

        if (lightweightChild == null) {
            lightweightChild = target;
        }
        Component currentOwner = getCurrentFocusOwner();
        if (currentOwner != null && currentOwner.getPeer() == null) {
            currentOwner = null;
        }
        FocusEvent fg = new CausedFocusEvent(lightweightChild, FocusEvent.FOCUS_GAINED, false, currentOwner, cause);
        FocusEvent fl = null;
        if (currentOwner != null) {
            fl = new CausedFocusEvent(currentOwner, FocusEvent.FOCUS_LOST, false, lightweightChild, cause);
        }

        if (fl != null) {
            postEvent(fl);
        }
        postEvent(fg);
        return true;

    }

    private void postEvent(AWTEvent ev) {
        SunToolkit.postEvent(AppContext.getAppContext(), ev);
    }

}
