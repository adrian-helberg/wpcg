/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 * <p>
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 * <p>
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */
/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */
package wpcg.base.mesh;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import wpcg.base.Logger;

import java.util.List;

/**
 * Tools for triangle meshes.
 */
public class TriangleMeshTools {
    /**
     * Adds all content of the otherMesh to the meshBase.
     */
    public static void unite(TriangleMesh baseMesh, TriangleMesh otherMesh) {
        int vertexOffset = baseMesh.getNumberOfVertices();
        int texCoordOffset = baseMesh.getNumberOfTextureCoordinates();

        // Vertices
        for (int i = 0; i < otherMesh.getNumberOfVertices(); i++) {
            baseMesh.addVertex(new Vertex(otherMesh.getVertex(i)));
        }
        for (int i = 0; i < otherMesh.getNumberOfTextureCoordinates(); i++) {
            baseMesh.addTextureCoordinate(new Vector2f(otherMesh.getTextureCoordinate(i)));
        }
        for (int i = 0; i < otherMesh.getNumberOfTriangles(); i++) {
            Triangle t = new Triangle(otherMesh.getTriangle(i));
            t.addVertexIndexOffset(vertexOffset);
            t.addTexCoordOffset(texCoordOffset);
            baseMesh.addTriangle(t);
        }

        vertexOffset += otherMesh.getNumberOfVertices();
        texCoordOffset += otherMesh.getNumberOfTextureCoordinates();
    }

    /**
     * Create a unified mesh from all meshes in the list. Not tested for meshes using textures.
     */
    public static TriangleMesh unite(List<TriangleMesh> meshes) {

        if (meshes.size() == 0) {
            return null;
        }

        TriangleMesh mesh = meshes.get(0);

        int vertexOffset = mesh.getNumberOfVertices();
        int texCoordOffset = mesh.getNumberOfTextureCoordinates();
        for (int meshIndex = 1; meshIndex < meshes.size(); meshIndex++) {
            TriangleMesh m = meshes.get(meshIndex);
            // Vertices
            for (int i = 0; i < m.getNumberOfVertices(); i++) {
                mesh.addVertex(new Vertex(m.getVertex(i)));
            }
            for (int i = 0; i < m.getNumberOfTextureCoordinates(); i++) {
                mesh.addTextureCoordinate(new Vector2f(m.getTextureCoordinate(i)));
            }
            for (int i = 0; i < m.getNumberOfTriangles(); i++) {
                Triangle t = new Triangle(m.getTriangle(i));
                t.addVertexIndexOffset(vertexOffset);
                t.addTexCoordOffset(texCoordOffset);
                mesh.addTriangle(t);
            }

            vertexOffset += m.getNumberOfVertices();
            texCoordOffset += m.getNumberOfTextureCoordinates();
        }

        return mesh;
    }


    /**
     * Move all vertices with the offset vector (x, y, z)
     */
    public static void translate(TriangleMesh mesh, float x, float y, float z) {
        for (int i = 0; i < mesh.getNumberOfVertices(); i++) {
            Vertex v = mesh.getVertex(i);
            v.getPosition().set(v.getPosition().x + x, v.getPosition().y + y, v.getPosition().z + z);
        }
    }

    /**
     * Move all vertices with the offset vector t
     */
    public static void translate(TriangleMesh mesh, Vector3f t) {
        translate(mesh, t.x, t.y, t.z);
    }

    /**
     * Merge all vertices which are closer to one another than numerical accuracy.
     */
    public static void mergeVertices(TriangleMesh mesh) {
        int numRemoved = 0;
        for (int i = 0; i < mesh.getNumberOfVertices(); i++) {
            for (int j = i + 1; j < mesh.getNumberOfVertices(); j++) {
                if (mesh.getVertex(i).getPosition().subtract(mesh.getVertex(j).getPosition()).length()
                        < 1e-5) {
                    for (int t = 0; t < mesh.getNumberOfTriangles(); t++) {
                        Triangle triangle = mesh.getTriangle(t);
                        triangle.replaceVertexIndex(i, j);
                    }
                    // Deprecate vertex j
                    mesh.getVertex(j).getPosition().set(new Vector3f(Float.NaN, Float.NaN, Float.NaN));
                    numRemoved++;
                }
            }
        }
        // Remove all degenerated triangles.
        for (int i = 0; i < mesh.getNumberOfTriangles(); i++) {
            if (mesh.getTriangle(i).isDegenerated()) {
                mesh.removeTriangle(i);
                i--;
            } else {
                Triangle t = mesh.getTriangle(i);
                Vector3f a = mesh.getVertex(t.getVertexIndex(0)).position;
                Vector3f b = mesh.getVertex(t.getVertexIndex(1)).position;
                Vector3f c = mesh.getVertex(t.getVertexIndex(2)).position;
                if (Triangle.getArea(a, b, c) < 1e-5) {
                    mesh.removeTriangle(i);
                    i--;
                }
            }
        }
        Logger.getInstance().debug("Removed " + numRemoved + " vertices with same position.");
    }
}
