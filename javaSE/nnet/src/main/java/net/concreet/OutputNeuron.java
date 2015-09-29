package net.concreet;

/**
 * Created by avorona on 28.09.15.
 */
public class OutputNeuron extends HiddenNeuron {

    private Double properOutput;
    private Double mistake;

    @Override
    public double active() {
        this.mistake = null;
        return super.active();
    }

    @Override
    public double getMistake() {
        if (this.mistake == null) {
            if (this.properOutput == null) {
                throw new RuntimeException("OutputNeuron: cannot compute mistake. Proper output is NULL.");
            }
            this.mistake = (this.properOutput - this.getOutput()) * HiddenNeuron.sigmoidDeriative(this.getSummedInput());
        }
        return this.mistake;
    }

    @Override
    public void updateWeightsAndBias(double learningRate) {
        super.updateWeightsAndBias(learningRate);
        this.setOutput(null);
        this.properOutput = null;
    }

    public Double getProperOutput() {
        return properOutput;
    }

    public void setProperOutput(Double properOutput) {
        this.properOutput = properOutput;
    }
}
