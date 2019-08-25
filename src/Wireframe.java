import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Represents a wireframe of a 3d image constructed from 2d shapes.
 */
public class Wireframe {
    private static final int DIMENSIONS = 3; // Number of dimensions that specify each point
    private final HashMap<Integer, Point> vertices; // Numbered points 1-n of each vertex in the wireframe
    private final Shape2d[] shapes; // Array of 2d shapes that make up the wireframe
    //TODO Change to store each unique line rather than points

    /**
     * Constructs a Wireframe from a numbered (1-n) HashMap of vertices (Points) and an array of Shape2d objects where
     * each is specified from references to points in the HashMap.
     *
     * @param vertices HashMap of vertices making up the shape: Point objects numbered 1-n
     * @param shapes   Array of Shape2d objects that make up the wireframe, constructed using Point objects that
     *                 reference points in vertices
     */
    private Wireframe(HashMap<Integer, Point> vertices, Shape2d[] shapes) {
        this.vertices = vertices;
        this.shapes = shapes;
    }

    public Wireframe() { // Empty wireframe
        this.vertices = new HashMap<>();
        this.shapes = new Shape2d[0];
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
                throw new IOException("Number of vertices could not be parsed.");
            }
            try {
                for (int i = 1; i <= numVertices; ++i) { // Fill HashMap of vertices
                    String[] line = br.readLine().trim().split("\\s+"); // Split each line by whitespace
                    double[] coordinates = new double[DIMENSIONS];
                    for (int j = 0; j < DIMENSIONS; ++j) {
                        coordinates[j] = Double.parseDouble(line[j]);
                    }
                    vertices.put(i, new Point(coordinates));
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                throw new IOException("Coordinates could not be parsed.");
            }
            try { // Set number of shapes in wireframe
                numShapes = Integer.parseInt(br.readLine());
                shapes = new Shape2d[numShapes];
            } catch (NumberFormatException e) {
                throw new IOException("Number of shapes could not be parsed.");
            }
            try {
                for (int i = 0; i < numShapes; ++i) { // Fill Array of shapes
                    String[] line = br.readLine().trim().split("\\s+"); // Split each line by whitespace
                    Point[] shapeVertices = new Point[numPoints];
                    for (int j = 0; j < numPoints; ++j) {
                        shapeVertices[j] = vertices.get(Integer.parseInt(line[j])); // Add shapes by vertex references
                    }
                    shapes[i] = new Shape2d(shapeVertices);
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                throw new IOException("Vertices of shapes could not be parsed.");
            }
        } catch (FileNotFoundException e) {
            throw new IOException("Specified data file does not exist.");
        }
        return new Wireframe(vertices, shapes);
    }


    /**
     * Draws the Wireframe onto a canvas using draw() methods of each shape contained within it.
     *
     * @param canvas    Graphics2d canvas to draw onto
     * @param transform Transformation matrix to apply before drawing
     */
    public void draw(Graphics2D canvas, Matrix transform, Component panel) {
        BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_ARGB); // Display image
        double[] zBuffer = new double[image.getWidth()*image.getHeight()]; // Array to keep track of closest pixels
        Arrays.fill(zBuffer, -Double.MIN_VALUE);
        for (Shape2d shape : this.shapes) {
            shape.draw(canvas, transform);
        }
    }

    /**
     * Scales the coordinates of each vertex in the Wireframe by a specified factor.
     *
     * @param scaleFactor Factor to scale each vertex by
     */
    public void scale(double scaleFactor) {
        for (Point vertex : this.vertices.values()) {
            vertex.scale(scaleFactor);
        }
    }


}
