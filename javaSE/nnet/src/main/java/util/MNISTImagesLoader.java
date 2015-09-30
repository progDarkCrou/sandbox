package util;

import imgLoader.ImageLoader;
import javafx.util.Pair;
import org.apache.commons.lang3.StringUtils;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by avorona on 28.09.15.
 */
public class MNISTImagesLoader implements ImageLoader {

    public ArrayList<Pair<double[], double[]>> loadImages(String imagesFileName, String labelFileName, int amount) {
        ArrayList<Pair<double[], double[]>> result = new ArrayList<>();

        DataInputStream labels = null;
        DataInputStream images = null;
        try {
            labels = new DataInputStream(new FileInputStream(labelFileName));
            images = new DataInputStream(new FileInputStream(imagesFileName));

            int magicNumber = labels.readInt();
            if (magicNumber != 2049) {
                throw new RuntimeException("Label file has wrong magic number: " + magicNumber + " (should be 2049)");
            }
            magicNumber = images.readInt();
            if (magicNumber != 2051) {
                throw new RuntimeException("Image file has wrong magic number: " + magicNumber + " (should be 2051)");
            }
            int numLabels = labels.readInt();
            int numImages = images.readInt();
            int numRows = images.readInt();
            int numCols = images.readInt();
            if (numLabels != numImages) {
                throw new RuntimeException("Image file and label file do not contain the same number of entries." +
                        "\n\tLabel file contains: " + numLabels +
                        "\n\tImage file contains: " + numImages);
            }

            long start = System.currentTimeMillis();
            System.out.println("Images loading started: " + imagesFileName);

            int numLabelsRead = 0;
            int percentage = 40;

            while (labels.available() > 0 && numLabelsRead < numLabels && numLabelsRead < amount) {
                byte label = labels.readByte();
                numLabelsRead++;
                double[] img = new double[numCols * numRows];
                for (int i = 0; i < numCols * numRows; i++) {
                    img[i] = images.readUnsignedByte() / 255.0;
                }

                // At this point, 'label' and 'image' agree and you can do whatever you like with them.
                double[] dig = new double[10];
                for (int i = 0; i < 10; i++) {
                    dig[i] = i == label ? 1 : 0;
                }
                if (numLabelsRead % (amount / percentage) == 0) {
                    System.out.print("\r[" + StringUtils.repeat("=", numLabelsRead / (amount / percentage)) +
                            StringUtils.repeat(" ", percentage - (numLabelsRead / (amount / percentage))) + "]");
                }
                result.add(new Pair<>(img, dig));
            }
            long time = (System.currentTimeMillis() - start) / 1000;
            System.out.println();
            System.out.println("Images loaded (" + amount + ") in " + time + " seconds.");
            System.out.println();
            return result;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cannot find one of two desired files. Please check if they exist.", e);
        } catch (IOException e) {
            throw new RuntimeException("Something goes wrong while reading files.", e);
        } finally {
            if (images != null) {
                try {
                    images.close();
                } catch (IOException e) {
                    throw new RuntimeException("Cannot close images file. Please check it out.", e);
                }
            }
            if (labels != null) {
                try {
                    labels.close();
                } catch (IOException e) {
                    throw new RuntimeException("Cannot close labels file. Please check it out.", e);
                }
            }
        }

    }
}
