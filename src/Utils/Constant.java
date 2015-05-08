package Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by neutronest on 15/5/8.
 */
public class Constant {

    public static final Map<String, String> featureFactorMap = new HashMap<String, String>();
    static {
        featureFactorMap.put("NodeEmoji", "Emoji");
        // TODO: Add more featureName~featureFactor Map
    }
}
