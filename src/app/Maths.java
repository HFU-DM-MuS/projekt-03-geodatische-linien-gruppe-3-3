package app;

public class Maths {
    public static double[] multMatVec(double[][] m, double[] v) {

        double[] ResultMatrix = new double[m.length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                ResultMatrix[i] += m[i][j] * v[j];
            }
        }
        return ResultMatrix;

    }
    public static double[] subtractVectors(double[] vector1, double[] vector2) {

        double[] result = new double[vector1.length];
        for (int i = 0; i <= vector1.length-1; i++) {
            result[i] = vector1[i] - vector2[i];
        }
        return result;
    }
    public static double cosineOfAngleBetweenVectors(double[] vector1, double[] vector2) {
        double dotProduct = 0;
        double magnitudeP = 0;
        double magnitudeQ = 0;
        for (int i = 0; i <= vector1.length-1; i++) {
            dotProduct += vector1[i] * vector2[i];
            magnitudeP += vector1[i] * vector1[i];
            magnitudeQ += vector2[i] * vector2[i];
        }
        magnitudeP = Math.sqrt(magnitudeP);
        magnitudeQ = Math.sqrt(magnitudeQ);
        return dotProduct / (magnitudeP * magnitudeQ);

    }
    public static double[] normalizeVector(double[] vector) {
        double magnitude = 0;
        for (int i = 0; i <= vector.length-1; i++) {
            magnitude += vector[i] * vector[i];
        }
        magnitude = Math.sqrt(magnitude);

        double[] normalized = new double[vector.length];
        for (int i = 0; i <= vector.length-1; i++) {
            normalized[i] = vector[i] / magnitude;
        }
        return normalized;
    }
    public static double[] crossProduct(double[] vector1, double[] vector2) {

        double[] result = new double[vector1.length];
        result[0] = vector1[1] * vector2[2] - vector1[2] * vector2[1];
        result[1] = vector1[2] * vector2[0] - vector1[0] * vector2[2];
        result[2] = vector1[0] * vector2[1] - vector1[1] * vector2[0];
        return result;
    }
    public static double crossProductMagnitude(double[] vector1, double[] vector2) {

        double[] result = new double[vector1.length];
        result[0] = vector1[1] * vector2[2] - vector1[2] * vector2[1];
        result[1] = vector1[2] * vector2[0] - vector1[0] * vector2[2];
        result[2] = vector1[0] * vector2[1] - vector1[1] * vector2[0];
        return Math.sqrt(result[0] * result[0] + result[1] * result[1] + result[2] * result[2]);

    }
    public static double[] divVecWithNumber(double[] vector, double number){

        double[] result = new double[vector.length];

        for(int i = 0; i <= vector.length-1; i++){

            result[i] = vector[i]/number;

        }

        return result;
    }
}
