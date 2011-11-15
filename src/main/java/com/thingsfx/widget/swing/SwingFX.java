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

import java.awt.KeyboardFocusManager;
import java.lang.reflect.Field;

import javafx.embed.swing.JFXPanel;

import javax.swing.RepaintManager;

/**
 * Helper class for {@link SwingView}. The purpouse of this class is to
 * initialize the special {@link RepaintManager} needed to hook Swing components
 * into JavaFX views.
 */
public class SwingFX {

    /**
     * Initializes and install the {@link RepaintManager} needed by
     * {@link SwingView}.
     */
    public static void init() {
        new JFXPanel(); // needed as a trick to launch it on a mac
        Class<KeyboardFocusManager> kfmCls = KeyboardFocusManager.class;
        Field peer;
        try {
            peer = kfmCls.getDeclaredField("peer");
            peer.setAccessible(true);
            peer.set(KeyboardFocusManager.getCurrentKeyboardFocusManager(),
                     FXSwingKeyboardFocusManagerPeer.getInstance());
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}