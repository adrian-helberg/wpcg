package wpcg.renderer.pipeline.step;

import com.jme3.math.Matrix4f;
import com.jme3.math.Vector3f;
import wpcg.renderer.pipeline.RenderContext;

/**
 * Camera transformation step of the rendering pipeline
 * Two operations are made in one step:
 *      * Translate to the origin
 *      * Rotate so that the direction lines up with z axis
 */
public class CameraTransformation implements Step<RenderContext, RenderContext> {
    @Override
    public RenderContext process(RenderContext input) throws StepException {
        // Look-at matrix (View-Transformation)
        Matrix4f viewMatrix = createLookAtMatrix(
                input.getCamera().getPosition(),
                input.getCamera().getDirection(),
                input.getCamera().getUp()
        );
        // Pass resulting transformation matrix to the context
        Matrix4f cameraMatrix = viewMatrix.mult(input.getTransformationMatrix());
        return input.setTransformationMatrix(cameraMatrix);
    }

    /**
     * Create camera matrix for given camera position, target and up vectors
     * Combine both translating to origin and rotating to line up with z axis
     * [      1       0       0   0 ]   [ xaxis.x  yaxis.x  zaxis.x 0 ]
     * [      0       1       0   0 ] * [ xaxis.y  yaxis.y  zaxis.y 0 ]
     * [      0       0       1   0 ]   [ xaxis.z  yaxis.z  zaxis.z 0 ]
     * [ -eye.x  -eye.y  -eye.z   1 ]   [       0        0        0 1 ]
     *
     * @param eye Position vector of the camera
     * @param ref Targeting vector of the camera
     * @param up Up vector of the camera
     * @return Camera matrix (look-at-matrix)
     */
    public static Matrix4f createLookAtMatrix(Vector3f eye, Vector3f ref, Vector3f up) {
        Vector3f x, y, z;
        z = ref.subtract(eye).normalize();
        x = up.cross(z).normalize();
        y = z.cross(x).normalize();

        Matrix4f lookAtMatrix = new Matrix4f();
        lookAtMatrix.m00 = x.x;
        lookAtMatrix.m01 = x.y;
        lookAtMatrix.m02 = x.z;
        lookAtMatrix.m03 = -x.dot(eye);
        lookAtMatrix.m10 = y.x;
        lookAtMatrix.m11 = y.y;
        lookAtMatrix.m12 = y.z;
        lookAtMatrix.m13 = -y.dot(eye);
        lookAtMatrix.m20 = z.x;
        lookAtMatrix.m21 = z.y;
        lookAtMatrix.m22 = z.z;
        lookAtMatrix.m23 = -z.dot(eye);
        return lookAtMatrix;
    }
}
