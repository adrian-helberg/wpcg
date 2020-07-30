package wpcg.renderer;

import com.jme3.math.Vector3f;
import wpcg.base.mesh.ObjReader;
import wpcg.renderer.pipeline.Pipeline;
import wpcg.renderer.pipeline.RenderContext;
import wpcg.renderer.pipeline.step.*;

/**
 * Rendering class for processing rendering pipeline for a given rendering context
 * Also contains triangle mesh file import
 */
public class Renderer {
    // Rendering pipeline
    private Pipeline<RenderContext, RenderContext> pipeline;
    // Rendering canvas instance to set precessed triangle mesh
    private RenderCanvas canvas;
    // Rendering context
    private RenderContext context;

    /**
     * Hide default constructor
     */
    private Renderer() {}

    /**
     * Creates Renderer object.
     * Prepare rendering context with camera and imported triangle mesh
     *
     * @param fileName File name to be imported from
     */
    Renderer(String fileName, RenderCanvas canvas) {
        // Initialize rendering pipeline
        pipeline = setupPipeline();
        this.canvas = canvas;
        // Camera position
        Vector3f position = new Vector3f(0, 0, -1f);
        // Camera up vector
        Vector3f up = new Vector3f(0, 1, 0);
        // Camera viewing direction
        Vector3f direction = new Vector3f(0, 0, 1);
        // Rendering camera
        RenderCamera camera = new RenderCamera(
                position,
                up,
                direction,
                45f, 0.2f, .3f
        );
        // Import from file
        Mesh mesh = importOBJ(fileName);
        // Rendering context
        context = new RenderContext(
                camera,
                mesh,
                canvas.getWidth(),
                canvas.getHeight()
        );
        // Initially process pipeline to render after renderer was created
        processPipeline();
    }

    /**
     * Import a triangle mesh from given file
     * @param selectedFileName File name to be imported from
     */
    private Mesh importOBJ(String selectedFileName) {
        return new Mesh(new ObjReader().read("Models/" + selectedFileName));
    }

    /**
     * Initialize rendering pipeline
     * @return Rendering pipeline
     */
    private Pipeline<RenderContext, RenderContext> setupPipeline() {
        return new Pipeline<>(new ModelTransformation())
                .pipe(new CameraTransformation())
                .pipe(new Projection())
                .pipe(new ViewportTransformation())
                .pipe(new Clipping());
    }

    /**
     * Process pipeline with a given context step by step until its finished
     * The pipeline updates the rendering context
     */
    void processPipeline() {
        RenderContext ctx = pipeline.execute(context);
        canvas.setTriangleMesh(ctx.getMesh().getTriangleMesh());
    }

    /**
     * Return current rendering context
     * @return Rendering context
     */
    public RenderContext getContext() {
        return context;
    }
}
