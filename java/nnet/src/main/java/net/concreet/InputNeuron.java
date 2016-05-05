package net.concreet;

import net.Neuron;

/**
 * Created by avorona on 28.09.15.
 */
public class InputNeuron extends Neuron {

    @Override
    public double active() {
        if (this.getOutput() == null) {
            throw new RuntimeException("InnerNeuron: please set my input to activate me.\nIn value is NULL.");
        }
        return this.getOutput();
    }

    public Double getIn() {
        return this.getOutput();
    }

    public void setIn(Double in) {
        this.setOutput(in);
    }
}
