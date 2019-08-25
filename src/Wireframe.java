import java.io.*;
import java.util.HashMap;

/**
 * Represents a wireframe of a 3d image constructed from 2d shapes.
 */
public class Wireframe {
    private HashMap<Integer, Point> vertices; // Numbered points 1-n of each vertex in the wireframe
    private Shape2d[] shapes; // Array of 2d shapes that make up the wireframe, made from points that use references to vertices
    private static final int DIMENSIONS = 3; // Number of dimensions that specify each point

    public Wireframe(HashMap<Integer, Point> vertices, Shape2d[] shapes) {
        this.vertices = vertices;
        this.shapes = shapes;
    }

    /**
     * Parses a data file into a Wireframe object. Expects first line to be an int, n specifying number of vertices,
     * followed by n lines of three doubles specifying the x, y, z coordinates of each vertex. Then a line specifying
     * the number of shapes which make up the wireframe, followed by lines of three ints specifying which of the
     * previous vertices (1-n) make up the shape.
     *
     * @param dataFile  Path to file to read data from
     * @param numPoints Number of points that make up each shape, must be greater than 2
     * @return Wireframe object
     * @throws IOException If file cannot be parsed
     */
    public static Wireframe readShapesFromFile(File dataFile, int numPoints) throws IOException {
        HashMap<Integer, Point> vertices = new HashMap<>(); // vertexNumber: [x,y,z]
        Shape2d[] shapes;
        try (BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
            int numVertices;
            int numShapes;
            try { // Set number of vertices in shape
                numVertices = Integer.parseInt(br.readLine());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                throw new IOException("Number of vertices could not be parsed.");
            }
            try {
                for (int i = 1; i <= numVertices; ++i) { // Fill HashMap of vertices
                    String[] line = br.readLine().split("\\s+"); // Split each line by whitespace
                    double[] coordinates = new double[DIMENSIONS];
                    for (int j = 0; j < DIMENSIONS; ++j) {
                        coordinates[j] = Double.parseDouble(line[j]);
                    }
                    vertices.put(i, new Point(coordinates));
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                throw new IOException("Coordinates could not be parsed.");
            }
            try { // Set number of shapes in wireframe
                numShapes = Integer.parseInt(br.readLine());
                shapes = new Shape2d[numShapes];
            } catch (NumberFormatException e) {
                e.printStackTrace();
                throw new IOException("Number of shapes could not be parsed.");
            }
            try {
                for (int i = 0; i < numShapes; ++i) { // Fill Array of shapes
                    String[] line = br.readLine().split("\\s+"); // Split each line by whitespace
                    Point[] shapeVertices = new Point[numPoints];
                    for (int j = 0; j < numPoints; ++j) {
                        shapeVertices[j] = vertices.get(Integer.parseInt(line[j])); // Add shapes by vertex references
                    }
                    shapes[i] = new Shape2d(shapeVertices);
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                throw new IOException("Vertices of shapes could not be parsed.");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new IOException("Specified data file does not exist.");
        }
        return new Wireframe(vertices, shapes);
    }
}
