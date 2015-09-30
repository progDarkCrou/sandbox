import imgLoader.ImageLoader;
import javafx.util.Pair;
import net.Network;
import org.apache.commons.lang3.ArrayUtils;
import util.MNISTImagesLoader;

import java.io.IOException;
import java.util.*;

/**
 * Created by avorona on 28.09.15.
 */
public class Main {
    public static void main(String[] args) throws IOException {

        int showStepPercent = 100;

        double learningRate = 1.0;
        int learningCount = 90000;
        int testOnCont = learningCount / showStepPercent;

        int testDataCount = 1000;
        int learnDataCount = 40000;

        int [] netStructure = {784, 100, 10};

        ImageLoader loader = new MNISTImagesLoader();

        ArrayList<Pair<double[], double[]>> learnData =
                loader.loadImages("train-images.idx3-ubyte",
                        "train-labels.idx1-ubyte", learnDataCount);
        System.out.println("Learning data loaded (" + learnDataCount + ")");

        ArrayList<Pair<double[], double[]>> testData =
                loader.loadImages("t10k-images.idx3-ubyte",
                        "t10k-labels.idx1-ubyte", testDataCount);
        System.out.println("Testing data loaded (" + testDataCount + ")");

        Network net = new Network(netStructure, learningRate);
        System.out.println("Network constructed: " + Arrays.toString(netStructure));

        Set<Integer> randomLearningPos = new HashSet<>((int) (learnDataCount * 0.5));

        int rightResults = 0;

        for (int i = 0; i < learningCount; i++) {
            int pos = 0;
            do {
                pos = (int) Math.floor(Math.random() * learnDataCount);
            } while (randomLearningPos.contains(pos));
            randomLearningPos.add(pos);

            if (randomLearningPos.size() > learnDataCount * 0.3) {
                randomLearningPos.clear();
            }

            net.learn(learnData.get(pos).getKey(), learnData.get(pos).getValue());
            if (i % (learningCount / showStepPercent) == 0) {
                System.out.println("Iteration #"+ i + ":");
                System.out.println(i / (learningCount / showStepPercent) + "% completed.");
            }
            if (i % testOnCont == 0) {
                int pos1 = (int) Math.floor(Math.random() * testDataCount);

                System.out.println("Iteration #" + i + " test results:");
                List<Double> result = Arrays.asList(ArrayUtils.toObject(net.procced(testData.get(pos1).getKey())));

                double max = result.parallelStream().max(Comparator.<Double>naturalOrder()).get();
                int resulDigit = result.indexOf(max);

                int digit =  ArrayUtils.indexOf(testData.get(pos1).getValue(), 1);

                System.out.println("Test digit: " + digit);
                System.out.println("Network result: \t" + resulDigit);

                if (digit == resulDigit) rightResults++;
            }
        }
        System.out.println("******************************************************");
        System.out.println("Learning results:");
        System.out.println("Test count: " + learningCount / testOnCont);
        System.out.println("Network right results count: " + rightResults);
        System.out.println("******************************************************");
    }
}
