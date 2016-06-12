package net;

import net.concreet.HiddenNeuron;
import net.concreet.InputNeuron;
import net.concreet.OutputNeuron;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by avorona on 28.09.15.
 */
public class Network {

    private ArrayList<ArrayList<? extends Neuron>> net;
    private ArrayList<InputNeuron> inputLayer;
    private ArrayList<OutputNeuron> outputLayer;
    private Double learningRate;


    public Network(int[] netStructure, double learningRate) {
        int inputNeuronsCount = netStructure[0];
        int [] hiddenNeuronsCounts = Arrays.copyOfRange(netStructure, 1, netStructure.length - 1);
        int outputNeuronsCount = netStructure[netStructure.length - 1];

        this.learningRate = learningRate;
        this.net = new ArrayList<>(netStructure.length);
        this.inputLayer = new ArrayList<>();
        this.outputLayer = new ArrayList<>();

        for (int i = 0; i < inputNeuronsCount; i++) {
            this.inputLayer.add(new InputNeuron());
        }
        this.net.add(this.inputLayer);

        for (int i = 0; i < hiddenNeuronsCounts.length; i++) {
            ArrayList<Neuron> hiddenLayer = new ArrayList<>(hiddenNeuronsCounts[i]);
            for (int j = 0; j < hiddenNeuronsCounts[i]; j++) {
                hiddenLayer.add(new HiddenNeuron());
            }
            this.net.add(hiddenLayer);
        }

        for (int i = 0; i < outputNeuronsCount; i++) {
            this.outputLayer.add(new OutputNeuron());
        }
        this.net.add(this.outputLayer);

        for (int i = 1; i < this.net.size() - 1; i++) {
            ArrayList<HiddenNeuron> neurons = (ArrayList<HiddenNeuron>) this.net.get(i);
            ArrayList<HiddenNeuron> neurons2 = (ArrayList<HiddenNeuron>) this.net.get(i + 1);
            neurons2.stream().forEach(neuron -> neuron.initialize(neurons));
            neurons.stream().forEach(neuron -> neuron.setOuter(neurons2));
            if (i == 1) {
                neurons.stream().forEach(neuron -> neuron.initialize(this.inputLayer));
            }
        }
    }

    public double[] learn(double[] input, double[] desireOutput) {
        if (desireOutput.length != outputLayer.size()) {
            throw new RuntimeException("Network: desire array size differs from the output layer's neurons count.");
        }
        double [] result = this.procced(input);
        for (int i = 0; i < desireOutput.length; i++) {
            this.outputLayer.get(i).setProperOutput(desireOutput[i]);
        }
        for (int i = this.net.size() - 1; i > 0; i--) {
            this.net.get(i).parallelStream().forEach(neuron -> ((HiddenNeuron) neuron).updateWeightsAndBias(learningRate));
        } //Most of the learning process is done here
        return result;
    }

    public double[] procced(double[] input) {
        if (input.length != inputLayer.size()) {
            throw new RuntimeException("Network: input array size differs from the input layer's neurons count.");
        }

        for (int i = 0; i < input.length; i++) {
            this.inputLayer.get(i).setIn(input[i]);
        }

        return this.outputLayer.parallelStream().mapToDouble(outputNeuron -> outputNeuron.active()).toArray();
    }

    private void backprop() {

    }

    public Double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(Double learningRate) {
        this.learningRate = learningRate;
    }


}
