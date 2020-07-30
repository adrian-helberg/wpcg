package wpcg.renderer.pipeline.step;

import com.jme3.math.Matrix4f;
import wpcg.renderer.pipeline.RenderContext;

/**
 * Projection step of the rendering pipeline
 *      * Projects camera view frustum into the world
 */
public class Projection implements Step<RenderContext, RenderContext> {

    @Override
    public RenderContext process(RenderContext input) throws StepException {
        // Projection matrix (perspective transformation)
        Matrix4f projectionMatrix = createPerspectiveMatrix(
                input.getCamera().getFoV(),
                input.getScreenWidth(),
                input.getScreenHeight(),
                input.getCamera().getNear(),
                input.getCamera().getFar()
        );
        // Pass resulting transformation matrix to the rendering context
        Matrix4f transformMatrix = projectionMatrix.mult(input.getTransformationMatrix());
        return input.setTransformationMatrix(transformMatrix);
    }

    /**
     * Create perspective matrix for given field of view, screen width,
     * screen height, near and far clipping
     * @param foV Field of view
     * @param screenWidth Screen width
     * @param screenHeight Screen height
     * @param near Near clipping
     * @param far Far clipping
     * @return Perspective matrix
     */
    Matrix4f createPerspectiveMatrix(float foV, float screenWidth, float screenHeight, float near, float far) {
        float cotanFov = 1f / (float) Math.tan(ModelTransformation.toRadian(foV) * .5f);
        float depth = far - near;
        float aspect = screenHeight / screenWidth;
        Matrix4f perspectiveMatrix = new Matrix4f();
        perspectiveMatrix.m00 = cotanFov * aspect;
        perspectiveMatrix.m11 = cotanFov;
        perspectiveMatrix.m22 = far / depth;
        perspectiveMatrix.m23 = -(far * near) / depth;
        perspectiveMatrix.m32 = 1f;
        return perspectiveMatrix;
    }
}
