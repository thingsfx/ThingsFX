package com.thingsfx.widget.swing;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.BufferCapabilities;
import java.awt.BufferCapabilities.FlipContents;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MenuBar;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.PaintEvent;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.VolatileImage;
import java.awt.peer.ComponentPeer;
import java.awt.peer.ContainerPeer;
import java.awt.peer.FramePeer;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;
import sun.awt.CausedFocusEvent.Cause;
import sun.java2d.pipe.Region;

class ProxyWindowPeer implements FramePeer {

    private ProxyWindow window;

    ProxyWindowPeer(ProxyWindow w) {
        window = w;
    }

    @Override
    public void toFront() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void toBack() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAlwaysOnTop(boolean alwaysOnTop) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateFocusableWindowState() {
        throw new UnsupportedOperationException();
    }

    // @Override No longer present in JDK6
    public boolean requestWindowFocus() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setModalBlocked(Dialog blocker, boolean blocked) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateMinimumSize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateIconImages() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setOpacity(float opacity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setOpaque(boolean isOpaque) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateWindow() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void repositionSecurityWarning() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Insets getInsets() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void beginValidate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void endValidate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void beginLayout() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void endLayout() {
        throw new UnsupportedOperationException();
    }

    // @Override Not present in JDK7
    public boolean isPaintPending() {
        throw new UnsupportedOperationException();
    }

    // @Override Not present in JDK7
    public void restack() {
        throw new UnsupportedOperationException();
    }

    // @Override Not present in JDK7
    public boolean isRestackSupported() {
        return false;
    }

    // @Override Not present in JDK7
    public Insets insets() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isObscured() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean canDetermineObscurity() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setVisible(boolean b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setEnabled(boolean b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void paint(Graphics g) {
        throw new UnsupportedOperationException();
    }

    // @Override Not present in JDK7
    public void repaint(long tm, int x, int y, int width, int height) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void print(Graphics g) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBounds(int x, int y, int width, int height, int op) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void handleEvent(AWTEvent e) {
        // Nothing to do here.
    }

    @Override
    public void coalescePaintEvent(PaintEvent e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Point getLocationOnScreen() {
        Point loc = new Point();
        Node node = window.getProxyView();
        while (true) {
            loc.x += node.getLayoutX();
            loc.y += node.getLayoutY();
            Node parent = node.getParent();
            if (parent == null) {
                break;
            }
            node = parent;
        }
        Scene scene = node.getScene();
        loc.x += scene.getX();
        loc.y += scene.getY();
        Window window = scene.getWindow();
        loc.x += window.getX();
        loc.y += window.getY();
        return loc;
    }

    @Override
    public Dimension getPreferredSize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Dimension getMinimumSize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ColorModel getColorModel() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Toolkit getToolkit() {
        return Toolkit.getDefaultToolkit();
    }

    @Override
    public Graphics getGraphics() {
        throw new UnsupportedOperationException();
    }

    @Override
    public FontMetrics getFontMetrics(Font font) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void dispose() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setForeground(Color c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBackground(Color c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFont(Font f) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateCursorImmediately() {
        // TODO: Implement.
    }

    @Override
    public boolean requestFocus(Component lightweightChild, boolean temporary,
            boolean focusedWindowChangeAllowed, long time, Cause cause) {
        if (KFMHelper.processSynchronousLightweightTransfer(window,
                lightweightChild, temporary, focusedWindowChangeAllowed, time)) {
            return true;
        }

        int result = KFMHelper.shouldNativelyFocusHeavyweight(window,
                lightweightChild, temporary, focusedWindowChangeAllowed, time,
                cause);

        switch (result) {
        case KFMHelper.SNFH_FAILURE:
            return false;
        case KFMHelper.SNFH_SUCCESS_PROCEED:
            return FXSwingKeyboardFocusManagerPeer.getInstance().
                    requestFocus(window, lightweightChild, temporary,
                                 focusedWindowChangeAllowed, time, cause);
        case KFMHelper.SNFH_SUCCESS_HANDLED:
            // Either lightweight or excessive request - all events are
            // generated.
            return true;
        default:
            return false;
        }
    }

    @Override
    public boolean isFocusable() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Image createImage(ImageProducer producer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Image createImage(int width, int height) {
        throw new UnsupportedOperationException();
    }

    @Override
    public VolatileImage createVolatileImage(int width, int height) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean prepareImage(Image img, int w, int h, ImageObserver o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int checkImage(Image img, int w, int h, ImageObserver o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public GraphicsConfiguration getGraphicsConfiguration() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean handlesWheelScrolling() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createBuffers(int numBuffers, BufferCapabilities caps)
            throws AWTException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Image getBackBuffer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void flip(int x1, int y1, int x2, int y2, FlipContents flipAction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void destroyBuffers() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reparent(ContainerPeer newContainer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isReparentSupported() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void layout() {
        throw new UnsupportedOperationException();
    }

    // @Override Not present in JDK7
    public Rectangle getBounds() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void applyShape(Region shape) {
        throw new UnsupportedOperationException();
    }

    // @Override Not present in JDK7
    public Dimension preferredSize() {
        throw new UnsupportedOperationException();
    }

    // @Override Not present in JDK7
    public Dimension minimumSize() {
        throw new UnsupportedOperationException();
    }

    // @Override Not present in JDK7
    public void show() {
        throw new UnsupportedOperationException();
    }

    // @Override Not present in JDK7
    public void hide() {
        throw new UnsupportedOperationException();
    }

    // @Override no more present in JDK7
    public void enable() {
        throw new UnsupportedOperationException();
    }

    // @Override no more present in JDK7
    public void disable() {
        throw new UnsupportedOperationException();
    }

    // @Override no more present in JDK7
    public void reshape(int x, int y, int width, int height) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTitle(String title) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMenuBar(MenuBar mb) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setResizable(boolean resizeable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setState(int state) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getState() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMaximizedBounds(Rectangle bounds) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBoundsPrivate(int x, int y, int width, int height) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Rectangle getBoundsPrivate() {
        throw new UnsupportedOperationException();
    }

    // @Override Not present in JDK6
    public void setZOrder(ComponentPeer above) {
        throw new UnsupportedOperationException();
    }

    // @Override Not present in JDK6
    public boolean updateGraphicsData(GraphicsConfiguration gc) {
        throw new UnsupportedOperationException();
    }

}
