package app;

public class Matrix {
    // Zeilen der Matrix
    private final int rows;
    // Spalten der Matrix
    private final int cols;
    //2D-Array --> enthält die tatsächlichen Matrix-Werten
    private final double[][] data;

    //Erstellt eine Matrix mit angegeben Zeilen und Spalten
    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }
    //Initialisiert das data-Array mit den Werten des übergebenen Arrays.
    public Matrix(double[][] data) {
        this.rows = data.length;
        this.cols = data[0].length;
        this.data = new double[rows][cols];
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                this.data[row][col] = data[row][col];
            }
        }
    }

    //gibt denn Wert an der angegebenen Spalte, Zeile in der Matrix zurück
    public double get(int col, int row) {
        return this.data[row][col];
    }

    // aktuelle Matrix * anderer Matrix = neue Matrix
    //Wenn Dimension nicht passen, dann Exception
    public Matrix multiply(Matrix other) throws Exception {
        if (this.cols != other.rows) {
            throw new Exception("The dimensions of the matrix do not match");
        }

        Matrix result = new Matrix(this.rows, other.cols);

        for (int row = 0; row < result.rows; row++) {
            for (int col = 0; col < result.cols; col++) {
                for (int i = 0; i < this.cols; i++) {
                    result.data[row][col] += this.data[row][i] * other.data[i][col];
                }
            }
        }

        return result;
    }
    //Vektor wird in Matrix 4x1 umgewandelt
    //Matrix wird multipliziert mit der Matrix die die Methode aufruft
    //Ersten beiden Ergebnisse der Matrix multiplikation werden als x,y Koor benutzt z = 0 da 2D-Ebene
    public Vector doScreenProjection(Vector v) throws Exception {
        Matrix vm = new Matrix(new double[][]{
                {v.x()},
                {v.y()},
                {v.z()},
                {1.0}
        });

        Matrix result = this.multiply(vm);
        return new Vector(
                result.data[0][0],
                result.data[1][0],
                0
        );
    }

    //Gibt Matrix als String aus mit der richtigen Formatierung
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[\n");

        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                sb.append(this.data[row][col]).append(" ");
            }
            sb.append("\n");
        }
        sb.append("]");

        return sb.toString();
    }
}
