/**
 * Represents a point in 3d space.
 */
public class Point {
    protected double x; // x, y, z cartesian coordinates
    protected double y;
    protected double z;

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point(double[] coordinates) { // x, y, z array of coordinates
        this.x = coordinates[0];
        this.y = coordinates[1];
        this.z = coordinates[2];
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }


    public void scale(double scaleFactor) {
        this.x = x * scaleFactor;
        this.y = y * scaleFactor;
        this.z = z * scaleFactor;

    }
}
