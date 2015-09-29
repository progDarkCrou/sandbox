import net.concreet.HiddenNeuron;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;

/**
 * Created by avorona on 29.09.15.
 */
public class VectorizedNetwork {
    public static void main(String[] args) throws IOException {
        int showStepPercent = 100;

        double learningRate = 1.0;
        int learningCount = 90000;
        int testOnCont = learningCount / showStepPercent;

        int testDataCount = 1000;
        int learnDataCount = 40000;

        int[] netStructure = {3, 5, 5, 3};

//        ArrayList<Pair<double [], double[]>> learnData =
//                MNISTImagesLoader.loadImages("train-images.idx3-ubyte",
//                        "train-labels.idx1-ubyte", learnDataCount);
//        System.out.println("Learning data loaded (" + learnDataCount + ")...");
//
//        ArrayList<Pair<double [], double[]>> testData =
//                MNISTImagesLoader.loadImages("t10k-images.idx3-ubyte",
//                        "t10k-labels.idx1-ubyte", testDataCount);
//        System.out.println("Testing data loaded (" + testDataCount + ")...");

        double[][][] weights = new double[netStructure.length - 1][][];
        double[][] biases = new double[netStructure.length - 1][];

        for (int i = netStructure.length - 1; i > 0; i--) {
            int curLayerNeuronCount = netStructure[i];
            double[][] layer = new double[curLayerNeuronCount][];
            double[] layerBiases = new double[curLayerNeuronCount];
            for (int nc = 0; nc < curLayerNeuronCount; nc++) {
                int prevLayerNeuronCount = netStructure[i - 1];
                double[] neuron = new double[prevLayerNeuronCount];
                for (int np = 0; np < prevLayerNeuronCount; np++) {
                    neuron[np] = Math.random() - 0.5;
                }
                layerBiases[nc] = Math.random() - 0.5;
                layer[nc] = neuron;
            }
            biases[i - 1] = layerBiases;
            weights[i - 1] = layer;
        }

        double[][] feedforward = VectorizedNetwork.feedforward(new double[]{1, 2, 3}, weights, biases);
        double[][][][] back = VectorizedNetwork.backprop(new double[]{1, 0, 0}, weights, biases, new double[]{1, 0, 0});

        double [][][] deltaWeights = back[0];
        double [][] deltaBiases = back[0][0];

        System.out.println(Arrays.toString(feedforward[feedforward.length - 1]));
    }

    public static double[][] feedforward(double[] input, double[][][] weights, double[][] biases) {
        if (input.length != weights[0][0].length) {
            throw new RuntimeException("FeedForward: cannot perform feedforward, because input size is not equal input neurons count");
        }
        double[][] activations = new double[weights.length + 1][];
        activations[0] = input;

        for (int i = 0; i < weights.length; i++) {
            double[][] layerWeights = weights[i];
            double[] layerActivations = new double[weights[i].length];
            double[] curLayerBiases = biases[i];
            for (int cnw = 0; cnw < layerWeights.length; cnw++) {
                double[] curNeuronWeights = new double[layerWeights[i].length];
                layerActivations[cnw] = HiddenNeuron.sigmoid(
                        VectorizedNetwork.dotProduct(curNeuronWeights, activations[i]) + curLayerBiases[cnw]);
            }
            activations[i + 1] = layerActivations;
        }

        return activations;
    }

