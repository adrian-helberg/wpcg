/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package wpcg.a4;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import wpcg.base.mesh.ObjReader;
import wpcg.base.mesh.TriangleMesh;
import wpcg.a4.level.Cell;
import wpcg.a4.level.Level;
import wpcg.a4.level.Direction;
import wpcg.base.animatedmesh.AnimatedMesh;
import wpcg.base.animatedmesh.AnimationControllerPath;
import wpcg.base.CameraController;
import wpcg.base.Scene;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Base scene for the exercise 4.
 */
public class A4Scene extends Scene {

    /**
     * Level object
     */
    private Level level;

    /**
     * The animation node of the knight.
     */
    protected AnimatedMesh animatedMesh;

    /**
     * Root node of the scene graph.
     */
    protected Node rootNode;

    /**
     * Camera controller.
     */
    protected CameraController cameraController;

    public A4Scene() {
        this.level = new Level();
        this.cameraController = null;
        this.animatedMesh = null;
        this.level.readLevelFromFile("src/main/resources/levels/level01.json");
    }

    protected List<Vector3f> getWayPoints() {
        // Set path for the knight (list of cell centers)
        return Arrays.asList(
                to3D(level.getCell(0, 1).getCenter()),
                to3D(level.getCell(1, 0).getCenter()),
                to3D(level.getCell(2, 0).getCenter()),
                to3D(level.getCell(3, 0).getCenter()),
                to3D(level.getCell(3, 1).getCenter()),
                to3D(level.getCell(2, 1).getCenter()),
                to3D(level.getCell(2, 2).getCenter()),
                to3D(level.getCell(3, 2).getCenter()),
                to3D(level.getCell(2, 3).getCenter()),
                to3D(level.getCell(1, 3).getCenter()),
                to3D(level.getCell(0, 3).getCenter()),
                to3D(level.getCell(0, 2).getCenter())
        );
    }

    @Override
    public void init(AssetManager assetManager, Node rootNode, CameraController cameraController) {
        this.rootNode = rootNode;
        this.cameraController = cameraController;
        Vector3f levelCenter = level.getCenter();
        cameraController.setup(new Vector3f(10, 10, 10),
                levelCenter, new Vector3f(0, 1, 0));


        // Setup animated knight mesh.
        Node knightNode = loadCharacter(assetManager, rootNode, "Models/knight.gltf");
        knightNode.setLocalScale(0.003f);
        AnimationControllerPath knightAnimationController = new AnimationControllerPath(getWayPoints(), 0.5f);
        animatedMesh = new AnimatedMesh(knightNode, knightAnimationController);

        // Render cells
        for (Iterator<Cell> it = level.getCellIterator(); it.hasNext(); ) {
            Cell cell = it.next();
            addCellGeometry(assetManager, "Textures/stone.png",
                    "Textures/stone_normalmap.png", cell);

            // Render walls
            for (Direction dir : Direction.values()) {
                if (cell.isWall(dir)) {
                    if (cell.getNeighborCell(dir) != null
                            && (dir == Direction.UHR_0 || dir == Direction.UHR_2 || dir == Direction.UHR_4)) {
                        // Create wall only once.
                        continue;
                    }
                    addWallGeometry(assetManager, "Textures/stone.png",
                            "Textures/stone_normalmap.png",
                            cell, dir);
                }
            }
        }
    }

    @Override
    public void update(float time) {
        animatedMesh.update(time);
    }

    @Override
    public void render() {
    }

    @Override
    public void setupLights(AssetManager assetManager, Node rootNode, ViewPort viewPort) {
        super.setupLights(assetManager, rootNode, viewPort);
    }

    /**
     * Convert a 2D vector to a 3D vector.
     */
    public Vector3f to3D(Vector2f p) {
        return new Vector3f(p.x, 0, p.y);
    }

    /**
     * Create and add the geometry for a hexagon cell ground.
     */
    protected void addCellGeometry(AssetManager assetManager, String textureFilename,
                                   String normalMapFilename, Cell cell) {
        TriangleMesh mesh = new ObjReader().read("Models/hexagon.obj");
        Vector3f cellCenter = to3D(cell.getCenter());

        // TODO: Create jmonkey triangle mesh, set texture and normal map texture, move to cell center,
        //  add to root node
    }

    /**
     * Create and add the geometry for a cell wall in a given direction.
     */
    protected void addWallGeometry(AssetManager assetManager, String textureFilename, String normalMapFilename,
                                   Cell cell, Direction dir) {
        Vector2f orientation = dir.getOrientation();
        Vector2f wallCenter = cell.getCenter().add(orientation.mult(Cell.getZellenhoehe()));
        TriangleMesh mesh = new ObjReader().read("Models/hex_wall.obj");

        // TODO: Create jmonkey triangle mesh, set texture and normal map texture, move to wall center, rotate wall,
        //  add to root node
    }
}
