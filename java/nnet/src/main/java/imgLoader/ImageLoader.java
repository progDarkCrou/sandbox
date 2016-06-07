package imgLoader;

import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by avorona on 30.09.15.
 */
public interface ImageLoader {
    ArrayList<Pair<double [], double[]>> loadImages(String imagesFileName, String labelFileName, int amount);
}
