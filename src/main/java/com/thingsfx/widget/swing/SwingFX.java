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

import javax.swing.RepaintManager;

/**
 * Helper class for {@link SwingView}. The purpouse of this class is to
 * initialize the special {@link RepaintManager} needed to hook Swing
 * components into JavaFX views.
 */
public class SwingFX {
   
    /**
     * Initializes and install the {@link RepaintManager} needed
     * by {@link SwingView}.
     */
    public static void init() {
        System.setProperty("swing.volatileImageBufferEnabled", "false");
        RepaintManager.setCurrentManager(new ThingsFXRepaintManager());
    }
}
