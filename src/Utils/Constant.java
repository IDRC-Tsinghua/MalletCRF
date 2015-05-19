package Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by neutronest on 15/5/8.
 */
public class Constant {

    public static String[] nodeFeatureNames = new String[]{"NodeEmoji"};
    public static String[] edgeFeatureNames = new String[]{"SameAuthor", "Similarity", "Difference",
            "SentimentProp", "AuthorRef", "HashTag", "SameEmoji", "FollowRoot"};
    public static int posDictLength = 345;
    public static int neuDictLength = 335;
    public static int negDictLength = 328;
    public static int sentimentDictLength = 1008;
    public static double minPtl = 0.001; // use a small double to stand for 0.0 in potentials

    public static final Map<String, String> featureFactorMap = new HashMap<String, String>();
    static {

        featureFactorMap.put("NodeEmoji", "NodeEmojiFactor");
        featureFactorMap.put("SameAuthor", "SameAuthorFactor");
        featureFactorMap.put("Sibling", "SiblingFactor");
        featureFactorMap.put("Similarity", "SimilarityFactor");
        featureFactorMap.put("Difference", "DifferenceFactor");
        featureFactorMap.put("SentimentProp", "SentimentPropFactor");
        featureFactorMap.put("AuthorRef", "AuthorRefFactor");
        featureFactorMap.put("Hashtag", "HashtagFactor");
        featureFactorMap.put("SameEmoji", "SameEmojiFactor");
        featureFactorMap.put("FollowRoot", "FollowRootFactor");
        // TODO: Add more featureName~featureFactor Map
    }


}
