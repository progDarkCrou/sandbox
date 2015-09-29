package util;

import javafx.util.Pair;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by avorona on 28.09.15.
 */
public class MNISTImagesLoader {

    public static ArrayList<Pair<double [], double[]>> loadImages(String imagesFileName, String labelFileName, int amount) throws IOException {
        ArrayList<Pair<double [], double []>> result = new ArrayList<>();

        DataInputStream labels = new DataInputStream(new FileInputStream(labelFileName));
        DataInputStream images = new DataInputStream(new FileInputStream(imagesFileName));

        int magicNumber = labels.readInt();
        if (magicNumber != 2049) {
            System.err.println("Label file has wrong magic number: " + magicNumber + " (should be 2049)");
            System.exit(0);
        }
        magicNumber = images.readInt();
        if (magicNumber != 2051) {
            System.err.println("Image file has wrong magic number: " + magicNumber + " (should be 2051)");
            System.exit(0);
        }
        int numLabels = labels.readInt();
        int numImages = images.readInt();
        int numRows = images.readInt();
        int numCols = images.readInt();
        if (numLabels != numImages) {
            System.err.println("Image file and label file do not contain the same number of entries.");
            System.err.println("  Label file contains: " + numLabels);
            System.err.println("  Image file contains: " + numImages);
            System.exit(0);
        }

        long start = System.currentTimeMillis();
        int numLabelsRead = 0;
        while (labels.available() > 0 && numLabelsRead < numLabels && numLabelsRead < amount) {
            byte label = labels.readByte();
            numLabelsRead++;
            double [] img = new double[numCols * numRows];
            for (int i = 0; i < numCols * numRows; i++) {
                img[i]= images.readUnsignedByte() / 255.0;
            }

            // At this point, 'label' and 'image' agree and you can do whatever you like with them.
            double [] dig = new double[10];
            for (int i = 0; i < 10; i++) {
                dig[i] = i == label? 1: 0;
            }

            result.add(new Pair<>(img, dig));
        }

        return result;
    }
}
