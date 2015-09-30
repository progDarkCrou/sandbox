import imgLoader.ImageLoader;
import javafx.util.Pair;
import net.concreet.HiddenNeuron;
import org.apache.commons.lang3.ArrayUtils;
import util.ArraysUtils;
import util.MNISTImagesLoader;

import java.io.IOException;
import java.util.*;
import java.util.function.BinaryOperator;

/**
 * Created by avorona on 29.09.15.
 */
public class VectorizedNetwork {

    private double [][][] weights = null;
    private double [][] biases = null;

    public VectorizedNetwork(int [] netStructure) {

        this.weights = new double[netStructure.length - 1][][];
        this.biases = new double[netStructure.length - 1][];

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
        System.out.println();
    }

    public static void main(String[] args) throws IOException {

        int[] netStructure = {784, 40, 10};

        double learningRate = 3.0;
        int epochs = 30;
        int learnTimes = 100;
        int miniBatchSize = 10;
        int testCount = 1;

        int testDataCount = 1000;
        int learnDataCount = 40000;

        ImageLoader loader = new MNISTImagesLoader();

        ArrayList<Pair<double[], double[]>> learnData =
                loader.loadImages("train-images.idx3-ubyte",
                        "train-labels.idx1-ubyte", learnDataCount);

        ArrayList<Pair<double[], double[]>> testData =
                loader.loadImages("t10k-images.idx3-ubyte",
                        "t10k-labels.idx1-ubyte", testDataCount);

        VectorizedNetwork net = new VectorizedNetwork(netStructure);

    }

    public void learn(ArrayList<Pair<double[], double[]>> learnData,
                      double learningRate,
                      int epochs,
                      int epochIterations,
                      int miniBatchSize,
                      ArrayList<Pair<double[], double[]>> testData) {
        System.out.println("************** Learning started **************");
        for (int i = 0; i < epochs; i++) {
            for (int t = 0; t < epochIterations; t++) {
                double[][][] deltaWeights = ArraysUtils.cloneEmpty(this.weights);
                double[][] deltaBiases = ArraysUtils.cloneEmpty(this.biases);

                Set<Integer> randomLearningPos = new HashSet<>((int) (learnData.size() * 0.5));

                for (int s = 0; s < miniBatchSize; s++) {
                    int pos;
                    do {
                        pos = (int) Math.floor(Math.random() * learnData.size());
                    } while (randomLearningPos.contains(pos));
                    randomLearningPos.add(pos);

                    double[][][][] nablaWeightAndBiases = this.backpropagation(learnData.get(pos).getKey(), this.weights, this.biases, learnData.get(pos).getValue());
                    double[][] nablaBiases = nablaWeightAndBiases[1][0];
                    double[][][] nablaWeights = nablaWeightAndBiases[0];
                    deltaBiases = this.updateBiases(deltaBiases, nablaBiases, ArraysUtils::arraysSum);
                    deltaWeights = this.updateWeights(deltaWeights, nablaWeights, ArraysUtils::arraysSum);
                }
                this.weights = this.updateWeights(this.weights, deltaWeights, (ws, dWs) -> {
                    return ArraysUtils.arraysDif(ws, ArraysUtils.arrayDo(dWs, d -> d * (learningRate / miniBatchSize)));
                });
                this.biases = this.updateBiases(this.biases, deltaBiases, (bs, dBs) -> {
                    return ArraysUtils.arraysDif(bs, ArraysUtils.arrayDo(dBs, d -> d * (learningRate / miniBatchSize)));
                });
            }
            System.out.println("Epoch #" + i + " ended");
            if (testData != null) {
                this.test(testData);
            }
        }
        System.out.println("*************** Learning ended ***************");
    }

    public float test(ArrayList<Pair<double[], double[]>> testData) {
        System.out.print("Test: ");
        float rightResults = 0;
        for (int t = 0; t < testData.size(); t++) {

            double[] input = testData.get(t).getKey();
            double[][] feedforwardResult = this.feedforward(input, this.weights, this.biases);
            double[] testResult = feedforwardResult[feedforwardResult.length - 1];

            List<Double> result = Arrays.asList(ArrayUtils.toObject(testResult));

            double max = result.parallelStream().max(Comparator.<Double>naturalOrder()).get();
            int resultDigit = result.indexOf(max);

            int digit = ArrayUtils.indexOf(testData.get(t).getValue(), 1);
            if (digit == resultDigit) rightResults++;
        }
        System.out.print("tested elements " + testData.size() + ", " +
                "accuracy: " + String.format("%.2f", rightResults / testData.size() * 100) + "%");
        System.out.println();
        return rightResults / testData.size();
    }

