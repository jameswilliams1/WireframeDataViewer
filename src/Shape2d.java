import java.awt.*;
import java.awt.geom.Path2D;

/**
 * Represents a 2d shape in 3d space.
 */
public class Shape2d {

    private Point[] vertices; // Array of shape vertices as Point objects, must be in sequential order around perimeter of shape
    private Color lineColor;
    private Color fillColor;

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
    public void draw(Graphics2D canvas) {
        Path2D.Double path = new Path2D.Double();
        path.moveTo(this.vertices[0].getX(), this.vertices[0].getY()); // Start at first point
        for (int i = 1; i < this.vertices.length; ++i) {
            path.lineTo(this.vertices[i].getX(), this.vertices[i].getY()); // Draw a line to each subsequent point
        }
        path.closePath();
        canvas.setColor(this.lineColor);
        canvas.draw(path);
        canvas.setColor(this.fillColor);
        canvas.fill(path);
    }
}
