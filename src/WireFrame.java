import java.awt.*;
import java.awt.geom.Path2D;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Represents a wire frame of a 3d image constructed from 2d shapes.
 */
public class WireFrame {
    private static final int DIMENSIONS = 3; // Number of dimensions that specify each point
    private final Shape2d[] shapes; // Array of 2d shapes that make up the wire frame

    /**
     * Constructs a WireFrame from an array of Shape2d objects.
     *
     * @param shapes Array of Shape2d objects that make up the wire frame
     */
    private WireFrame(Shape2d[] shapes) {
        this.shapes = shapes;
    }

    WireFrame() { // Empty WireFrame
        this.shapes = new Shape2d[0];
    }

    /**
     * Parses a data file into a WireFrame object. Expects first line to be an int, n specifying number of vertices,
     * followed by n lines of three doubles specifying the x, y, z coordinates of each vertex. Then a line specifying
     * the number of shapes which make up the wire frame, followed by lines of three integers specifying which of the
     * previous vertices (1-n) make up the shape.
     *
     * @param dataFile  Path to file to read data from
     * @param numPoints Number of points that make up each shape, must be greater than 2
     * @return WireFrame object
     * @throws IOException If file cannot be parsed
     */
    public static WireFrame readShapesFromFile(File dataFile, int numPoints) throws IOException {
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
            try { // Set number of shapes in WireFrame
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
        return new WireFrame(shapes);
    }

    /**
     * Draws the WireFrame onto a canvas using triangles.
     *
     * @param canvas    Graphics2d canvas to draw onto
     * @param transform Transformation matrix to apply before drawing
     */
    public void draw(Graphics2D canvas, Matrix transform, Point viewVector) {
        for (Shape2d shape : this.shapes) {
            Point[] newVertices = Arrays.stream(shape.vertices).map(transform::transformPoint).toArray(Point[]::new); // Create new array of transformed points
            Point AB = newVertices[2].subtract(newVertices[1]); // Vectors of two triangle sides
            Point AC = newVertices[1].subtract(newVertices[0]);
            Point normal = AB.crossProduct(AC); // Normal of triangle
            double normalDotView = normal.dotProduct(viewVector);
            if (normalDotView > 0) { // Shape is facing camera
                Path2D.Double path = new Path2D.Double();
                path.moveTo(newVertices[0].x, newVertices[0].y); // Start at first point
                for (int i = 1; i < newVertices.length; ++i) {
                    path.lineTo(newVertices[i].x, newVertices[i].y); // Draw a line to each subsequent point
                }
                path.closePath();
                canvas.setColor(shape.lineColor);
                canvas.draw(path);
                canvas.setColor(shape.fillColor);
                canvas.fill(path);
            }
        }
    }
}