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

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;

/**
 * An {@link ImageView} that is able to render {@link BufferedImage}s.
 */
public class BufferedImageView extends ImageView {
    
    public void paintImage(BufferedImage backBuffer) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(backBuffer, "png", os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            setImage(new Image(is));
            
            is.close();
            os.close();
            
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