    public static double[][][][] backprop(double[] input,
                                          double[][][] weights,
                                          double[][] biases,
                                          double[] propperOutput) {
        double[][][][] resultsContainer = new double[2][][][];

        double[][][] nablaW = new double[weights.length][][];
        double[][] nablaB = new double[biases.length][];

        double[][] inputs = new double[weights.length + 1][];
        inputs[0] = input;
        double[][] activations = new double[weights.length + 1][];
        activations[0] = input;

        for (int i = 0; i < weights.length; i++) {
            double[][] layerWeights = weights[i];
            double[] layerInputs = new double[weights[i].length];
            double[] layerActivations = new double[weights[i].length];
            double[] curLayerBiases = biases[i];
            for (int cnw = 0; cnw < layerWeights.length; cnw++) {
                double[] curNeuronWeights = new double[layerWeights[i].length];
                layerInputs[cnw] = VectorizedNetwork.dotProduct(curNeuronWeights, inputs[i]) + curLayerBiases[cnw];
                layerActivations[cnw] = HiddenNeuron.sigmoid(layerInputs[cnw]);
            }
            inputs[i + 1] = layerInputs;
            activations[i + 1] = layerActivations;
        }

        double[] delta = VectorizedNetwork.arraysMult(VectorizedNetwork.arraysDif(activations[activations.length - 1], propperOutput),
                VectorizedNetwork.arraysDo(inputs[inputs.length - 1], HiddenNeuron::sigmoidDeriative));
        nablaB[nablaB.length - 1] = delta;
        nablaW[nablaW.length - 1] = VectorizedNetwork.matrixMult(
                VectorizedNetwork.matrixTranspose(new double[][]{delta}), new double[][]{activations[activations.length - 2]});
        for (int ir = weights.length - 2; ir >= 0; ir--) {
            double [][] d = new double[][]{delta};
            double[] sigDer = VectorizedNetwork.arraysDo(inputs[ir+1], HiddenNeuron::sigmoidDeriative);
            double[][] weightsTransposed = VectorizedNetwork.matrixTranspose(weights[ir + 1]);
            double[][] deltaTransposed = VectorizedNetwork.matrixTranspose(d);
            double[][] multipliedMatrix = VectorizedNetwork.matrixMult(weightsTransposed, deltaTransposed);
            double[] deltaTimesNextWeights = VectorizedNetwork.matrixTranspose(multipliedMatrix)[0];

            delta = VectorizedNetwork.arraysMult(deltaTimesNextWeights, sigDer);
            nablaB[ir] = delta;
            nablaW[ir] = VectorizedNetwork.matrixMult(VectorizedNetwork.matrixTranspose(new double[][]{delta}), new double[][]{activations[ir]});
        }

        resultsContainer[0] = nablaW;
        resultsContainer[1] = new double[][][]{nablaB};
        return resultsContainer;
    }

    public static double dotProduct(double[] first, double[] second) {
        if (first.length != second.length) {
            throw new RuntimeException("DotProduct: cannot product arrays with different size");
        }
        double result = 0;
        for (int i = 0; i < first.length; i++) {
            result += first[i] * second[i];
        }

        return result;
    }

    public static double[] arraysSum(double[] first, double[] second) {
        double[] res = new double[first.length];

        for (int i = 0; i < first.length; i++) {
            res[i] = first[i] + second[i];
        }
        return res;
    }

    public static double[] arraysDif(double[] first, double[] second) {
        double[] res = new double[first.length];

        for (int i = 0; i < first.length; i++) {
            res[i] = first[i] - second[i];
        }
        return res;
    }

    public static double[] arraysMult(double[] first, double[] second) {
        double[] res = new double[first.length];

        for (int i = 0; i < first.length; i++) {
            res[i] = first[i] * second[i];
        }
        return res;
    }

    public static double[] arraysDo(double[] array, Function<Double, Double> fun) {
        double[] res = new double[array.length];

        for (int i = 0; i < array.length; i++) {
            res[i] = fun.apply(array[i]);
        }
        return res;
    }

    public static double[][] arrayToMatrix(double[] array) {
        double[][] matrix = new double[array.length][];
        for (int i = 0; i < array.length; i++) {
            matrix[i] = new double[]{array[i]};
        }
        return matrix;
    }

    public static double[][] matrixMult(double[][] first, double[][] second) {
        double[][] result = new double[first.length][second[0].length];
        double[][] secondTransposed = VectorizedNetwork.matrixTranspose(second);

        if (first[0].length == second.length) {
            for (int i = 0; i < first.length; i++) {
                double[] row = first[i];
                for (int j = 0; j < secondTransposed.length; j++) {
                    result[i][j] = VectorizedNetwork.dotProduct(row, secondTransposed[j]);
                }
            }
        }
        return result;
    }

    public static double[][] matrixTranspose(double[][] matrix) {
        double[][] res = new double[matrix[0].length][matrix.length];
        //Transposing
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                res[j][i] = matrix[i][j];
            }
        }
        return res;
    }

}
