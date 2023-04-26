package app;

public class Vector {
    private double x, y, z;

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double x() {
        return this.x;
    }

    public double y() {
        return this.y;
    }

    public double z() {
        return this.z;
    }


    public Vector rotateWorldZ(double angle) {

        angle = Math.toRadians(angle);

        Matrix rotMat = new Matrix(new double[][]{
                {Math.cos(angle), -Math.sin(angle), 0.0},
                {Math.sin(angle), Math.cos(angle), 0.0},
                {0.0, 0.0, 1.0}
        });

        this.rotateWithMatrix(rotMat);

        return this;
    }


    @Override
    public String toString() {
        return String.format("(%.2f, %.2f, %.2f)", this.x, this.y, this.z);
    }

    private void rotateWithMatrix(Matrix m) {
        try {
            Matrix rotated = m.multiply(new Matrix(new double[][]{
                    {this.x},
                    {this.y},
                    {this.z}
            }));

            this.x = rotated.get(0, 0);
            this.y = rotated.get(0, 1);
            this.z = rotated.get(0, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Vector rotateWorldY(double angle) {

        angle = Math.toRadians(angle);

        Matrix rotMat = new Matrix(new double[][]{
                {Math.cos(angle), 0.0, Math.sin(angle)},
                {0.0, 1.0, 0.0},
                {-Math.sin(angle), 0.0, Math.cos(angle)}
        });

        this.rotateWithMatrix(rotMat);

        return this;
    }


    public Vector scale(double f) {
        this.x *= f;
        this.y *= f;
        this.z *= f;

        return this;
    }
}
