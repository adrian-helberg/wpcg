package wpcg.renderer;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import wpcg.base.canvas2d.Canvas2D;
import wpcg.base.mesh.Triangle;
import wpcg.base.mesh.TriangleMesh;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Rendering canvas to hold information about current triangle mesh and canvas
 * Extends Canvas2D to provide a 2D drawing surface
 */
public class RenderCanvas extends Canvas2D {
    // Current triangle mesh to be rendered
    private TriangleMesh triangleMesh;

    /**
     * Create rendering canvas
     * @param width Canvas width
     * @param height Canvas height
     * @param ll Canvas lower-left normalizing vector
     * @param ur Canvas upper-right normalizing vector
     */
    public RenderCanvas(int width, int height, Vector2f ll, Vector2f ur) {
        super(width, height, ll, ur);
    }

    /**
     * Create rendering canvas
     * @param width Canvas width
     * @param height Canvas height
     */
    public RenderCanvas(int width, int height) {
        this(width, height, new Vector2f(-1, -1), new Vector2f(1, 1));
    }

    /**
     * Draw a triangle mesh on the canvas
     * @param g Graphical context
     * @param t Triangle to be drawn
     * @param tMesh Triangle mesh containing the triangle for access
     */
    private void _drawTriangle(Graphics2D g, Triangle t, TriangleMesh tMesh) {
        // Get triangle data
        Vector3f a = tMesh.getVertex(t.getA()).getPosition();
        Vector3f b = tMesh.getVertex(t.getB()).getPosition();
        Vector3f c = tMesh.getVertex(t.getC()).getPosition();
        // Draw edges of the triangle with provided algorithm
        String algorithm = RenderApplication.properties.getProperty("draw.algorithm");
        if(algorithm.equals("Bresenham")) {
            drawRasterLine(g, new Vector2f(a.x, a.y), new Vector2f(b.x, b.y));
            drawRasterLine(g, new Vector2f(b.x, b.y), new Vector2f(c.x, c.y));
            drawRasterLine(g, new Vector2f(c.x, c.y), new Vector2f(a.x, a.y));
        } else if (algorithm.equals("Scanline")) {
            ArrayList<Vector2f> vertices = new ArrayList<>();
            // This drawing algorithm also draws lines between vertices with Bresenham
            vertices.addAll(drawRasterLine(g, new Vector2f(a.x, a.y), new Vector2f(b.x, b.y)));
            vertices.addAll(drawRasterLine(g, new Vector2f(b.x, b.y), new Vector2f(c.x, c.y)));
            vertices.addAll(drawRasterLine(g, new Vector2f(c.x, c.y), new Vector2f(a.x, a.y)));
            // Actual Scanline algorithm
            drawScanlines(g, vertices);
            // Clear vertices for next iteration
            vertices.clear();
        } else {
            // Draw the 'standard' way with drawLine from framework
            drawLine(g, new Vector2f(a.x, a.y), new Vector2f(b.x, b.y), Color.RED);
            drawLine(g, new Vector2f(b.x, b.y), new Vector2f(c.x, c.y), Color.RED);
            drawLine(g, new Vector2f(c.x, c.y), new Vector2f(a.x, a.y), Color.RED);
        }

    }

    /**
     * Fill the space between provided vertices
     * @param g Graphical context
     * @param vertices Vertices to be drawn in between
     */
    private void drawScanlines(Graphics g, ArrayList<Vector2f> vertices) {
        if (vertices.isEmpty()) return;
        g.setColor(Color.GREEN);
        // 1. Sort vertices by y-axis-value
        vertices.sort((v0, v1) -> Float.compare(v0.y, v1.y));
        // 2. Iterate through sorted vertices
        int i = 0;
        while (i < vertices.size()) {
            // Get the vertices with the lowest y-value
            int currentY = (int)vertices.get(i).y;
            List<Vector2f> vList = vertices
                    .stream().filter(v -> v.y == currentY)
                    .collect(Collectors.toList());
            // Check if there is at least a pair of vertices on the same 'level'
            // to be drawn in between
            if (vList.size() >= 2) {
                // Get the lowest x-value
                float minX = vList.stream().min((v1, v2) -> Float.compare(v1.x, v2.x)).get().x;
                // Get the highest x-value
                float maxX = vList.stream().max((v1, v2) -> Float.compare(v1.x, v2.x)).get().x;
                // Loop through x-values until maximum x is reached
                while(minX < maxX) {
                    // Check if there is still space between lowest and highest x-value
                    if (!vertices.contains(new Vector2f(minX, currentY))) {
                        // Fill the space with an arc
                        g.drawArc((int)minX, currentY, 1, 1, 0, 360);
                    }
                    // Continue with next x-value
                    minX++;
                }
            }
            // Current y-value has been processed, skip those
            i += vList.size();
        }
    }

    /**
     * Draw raster line with Bresenham algorithm
     * @param g Graphical context
     * @param p0 Line start vector
     * @param p1 Line end vector
     * @return Drawn points for further use (Scanline algorithm)
     */
    private ArrayList<Vector2f> drawRasterLine(Graphics g, Vector2f p0, Vector2f p1) {
        g.setColor(Color.BLUE);
        // Storage for drawn points
        ArrayList<Vector2f> vertices = new ArrayList<>();
        // Get pixels on the canvas
        int x0 = (int) world2Pixel(p0).x;
        int y0 = (int) world2Pixel(p0).y;
        int x1 = (int) world2Pixel(p1).x;
        int y1 = (int) world2Pixel(p1).y;
        // Delta x
        float dx = Math.abs(x1 - x0);
        // Step for x-value
        int sx = x0 < x1 ? 1 : -1;
        // Delta y
        float dy = -Math.abs(y1 - y0);
        // Step for y-value
        int sy = y0 < y1 ? 1 : -1;
        // Error values to determine with step to take
        float err = dx + dy, e2;
        // Loop until algorithm has finished
        while(true) {
            // Draw point on current coordinates
            g.drawArc(x0, y0, 1, 1, 0, 360);
            // Add point to drawn points list
            vertices.add(new Vector2f(x0, y0));
            // Loop condition
            if (x0==x1 && y0==y1) break;
            // Check which step to take for next point to draw
            e2 = 2 * err;
            if (e2 > dy) {
                err += dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
        // Return all drawn points
        return vertices;
    }

    /**
     * Set current triangle mesh to be rendered
     * @param tMesh Triangle mesh
     */
    public void setTriangleMesh(TriangleMesh tMesh) {
        this.triangleMesh = tMesh;
        // Trigger onRepaint
        repaint();
    }

    @Override
    public void onRepaint(Graphics2D g) {
        if (triangleMesh == null) return;
        // Clear old stuff from canvas
        g.clearRect(0, 0, getWidth(), getHeight());
        // Iterate over all triangles from triangle mesh
        for (int i = 0; i < triangleMesh.getNumberOfTriangles(); i++) {
            _drawTriangle(g, triangleMesh.getTriangle(i), triangleMesh);
        }
    }

    @Override
    protected void onMouseDragged(int x, int y) {}

    @Override
    protected void onMouseMoved(int x, int y) {}

    @Override
    protected void onMouseClicked(int x, int y) {}
}
