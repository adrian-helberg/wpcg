package wpcg.renderer.pipeline.step;

import com.jme3.math.Matrix4f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import wpcg.base.mesh.Triangle;
import wpcg.base.mesh.TriangleMesh;
import wpcg.renderer.pipeline.RenderContext;

/**
 * Viewport transformation of the rendering pipeline
 *      * Multiplies the transformation matrix with the triangle points of the
 *        mesh and project them into the viewport
 */
public class ViewportTransformation implements Step<RenderContext, RenderContext> {
    @Override
    public RenderContext process(RenderContext input) throws StepException {
        Matrix4f transformMatrix = input.getTransformationMatrix();
        TriangleMesh mesh = input.getOriginalTriangleMesh();
        TriangleMesh transformed = new TriangleMesh();
        for (int i = 0; i < mesh.getNumberOfTriangles(); i++) {
            // Vectors
            Triangle t = mesh.getTriangle(i);
            Vector3f a = mesh.getVertex(t.getA()).getPosition();
            Vector3f b = mesh.getVertex(t.getB()).getPosition();
            Vector3f c = mesh.getVertex(t.getC()).getPosition();
            // Transformed vectors
            int aT, bT, cT;
            aT = transformed.addVertex(projectPoint(a, transformMatrix));
            bT = transformed.addVertex(projectPoint(b, transformMatrix));
            cT = transformed.addVertex(projectPoint(c, transformMatrix));
            transformed.addTriangle(aT, bT, cT);
        }
        return input.setTriangleMesh(transformed);
    }

    /**
     * Multiplies a transformation matrix and a vector
     * @param v Vector Given vector
     * @param m Matrix Given transformation matrix
     * @return Resulting projected vector
     */
    public static Vector3f projectPoint(Vector3f v, Matrix4f m) {
        Vector4f vec = new Vector4f(v.x, v.y, v.z, 1f);
        vec = m.mult(vec);
        vec.x /= vec.w;
        vec.y /= vec.w;
        vec.z /= vec.w;
        return new Vector3f(vec.x, vec.y, vec.z);
    }
}
