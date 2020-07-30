package wpcg.renderer.pipeline.step;

import com.jme3.math.Matrix4f;
import com.jme3.math.Vector3f;
import wpcg.renderer.pipeline.RenderContext;

/**
 * Model transformation of the rendering pipeline
 *      * Translate, scale and rotate a triangle mesh into world space
 */
public class ModelTransformation implements Step<RenderContext, RenderContext> {
    @Override
    public RenderContext process(RenderContext input) throws StepException {
        // Translation
        Matrix4f positionMatrix = createTranslationMatrix(input.getMesh().getTranslation());
        // Scaling
        Matrix4f scaleMatrix = createScalingMatrix(input.getMesh().getScaling());
        // Rotation
        Matrix4f rotationMatrix = createRotationMatrix(input.getMesh().getRotation());
        // World matrix: Translation * Rotation * Scaling
        Matrix4f worldMatrix = positionMatrix.mult(rotationMatrix).mult(scaleMatrix);
        // Pass resulting transformation matrix to the rendering context
        return input.setTransformationMatrix(worldMatrix);
    }

    /**
     * Create the translation matrix that corresponds to a given translation vector
     * @param translation Translation vector
     * @return Translation 4x4 matrix
     */
    private Matrix4f createTranslationMatrix(Vector3f translation) {
        Matrix4f matrix = new Matrix4f();
        matrix.m03 = translation.x;
        matrix.m13 = translation.y;
        matrix.m23 = translation.z;
        return matrix;
    }

    /**
     * Create the scaling matrix that corresponds to a given scaling vector
     * @param scaling Scaling vector
     * @return Scaling 4x4 matrix
     */
    private Matrix4f createScalingMatrix(Vector3f scaling) {
        Matrix4f matrix = new Matrix4f();
        matrix.m00 = scaling.x;
        matrix.m11 = scaling.y;
        matrix.m22 = scaling.z;
        return matrix;
    }

    /**
     * Create rotation matrix for a given rotation vector containing the x-axis
     * rotation in the x coordinate, the y-axis rotation in the y coordinate and
     * the z-axis rotation in the z coordinate
     * @param rotation Rotation vector
     * @return Rotation 4x4 matrix
     */
    public static Matrix4f createRotationMatrix(Vector3f rotation) {
        Matrix4f matrix = new Matrix4f();
        matrix = matrix.mult(createRotationMatrixZ(rotation.z));
        matrix = matrix.mult(createRotationMatrixY(rotation.y));
        matrix = matrix.mult(createRotationMatrixX(rotation.x));
        return matrix;
    }

    /**
     * Create the x-axis rotation Matrix for a given angle phi
     * @param phi Rotation angle
     * @return X-axis rotation 4x4 Matrix
     */
    public static Matrix4f createRotationMatrixX(float phi) {
        Matrix4f matrix = new Matrix4f();
        if (phi == 0) return matrix;
        final float cos = (float) Math.cos(toRadian(phi));
        final float sin = (float) Math.sin(toRadian(phi));
        matrix.m11 = cos;
        matrix.m12 = -sin;
        matrix.m21 = sin;
        matrix.m22 = cos;
        return matrix;
    }

    /**
     * Create the y-axis rotation Matrix for a given angle phi
     * @param phi Rotation angle
     * @return Y-axis rotation 4x4 Matrix
     */
    public static Matrix4f createRotationMatrixY(float phi) {
        Matrix4f matrix = new Matrix4f();
        if (phi == 0) return matrix;
        final float cos = (float) Math.cos(toRadian(phi));
        final float sin = (float) Math.sin(toRadian(phi));
        matrix.m00 = cos;
        matrix.m02 = sin;
        matrix.m20 = -sin;
        matrix.m22 = cos;
        return matrix;
    }

    /**
     * Create the z-axis rotation Matrix for a given angle phi
     * @param phi Rotation angle
     * @return Z-axis rotation 4x4 Matrix
     */
    public static Matrix4f createRotationMatrixZ(float phi) {
        Matrix4f matrix = new Matrix4f();
        if (phi == 0) return matrix;
        final float cos = (float) Math.cos(toRadian(phi));
        final float sin = (float) Math.sin(toRadian(phi));
        matrix.m00 = cos;
        matrix.m01 = -sin;
        matrix.m10 = sin;
        matrix.m11 = cos;
        return matrix;
    }

    /**
     * Helper method to transform an angle into radian
     * @param angle Angle to be transformed
     * @return Radian respresentation of the angle
     */
    public static float toRadian(float angle) {
        return angle * ((float) Math.PI / 180f);
    }
}