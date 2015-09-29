package net.concreet;

import net.Neuron;

import java.util.ArrayList;

/**
 * Created by avorona on 28.09.15.
 */
public class HiddenNeuron extends Neuron {

    private ArrayList<? extends HiddenNeuron> outer;
    private ArrayList<? extends Neuron> inner;
    private ArrayList<Double> weights;
    private ArrayList<Double> input;
    private Double mistake;

    private Double summedInput;
    private Double bias;

    @Override
    public double active() {
        if (this.getOutput() == null) {
            this.mistake = null;
            this.getInput().clear();
            this.getInner().stream().forEach(neuron -> this.getInput().add(neuron.active()));
            this.summedInput = HiddenNeuron.dotProduct(this.getInput(), this.getWeights()) + this.bias; //TODO apply bias
            this.setOutput(HiddenNeuron.sigmoid(this.summedInput));
        }
        return this.getOutput();
    }

    public double getMistake() {
        if (this.mistake == null) {
            this.mistake = this.getOuter().stream().mapToDouble(value -> {
                return value.getWeightFor(this) * value.getMistake();
            }).sum() * HiddenNeuron.sigmoidDeriative(this.getSummedInput());
        }
        return this.mistake;
    }

    public void updateWeightsAndBias(double learningRate) {
        for (int i = 0; i < this.getInner().size(); i++) {
            double oldWeight = this.getWeights().get(i);
            double newWeight = oldWeight + learningRate * this.getInput().get(i) * this.getMistake();
            this.getWeights().set(i, newWeight);
        }
        this.bias = this.bias + this.getMistake();
        this.summedInput = null;
        this.setOutput(null);
    }

    public void initialize(ArrayList<? extends Neuron> inner) {
        this.setInnerAndWeights(inner);
        this.bias = Math.random() - 0.5;
        this.input = new ArrayList<>(inner.size());
    }

    public Double getSummedInput() {
        return summedInput;
    }

    public void setSummedInput(Double summedInput) {
        this.summedInput = summedInput;
    }

    public ArrayList<? extends HiddenNeuron> getOuter() {
        return outer;
    }

    public void setOuter(ArrayList<? extends HiddenNeuron> outer) {
        this.outer = outer;
    }

    public ArrayList<? extends Neuron> getInner() {
        return inner;
    }

    public void setInner(ArrayList<? extends Neuron> inner) {
        this.inner = inner;
    }

    public ArrayList<Double> getWeights() {
        return weights;
    }

    public void setWeights(ArrayList<Double> weights) {
        this.weights = weights;
    }

    public ArrayList<Double> getInput() {
        return input;
    }

    public void setInput(ArrayList<Double> input) {
        this.input = input;
    }

    public double getWeightFor(Neuron concreteNeuron) {
        return this.weights.get(this.inner.indexOf(concreteNeuron));
    }

    public void setInnerAndWeights(ArrayList<? extends Neuron> inner) {
        this.inner = inner;
        ArrayList<Double> weights = new ArrayList<>(inner.size());
        for (int i = 0; i < inner.size(); i++) {
            weights.add(Math.random() - 0.5);
        }
        this.setWeights(weights);
    }

    public static double sigmoid(double in) {
        return 1/(1 + Math.exp(-in));
    }

    public static double sigmoidDeriative(double in) {
        return HiddenNeuron.sigmoid(in) * (1 - HiddenNeuron.sigmoid(in));
    }

    public static double dotProduct(ArrayList<? extends Number> first, ArrayList<? extends Number> second) {
        if (first.size() != second.size()) {
            throw new RuntimeException("DotProduct: cannot product arrays with different size");
        }
        double result = 0;
        for (int i = 0; i < first.size(); i++) {
            result += first.get(i).doubleValue() * second.get(i).doubleValue();
        }

        return result;
    }

}
