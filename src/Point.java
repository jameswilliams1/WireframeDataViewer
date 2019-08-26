/**
 * Represents a point in 3d space.
 */
public class Point {
    double x; // x, y, z cartesian coordinates
    double y;
    double z;

    public Point(double[] coordinates) { // x, y, z array of coordinates
        this.x = coordinates[0];
        this.y = coordinates[1];
        this.z = coordinates[2];
    }

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    /**
     * Subtract one point from another.
     *
     * @param point Point to subtract from this one.
     * @return Point
     */
    public Point subtract(Point point) {
        return new Point(this.x - point.x, this.y - point.y, this.z - point.z);
    }

    /**
     * Add one point to another.
     *
     * @param point Point to add to this one.
     * @return Point
     */
    public Point add(Point point) {
        return new Point(this.x + point.x, this.y + point.y, this.z + point.z);
    }

    /**
     * Dot product of two points.
     *
     * @param point Point to take dot product of.
     * @return double
     */
    public double dotProduct(Point point) {
        return this.x * point.x + this.y * point.y + this.z * point.z;
    }

    /**
     * Cross product of 2 points.
     *
     * @param point Point to take cross product of
     * @return Point
     */
    public Point crossProduct(Point point) {
        double x = this.y * point.z - point.y * this.z;
        double y = point.x * this.z - this.x * point.z;
        double z = this.x * point.y - point.x * this.y;
        return new Point(x, y, z);

    }
}