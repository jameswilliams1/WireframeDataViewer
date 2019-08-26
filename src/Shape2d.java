import java.awt.*;

/**
 * Represents a 2d shape in 3d space.
 */
class Shape2d {

    final Point[] vertices; // Array of shape vertices as Point objects, must be in sequential order around perimeter of shape
    Color lineColor;
    Color fillColor;

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
}
