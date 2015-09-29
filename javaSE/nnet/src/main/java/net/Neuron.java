package net;

/**
 * Created by avorona on 28.09.15.
 */
public abstract class Neuron {

    private Double output;

    public abstract double active();

    public Double getOutput() {
        return output;
    }

    public void setOutput(Double ouput) {
        this.output = ouput;
    }
}
