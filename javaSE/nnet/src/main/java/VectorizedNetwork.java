import javafx.util.Pair;
import net.concreet.HiddenNeuron;
import org.apache.commons.lang3.ArrayUtils;
import util.MNISTImagesLoader;

import java.io.IOException;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;

/**
 * Created by avorona on 29.09.15.
 */
public class VectorizedNetwork {
    public static void main(String[] args) throws IOException {

        int[] netStructure = {784, 40, 10};

        double learningRate = 3.0;
        int epochs = 30;
        int learnTimes = 100;
        int miniBatchSize = 10;
        int testCount = 1;

        int testDataCount = 1000;
        int learnDataCount = 40000;


        ArrayList<Pair<double [], double[]>> learnData =
                MNISTImagesLoader.loadImages("train-images.idx3-ubyte",
                        "train-labels.idx1-ubyte", learnDataCount);
        System.out.println("Learning data loaded (" + learnDataCount + ")...");

        ArrayList<Pair<double [], double[]>> testData =
                MNISTImagesLoader.loadImages("t10k-images.idx3-ubyte",
                        "t10k-labels.idx1-ubyte", testDataCount);
        System.out.println("Testing data loaded (" + testDataCount + ")...");

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
        System.out.println("Network constructed: " + Arrays.toString(netStructure));


        for (int i = 0; i < epochs; i++) {
            for (int time = 0; time < learnTimes; time++) {
                double[][][] deltaWeights = weights.clone();
                double[][] deltaBiases = biases.clone();

                Set<Integer> randomLearningPos = new HashSet<>((int) (learnDataCount * 0.5));

                for (int s = 0; s < miniBatchSize; s++) {
                    int pos;
                    do {
                        pos = (int) Math.floor(Math.random() * learnDataCount);
                    } while (randomLearningPos.contains(pos));
                    randomLearningPos.add(pos);

                    double[][][][] back = VectorizedNetwork.backprop(learnData.get(pos).getKey(), weights, biases, learnData.get(pos).getValue());
                    double[][] nablaBiases = back[1][0];
                    double[][][] nablaWeights = back[0];
                    deltaBiases = VectorizedNetwork.updateBiases(deltaBiases, nablaBiases, VectorizedNetwork::arraysSum);
                    deltaWeights = VectorizedNetwork.updateWeights(deltaWeights, nablaWeights, VectorizedNetwork::arraysSum);
                }
                weights = VectorizedNetwork.updateWeights(weights, deltaWeights, (ws, dWs) -> {
                    return VectorizedNetwork.arraysDif(ws, VectorizedNetwork.arraysDo(dWs, d -> d * (learningRate / miniBatchSize)));
                });
                biases = VectorizedNetwork.updateBiases(biases, deltaBiases, (bs, dBs) -> {
                    return VectorizedNetwork.arraysDif(bs, VectorizedNetwork.arraysDo(dBs, d -> d * (learningRate / miniBatchSize)));
                });
            }
            System.out.println("Epoch #" + i + " ended");
            for (int t = 0; t < testCount; t++) {
                int pos = (int) Math.floor(Math.random() * testDataCount);

                double[] input = testData.get(pos).getKey();
                double[][] feedforwardResult = VectorizedNetwork.feedforward(input, weights, biases);
                double[] testResult = feedforwardResult[feedforwardResult.length - 1];

                List<Double> result = Arrays.asList(ArrayUtils.toObject(testResult));

                double max = result.parallelStream().max(Comparator.<Double>naturalOrder()).get();
                int resulDigit = result.indexOf(max);

                int digit =  ArrayUtils.indexOf(testData.get(pos).getValue(), 1);

                System.out.println("Test digit: " + digit);
                System.out.println("Network result: \t" + resulDigit);
            }

        }


    }

    public static double[][] feedforward(double[] input, double[][][] weights, double[][] biases) {
        if (input.length != weights[0][0].length) {
            throw new RuntimeException("FeedForward: cannot perform feedforward, because input size is not equal input neurons count");
        }
        double[][] activations = new double[weights.length + 1][];
        activations[0] = input;

        for (int i = 0; i < weights.length; i++) {
            double[][] layerWeights = weights[i];
            double[] curLayerBiases = biases[i];
            double[] layerActivations = new double[weights[i].length];
            for (int cnw = 0; cnw < layerWeights.length; cnw++) {
                double[] curNeuronWeights = layerWeights[cnw];
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
        if (weights[weights.length - 1].length != propperOutput.length) {
            throw new RuntimeException("BackProp: cannot back propagate, because proper output " +
                    "array size is different form output neurons");
        }
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
            double[][] d = new double[][]{delta};
            double[] sigDer = VectorizedNetwork.arraysDo(inputs[ir + 1], HiddenNeuron::sigmoidDeriative);
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

    public static double[][][] updateWeights(double[][][] weights, double[][][] nablaWeights, BinaryOperator<double[]> operation) {
        double[][][] result = new double[weights.length][][];
        for (int i = 0; i < weights.length; i++) {
            result[i] = new double[weights[i].length][];
            for (int j = 0; j < weights[i].length; j++) {
                result[i][j] = operation.apply(weights[i][j], nablaWeights[i][j]);
            }
        }
        return result;
    }

    public static double[][] updateBiases(double[][] biases, double[][] nablaBiases, BinaryOperator<double[]> operation) {
        double[][] result = new double[biases.length][];
        for (int i = 0; i < biases.length; i++) {
            result[i] = operation.apply(biases[i], nablaBiases[i]);
        }
        return result;
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
