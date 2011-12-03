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
                FXSwingKeyboardFocusManagerPeer peer =
                        FXSwingKeyboardFocusManagerPeer.getInstance();
                if (newValue) {
                    peer.focusGained(ProxyWindow.this);
                } else {
                    peer.focusLost(ProxyWindow.this);
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
