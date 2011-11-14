package com.thingsfx.widget.swing;

import java.awt.Component;
import java.awt.Frame;
import java.lang.reflect.Field;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

class ProxyWindow extends Frame {

    private static Field peer;
    
    private static void initReflection() {
        try {
            peer = Component.class.getDeclaredField("peer");
            peer.setAccessible(true);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static {
        initReflection();
    }
    
    private SwingView proxyView;
    
    ProxyWindow(SwingView proxyView) {
        this.proxyView = proxyView;
        
        try {
            peer.set(this, new ProxyWindowPeer(this));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        proxyView.focusedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> obs,
                                Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    FXSwingKeyboardFocusManagerPeer.getInstance().focusGained(ProxyWindow.this);
                } else {
                    FXSwingKeyboardFocusManagerPeer.getInstance().focusLost(ProxyWindow.this);
                }
            }
        });
    }

    @Override
    public boolean isVisible() {
        return proxyView.isVisible();
    }
    @Override
    public boolean isShowing() {
        return proxyView.isVisible();
    }

    @Override
    public boolean isFocusTraversable() {
        return proxyView.isFocusTraversable();
    }

    SwingView getProxyView() {
        return proxyView;
    }
}
