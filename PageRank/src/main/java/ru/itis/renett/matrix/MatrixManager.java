package ru.itis.renett.matrix;

public class MatrixManager {

    public static double[][] multiplyMatrices(double[][] matrixA, double[][] matrixB) {
        double[][] result = new double[matrixA.length][matrixB.length];

        for (int row = 0; row < result.length; row++) {
            for (int col = 0; col < result[row].length; col++) {
                result[row][col] = multiplyCell(matrixA, matrixB, row, col);
            }
        }

        return result;
    }

    private static double multiplyCell(double[][] matrixA, double[][] matrixB, int row, int col) {
        double cell = 0;

        for(int i = 0; i < matrixB.length; i++) {
            cell += matrixA[row][i] * matrixB[i][col];
        }

        return cell;
    }
}
