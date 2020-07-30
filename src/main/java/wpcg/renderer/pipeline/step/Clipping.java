package wpcg.renderer.pipeline.step;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import wpcg.base.mesh.Triangle;
import wpcg.base.mesh.TriangleMesh;
import wpcg.base.mesh.Vertex;
import wpcg.renderer.pipeline.RenderContext;

/**
 * Clipping methods of the rendering pipeline
 *      * Backface-Culling: Discard all triangles where the dot product of
 *        the surface normal and the camera position is greater or equal to zero
 *      * Occlusion Culling: TODO
 */
public class Clipping implements Step<RenderContext, RenderContext> {
    @Override
    public RenderContext process(RenderContext input) throws StepException {
        TriangleMesh mesh = input.getTriangleMesh();
        TriangleMesh clipped = new TriangleMesh();
        for (int i = 0; i < mesh.getNumberOfTriangles(); i++) {
            Triangle t = mesh.getTriangle(i);

            Vertex a = mesh.getVertex(t.getA());
            Vertex b = mesh.getVertex(t.getB());
            Vertex c = mesh.getVertex(t.getC());

            Vector3f aV = a.getPosition();
            Vector3f bV = b.getPosition();
            Vector3f cV = c.getPosition();

            Matrix3f m = new Matrix3f();
            m.set(0, 0, aV.x);
            m.set(0, 1, aV.y);
            m.set(0, 2, 1);
            m.set(1, 0, bV.x);
            m.set(1, 1, bV.y);
            m.set(1, 2, 1);
            m.set(2, 0, cV.x);
            m.set(2, 1, cV.y);

            if (m.determinant() < 0) {
                clipped.addTriangle(new Triangle(
                        clipped.addVertex(a),
                        clipped.addVertex(b),
                        clipped.addVertex(c)
                ));
            }
        }
        // Pass resulting triangle mesh to the context
        return input.setTriangleMesh(clipped);
    }
}