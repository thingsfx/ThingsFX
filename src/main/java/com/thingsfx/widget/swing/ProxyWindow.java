package com.thingsfx.widget.swing;

import java.awt.Component;
import java.awt.Frame;
import java.lang.reflect.Field;

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
}
