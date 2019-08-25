import java.awt.*;
import java.awt.geom.Path2D;
import java.util.Arrays;

/**
 * Represents a 2d shape in 3d space.
 */
public class Shape2d {

    protected Point[] vertices; // Array of shape vertices as Point objects, must be in sequential order around perimeter of shape
    protected Color lineColor;
    protected Color fillColor;

    public Shape2d(Point[] vertices, Color lineColor, Color fillColor) {
        this.vertices = vertices;
        this.lineColor = lineColor;
        this.fillColor = fillColor;
    }

    public Shape2d(Point[] vertices) { // Use black lines with grey fill if not specified
        this.vertices = vertices;
        this.lineColor = Color.BLACK;
        this.fillColor = Color.GRAY;

    }

    /**
     * Draws a Shape2d onto a Graphics2d canvas using the set line and fill colors.
     *
     * @param canvas
     */
    public void draw(Graphics2D canvas, Matrix transform) {
        Path2D.Double path = new Path2D.Double();
        Point[] newVertices = Arrays.stream(this.vertices).map((Point p) -> transform.transformPoint(p)).toArray(Point[]::new); // Create new array of transformed points
        path.moveTo(newVertices[0].x, newVertices[0].y); // Start at first point
        for (int i = 1; i < newVertices.length; ++i) {
            path.lineTo(newVertices[i].x, newVertices[i].y); // Draw a line to each subsequent point
        }
        path.closePath();
        canvas.setColor(this.lineColor);
        canvas.draw(path);
        canvas.setColor(this.fillColor);
        canvas.fill(path);
    }

}
