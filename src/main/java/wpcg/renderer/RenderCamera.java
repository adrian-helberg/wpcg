package wpcg.renderer;

import com.jme3.math.Vector3f;
import wpcg.renderer.pipeline.step.ModelTransformation;

/**
 * Virtual camera as viewer of a scene
 */
public class RenderCamera {
    // Camera vectors
    private Vector3f position, up, direction;
    // field of view, near clipping value, far clipping value
    private float foV, near, far;

    /**
     * Create a virtual camera
     * @param positionVector viewers 'eye'
     * @param upVector Up vector
     * @param dir Camera viewing direction
     * @param fieldOfView Angle of the vision field
     * @param nearClipping Closest things to see
     * @param farClipping Furthermost things to see
     */
    public RenderCamera(Vector3f positionVector, Vector3f upVector, Vector3f dir,
                        float fieldOfView, float nearClipping, float farClipping) {
        position = positionVector;
        up = upVector;
        direction = dir;
        foV = fieldOfView;
        near = nearClipping;
        far = farClipping;
    }

    /**
     * Move camera along the x axis
     * @param positive Flag for moving direction
     */
    public void moveX(boolean positive) {
        position.x += positive ? 0.1f : -0.1f;
        direction.x += positive ? 0.1f : -0.1f;
    }

    /**
     * Move camera along the y axis
     * @param positive Flag for moving direction
     */
    public void moveY(boolean positive) {
        position.y += positive ? 0.1f : -0.1f;
        direction.y += positive ? 0.1f : -0.1f;
    }

    /**
     * Move camera along the z axis
     * @param positive Flag for moving direction
     */
    public void moveZ(boolean positive) {
        position.z += positive ? 0.1f : -0.1f;
        direction.z += positive ? 0.1f : -0.1f;
    }

    /**
     * Rotate the camera around x axis
     * @param up Rotation direction
     */
    public void rotateX(boolean up) {
        direction = ModelTransformation.createRotationMatrixX(up ? 10f : -10f).mult(direction);
    }

    /**
     * Rotate the camera around y axis
     * @param right Rotation direction
     */
    public void rotateY(boolean right) {
        direction = ModelTransformation.createRotationMatrixY(right ? 10f : -10f).mult(direction);
    }

    /**
     * Change camera field of view
     * @param in Flag to determine zooming in or out
     */
    public void zoom(boolean in) {
        foV += in ? 1f : -1f;
    }

    /**
     * Return camera position
     * @return Camera position
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * Return camera up vector
     * @return Up vector
     */
    public Vector3f getUp() {
        return up;
    }

    /**
     * Return camera viewing direction
     * @return Direction vector
     */
    public Vector3f getDirection() {
        return direction;
    }

    /**
     * Return field of view angle
     * @return Field of view angle
     */
    public float getFoV() {
        return foV;
    }

    /**
     * Return near clipping value
     * @return Near clipping
     */
    public float getNear() {
        return near;
    }

    /**
     * Return far clipping value
     * @return Far clipping
     */
    public float getFar() {
        return far;
    }

    /**
     * Set camera position
     * @param position Camera position
     */
    public void setPosition(Vector3f position) {
        this.position = position;
    }

    /**
     * Set camera viewing direction
     * @param direction Viewing direction
     */
    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }
}
