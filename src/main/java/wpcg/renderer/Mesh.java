package wpcg.renderer;

import com.jme3.math.Vector3f;
import wpcg.base.mesh.TriangleMesh;

/**
 * Mesh that contains translation, scaling and rotation for a triangle mesh
 * Implements Cloneable to copy
 */
public class Mesh implements Cloneable {
    // Triangle mesh with graphical information
    TriangleMesh tMesh;
    // Translation vector
    Vector3f translation;
    // Scaling vector
    Vector3f scaling;
    // Rotation vector
    Vector3f rotation;

    /**
     * Create a mesh from a given triangle mesh with default transformations
     * @param mesh Triangle Mesh
     */
    public Mesh(TriangleMesh mesh) {
        tMesh = mesh;
        translation = new Vector3f(0, 0, 0);
        scaling = new Vector3f(1, 1, 1);
        rotation = new Vector3f(0, 0, 0);
    }

    /**
     * Create a mesh from a given triangle mesh with given transformations
     * @param mesh Triangle mesh
     * @param positionVector Position vector
     * @param scalingVector Scaling vector
     * @param rotationVector Rotation vector
     */
    public Mesh(TriangleMesh mesh, Vector3f positionVector, Vector3f scalingVector, Vector3f rotationVector) {
        tMesh = mesh;
        translation = positionVector;
        scaling = scalingVector;
        rotation = rotationVector;
    }

    /**
     * Return triangle mesh with graphical information
     * @return Triangle mesh
     */
    public TriangleMesh getTriangleMesh() {
        return tMesh;
    }

    /**
     * Return the translation vector
     * @return Translation vector
     */
    public Vector3f getTranslation() {
        return translation;
    }

    /**
     * Return the scaling vector
     * @return Scaling vector
     */
    public Vector3f getScaling() {
        return scaling;
    }

    /**
     * Return the rotation vector
     * @return Rotation vector
     */
    public Vector3f getRotation() {
        return rotation;
    }

    /**
     * Set the triangle mesh
     * @param tMesh Triangle mesh
     */
    public void setTriangleMesh(TriangleMesh tMesh) {
        this.tMesh = tMesh;
    }

    /**
     * Set the translation vector
     * @param translation Translation vector
     */
    public void setTranslation(Vector3f translation) {
        this.translation = translation;
    }

    /**
     * Set the scaling Vector
     * @param scaling Scaling vector
     */
    public void setScaling(Vector3f scaling) {
        this.scaling = scaling;
    }

    /**
     * Set the rotation vector
     * @param rotation Rotation vector
     */
    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    @Override
    public Mesh clone() throws CloneNotSupportedException {
        Mesh mesh = (Mesh)super.clone();
        mesh.setTriangleMesh(tMesh);
        mesh.setTranslation(translation);
        mesh.setScaling(scaling);
        mesh.setRotation(rotation);
        return mesh;
    }
}