    public double[][] feedforward(double[] input, double[][][] weights, double[][] biases) {
        if (input.length != weights[0][0].length) {
            throw new RuntimeException("Cannot perform feedforward, because input size is not equal input neurons count");
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
                        ArraysUtils.dotProduct(curNeuronWeights, activations[i]) + curLayerBiases[cnw]);
            }
            activations[i + 1] = layerActivations;
        }

        return activations;
    }

    private double[][][][] backpropagation(double[] input,
                                                 double[][][] weights,
                                                 double[][] biases,
                                                 double[] properOutput) {
        if (weights[weights.length - 1].length != properOutput.length) {
            throw new RuntimeException("BackProp: cannot back propagate, because proper output " +
                    "array size is different form output neurons");
        }
        double[][][][] resultsContainer = new double[2][][][];

        double[][][] nablaW = new double[weights.length][][];
        double[][] nablaB = new double[biases.length][];

        double[][] netInputs = new double[weights.length + 1][];
        netInputs[0] = input;
        double[][] netActivations = new double[weights.length + 1][];
        netActivations[0] = input;

        for (int i = 0; i < weights.length; i++) {
            double[][] currLayerWeights = weights[i];
            double[] currLayerBiases = biases[i];

            double[] layerInputs = new double[currLayerWeights.length];
            double[] layerActivations = new double[currLayerWeights.length];
            for (int cn = 0; cn < currLayerWeights.length && currLayerBiases.length == currLayerWeights.length; cn++) {
                double[] curNeuronWeights = currLayerWeights[cn];
                layerInputs[cn] = ArraysUtils.dotProduct(curNeuronWeights, netActivations[i]) + currLayerBiases[cn];
                layerActivations[cn] = HiddenNeuron.sigmoid(layerInputs[cn]);
            }
            netInputs[i + 1] = layerInputs;
            netActivations[i + 1] = layerActivations;
        }

        double[] errors = ArraysUtils.arraysMul(ArraysUtils.arraysDif(netActivations[netActivations.length - 1], properOutput),
                ArraysUtils.arrayDo(netInputs[netInputs.length - 1], HiddenNeuron::sigmoidDeriative));
        nablaB[nablaB.length - 1] = errors;
        nablaW[nablaW.length - 1] = ArraysUtils.matrixMul(
                ArraysUtils.matrixTranspose(new double[][]{errors}), new double[][]{netActivations[netActivations.length - 2]});
        for (int ir = weights.length - 2; ir >= 0; ir--) {
            double[][] errorsT = ArraysUtils.matrixTranspose(new double[][]{errors});
            double[][] nextWeightsT = ArraysUtils.matrixTranspose(weights[ir + 1]);
            double[] errorsByNextWeights = ArraysUtils.matrixTranspose(ArraysUtils.matrixMul(nextWeightsT, errorsT))[0];

            double[] sigmoidDerivatives = ArraysUtils.arrayDo(netInputs[ir + 1], HiddenNeuron::sigmoidDeriative);
            errors = ArraysUtils.arraysMul(errorsByNextWeights, sigmoidDerivatives);
            nablaB[ir] = errors;
            nablaW[ir] = ArraysUtils.matrixMul(ArraysUtils.matrixTranspose(new double[][]{errors}), new double[][]{netActivations[ir]});
        }

        resultsContainer[0] = nablaW;
        resultsContainer[1] = new double[][][]{nablaB};
        return resultsContainer;
    }

    private double[][][] updateWeights(double[][][] w, double[][][] nablaW, BinaryOperator<double[]> o) {
        double[][][] res = ArraysUtils.cloneEmpty(w);
        for (int i = 0; i < w.length; i++) {
            for (int j = 0; j < w[i].length; j++) {
                res[i][j] = o.apply(w[i][j], nablaW[i][j]);
            }
        }
        return res;
    }

    private double[][] updateBiases(double[][] b, double[][] nablaB, BinaryOperator<double[]> o) {
        double[][] r = ArraysUtils.cloneEmpty(b);
        for (int i = 0; i < b.length; i++) {
            r[i] = o.apply(b[i], nablaB[i]);
        }
        return r;
    }


}
