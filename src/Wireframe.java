import java.awt.*;
import java.io.*;
import java.util.*;

/**
 * Represents a wireframe of a 3d image constructed from 2d shapes.
 */
public class Wireframe {

    Triangle[] shapes; // Array of triangles that make up a wireframe

    public Wireframe(Triangle[] shapes) {
        this.shapes = shapes;
    }

    /**
     * Parses a data file into a Wireframe object. Expects first line to be an int, n specifying number of vertices,
     * followed by n lines of three doubles specifying the x, y, z coordinates of each vertex. Then a line specifying
     * the number of triangles which make up the wireframe, followed by lines of three ints specifying which of the
     * previous vertices (1-n) make up the triangle.
     *
     * @param filePath Path to file to read data from
     * @return Wireframe object
     * @throws IOException if file cannot be parsed.
     */
    public static Wireframe readTrianglesFromFile(String filePath) throws IOException {
        Scanner s;
        HashMap<Integer, double[]> vertices = new HashMap<>(); // vertexNumber: [x,y,z]
        Triangle[] triangles = new Triangle[0];
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            int numVertices = Integer.parseInt(br.readLine());
            for (int i = 1; i <= numVertices; ++i) {
                s = new Scanner(br.readLine());
                vertices.put(i, new double[]{s.nextDouble(), s.nextDouble(), s.nextDouble()});
            }
            int numTriangles = Integer.parseInt(br.readLine());
            triangles = new Triangle[numTriangles];
            for (int i = 0; i < numTriangles; ++i) {
                s = new Scanner(br.readLine());
                triangles[i] = new Triangle(vertices.get(s.nextInt()), vertices.get(s.nextInt()), vertices.get(s.nextInt()));
            }
            if (br.readLine() != null) { // Too many triangles present
                throw new IOException();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NumberFormatException | NoSuchElementException | NullPointerException e) {
            throw new IOException();
        }
        return new Wireframe(triangles);
    }
}
