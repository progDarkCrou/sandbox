package util;

import java.util.Arrays;
import java.util.function.Function;

/**
 * Created by avorona on 30.09.15.
 */
public class ArraysUtils {

    public static double[] arraysSum(double[] a, double[] s) {
        double[] res = new double[a.length];

        for (int i = 0; i < a.length; i++) {
            res[i] = a[i] + s[i];
        }
        return res;
    }

    public static double[] arraysDif(double[] a, double[] b) {
        if (a.length != b.length) {
            throw new RuntimeException("Cannot simultaneously subtract arrays with different sizes");
        }
        double[] res = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            res[i] = a[i] - b[i];
        }
        return res;
    }

    public static double[] arraysMul(double[] a, double[] b) {
        if (a.length != b.length) {
            throw new RuntimeException("Cannot simultaneously multiply arrays with different sizes.");
        }
        double[] res = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            res[i] = a[i] * b[i];
        }
        return res;
    }

    public static double[] arrayDo(double[] a, Function<Double, Double> f) {
        return Arrays.stream(a).map(f::apply).toArray();
    }

    public static double[][] matrixMul(double[][] a, double[][] b) {
        if (a[0].length != b.length) {
            throw new RuntimeException("Cannot multiply matrix. First matrix's width is not equal second matrix height.");
        }
        double[][] res = new double[a.length][b[0].length];
        double[][] bT = matrixTranspose(b);

        if (a[0].length == b.length) {
            for (int i = 0; i < a.length; i++) {
                double[] row = a[i];
                for (int j = 0; j < bT.length; j++) {
                    res[i][j] = ArraysUtils.dotProduct(row, bT[j]);
                }
            }
        }
        return res;
    }

    public static double[][] matrixTranspose(double[][] m) {
        double[][] res = new double[m[0].length][m.length];
        //Transposing
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                res[j][i] = m[i][j];
            }
        }
        return res;
    }

    public static double [][][] cloneEmpty(double[][][] a) {
        double [][][] res = new double[a.length][][];
        for (int i = 0; i < a.length; i++) {
            res[i] = cloneEmpty(a[i]);
        }
        return res;
    }

    public static double [][] cloneEmpty(double[][] a) {
        double [][] res = new double[a.length][];
        for (int i = 0; i < a.length; i++) {
            res[i] = new double[a[i].length];
        }
        return res;
    }

    public static double dotProduct(double[] a, double[] b) {
        if (a.length != b.length) {
            throw new RuntimeException("Cannot product arrays with different sizes.");
        }
        double res = 0;
        for (int i = 0; i < a.length; i++) {
            res += a[i] * b[i];
        }
        return res;
    }
}
