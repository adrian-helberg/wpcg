/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package wpcg.base.canvas2d;

import com.jme3.math.Vector2f;

import java.awt.*;

/**
 * Simple example canvas to draw something
 */
public class SimpleExampleCanvas2D extends Canvas2D {

    public SimpleExampleCanvas2D(int width, int height) {
        super(width, height, new Vector2f(-1, -1), new Vector2f(1, 1));
    }

    @Override
    public void onRepaint(Graphics2D g) {
        drawLine(g, new Vector2f(-0.5f, -0.5f), new Vector2f(0.2f, 0.4f), Color.BLUE);
        drawPoint(g, new Vector2f(0, 0.3f), Color.RED);
    }

    @Override
    protected void onMouseDragged(int x, int y) {
    }

    @Override
    protected void onMouseMoved(int x, int y) {
    }

    @Override
    protected void onMouseClicked(int x, int y) {
        Vector2f pWorld = pixel2World(new Vector2f(x, y));
        System.out.printf("You clicked at pixel coordinates (%d,%d) which corresponds to world coordinates (%.2f,%.2f).\n",
                x, y, pWorld.x, pWorld.y);
    }
}
