/**
 * Stores a 3x3 matrix with transformation methods.
 */
class Matrix {
    private static final int SIZE = 3;
    private double[][] values;

    /**
     * Construct a Matrix from a 2d array of doubles (must be a square matrix).
     *
     * @param values
     */
    Matrix(double[][] values) {
        this.values = values;
    }

    /**
     * Get a 3x3 XY rotation matrix for the specified angle (radians).
     *
     * @param angle Angle of rotation
     * @return Matrix (3x3)
     */
    public static Matrix getRotateXY(double angle) {
        double cosA = Math.cos(angle);
        double sinA = Math.sin(angle);
        double[][] values = {{cosA, -sinA, 0}, {sinA, cosA, 0}, {0, 0, 1}};
        return new Matrix(values);
    }

    /**
     * Get a 3x3 YZ rotation matrix for the specified angle (radians).
     *
     * @param angle Angle of rotation
     * @return Matrix (3x3)
     */
    public static Matrix getRotateYZ(double angle) {
        double cosA = Math.cos(angle);
        double sinA = Math.sin(angle);
        double[][] values = {{1, 0, 0}, {0, cosA, sinA}, {0, -sinA, cosA}};
        return new Matrix(values);
    }

    /**
     * Get a 3x3 XZ rotation matrix for the specified angle (radians).
     *
     * @param angle Angle of rotation
     * @return Matrix (3x3)
     */
    public static Matrix getRotateXZ(double angle) {
        double cosA = Math.cos(angle);
        double sinA = Math.sin(angle);
        double[][] values = {{cosA, 0, sinA}, {0, 1, 0}, {sinA, -0, cosA}};
        return new Matrix(values);
    }

    /**
     * Get a 3x3 matrix to scale by a factor of scaleFactor
     *
     * @param scaleFactor Factor to scale by
     * @return Matrix (3x3)
     */
    public static Matrix getScaleMatrix(double scaleFactor) {
        return new Matrix(new double[][]{{scaleFactor, 0, 0}, {0, scaleFactor, 0}, {0, 0, scaleFactor}});
    }

    /**
     * Multiply a matrix by another matrix, must be of equal size.
     *
     * @param otherMatrix Matrix to multiply by
     * @return Matrix
     */
    public Matrix multiply(Matrix otherMatrix) {
        double[][] result = new double[SIZE][SIZE];
        double sum;
        for (int i = 0; i < SIZE; ++i) {
            for (int j = 0; j < SIZE; ++j) {
                sum = 0;
                for (int k = 0; k < SIZE; ++k) {
                    sum += this.values[i][k] * otherMatrix.values[k][j];
                }
                result[i][j] = sum;
            }
        }
        return new Matrix(result);
    }

    public Point transformPoint(Point point) {
        double[] result = new double[SIZE];
        double[] p = new double[]{point.x, point.y, point.z};
        double sum;
        for (int i = 0; i < SIZE; ++i) {
            sum = 0;
            for (int k = 0; k < SIZE; ++k) {
                sum += this.values[i][k] * p[k];
            }
            result[i] = sum;
        }
        return new Point(result);
    }


}