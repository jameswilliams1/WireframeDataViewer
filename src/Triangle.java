import java.awt.*;
import java.util.Arrays;

/**
 * Represents a triangle in 3d space.
 */
public class Triangle {

    private double[] corner1; // x, y, z coordinates of each corner
    private double[] corner2;
    private double[] corner3;

    public Triangle(double[] corner1, double[] corner2, double[] corner3) {
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.corner3 = corner3;
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "corner1=" + Arrays.toString(corner1) +
                ", corner2=" + Arrays.toString(corner2) +
                ", corner3=" + Arrays.toString(corner3) +
                '}';
    }
//    public Polygon toPolygon(Triangle triangle){
//
//    }
}
