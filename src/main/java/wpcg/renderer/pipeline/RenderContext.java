package wpcg.renderer.pipeline;

import com.jme3.math.Matrix4f;
import wpcg.base.mesh.TriangleMesh;
import wpcg.renderer.Mesh;
import wpcg.renderer.RenderCamera;

/**
 * Rendering context for rendering pipeline
 */
public class RenderContext {
    // Virtual camera
    private RenderCamera camera;
    // Unchangeable screen size
    private final int screenWidth, screenHeight;
    // Graphical information
    private Mesh mesh;
    // Mesh clone
    private TriangleMesh originalTriangleMesh;
    // Transformation matrix
    private Matrix4f transformationMatrix;

    /**
     * Create a rendering context
     * @param cam Virtual camera
     * @param mesh Mesh with graphical information
     * @param width Viewport width
     * @param height Viewport height
     */
    public RenderContext(RenderCamera cam, Mesh mesh, int width, int height) {
        camera = cam;
        screenWidth = width;
        screenHeight = height;
        this.mesh = mesh;
        // Cone mesh to keep original information
        try {
            originalTriangleMesh = mesh.clone().getTriangleMesh();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return the virtual camera
     * @return Virtual camera
     */
    public RenderCamera getCamera() {
        return camera;
    }

    /**
     * Return the viewport width
     * @return Viewport width
     */
    public int getScreenWidth() {
        return screenWidth;
    }

    /**
     * Return the viewport height
     * @return Viewport height
     */
    public int getScreenHeight() {
        return screenHeight;
    }

    /**
     * Return graphical information as a mesh
     * @return Mesh
     */
    public Mesh getMesh() {
        return mesh;
    }

    /**
     * Return original graphical information as a mesh
     * @return Original mesh
     */
    public TriangleMesh getOriginalTriangleMesh() {
        return originalTriangleMesh;
    }

    /**
     * Return current state of the transformation matrix
     * @return Transformation matrix
     */
    public Matrix4f getTransformationMatrix() {
        return transformationMatrix;
    }

    /**
     * Return the triangle mesh of the mesh as short access
     * @return Triangle mesh
     */
    public TriangleMesh getTriangleMesh() {
        return mesh.getTriangleMesh();
    }

    /**
     * Set the current state of the transformation matrix
     * @param transformationMatrix Transformation matrix
     * @return Updated rendering context
     */
    public RenderContext setTransformationMatrix(Matrix4f transformationMatrix) {
        this.transformationMatrix = transformationMatrix;
        return this;
    }

    /**
     * Set the current triangle mesh of the mesh
     * @param tMesh Triangle mesh
     * @return Updated rendering context
     */
    public RenderContext setTriangleMesh(TriangleMesh tMesh) {
        mesh.setTriangleMesh(tMesh);
        return this;
    }
}