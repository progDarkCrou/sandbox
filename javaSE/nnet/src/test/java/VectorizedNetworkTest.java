import imgLoader.ImageLoader;
import javafx.util.Pair;
import org.junit.BeforeClass;
import org.junit.Test;
import util.MNISTImagesLoader;

import java.util.ArrayList;

/**
 * Created by avorona on 30.09.15.
 */
public class VectorizedNetworkTest {

    static ArrayList<Pair<double[], double[]>> learnData;
    static ArrayList<Pair<double[], double[]>> testData;

    @BeforeClass
    public static void BeforeClass() {
        ImageLoader loader = new MNISTImagesLoader();

        int testDataCount = 1000;
        int learnDataCount = 40000;

        learnData =
                loader.loadImages("train-images.idx3-ubyte",
                        "train-labels.idx1-ubyte", learnDataCount);

        testData =
                loader.loadImages("t10k-images.idx3-ubyte",
                        "t10k-labels.idx1-ubyte", testDataCount);
    }

    @Test
    public void first() {
        int[] netStructure = {784, 40, 10};

        double learningRate = 3.0;

        VectorizedNetwork net = new VectorizedNetwork(netStructure);

        net.learn(learnData, learningRate, 30, 300, 10, testData);
    }

    @Test
    public void second() {
        int[] netStructure = {784, 20, 10};

        double learningRate = 3.0;

        VectorizedNetwork net = new VectorizedNetwork(netStructure);

        net.learn(learnData, learningRate, 30, 300, 10, testData);
    }

    @Test
    public void third() {
        int[] netStructure = {784, 100, 40, 10, 10};

        double learningRate = 3.0;

        VectorizedNetwork net = new VectorizedNetwork(netStructure);

        net.learn(learnData, learningRate, 30, 300, 10, testData);
    }

    @Test
    public void fourth() {
        int[] netStructure = {784, 100, 10};

        double learningRate = 3.0;

        VectorizedNetwork net = new VectorizedNetwork(netStructure);

        net.learn(learnData, learningRate, 30, 300, 10, testData);
    }
}
