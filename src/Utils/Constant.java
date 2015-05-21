package Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by neutronest on 15/5/8.
 */
public class Constant {

    public static String[] nodeFeatureNames = new String[]{"NodeEmoji", "PositiveWord", "NeutralWord", "NegativeWord"};
    public static String[] edgeFeatureNames = new String[]{"SameAuthor", "Similarity", "Difference",
            "SentimentProp", "AuthorRef", "HashTag", "SameEmoji", "FollowRoot"};
    public static int posDictLength = 345;
    public static int neuDictLength = 335;
    public static int negDictLength = 328;
    public static int sentimentDictLength = 1008;
    public static double minPtl = 0.0001; // use a small double to stand for 0.0 in potentials

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

    public static double[] NodeEmojiPtl = new double[]{
        1.0, Constant.minPtl, Constant.minPtl,
        1.0, 1.0, 1.0,
        Constant.minPtl, Constant.minPtl, 1.0
    };

    public static double[] Word2Ptl = new double[]{
        Constant.minPtl, Constant.minPtl, Constant.minPtl,
        Constant.minPtl, Constant.minPtl, 1.0
    };

    public static double[] Word1Ptl = new double[]{
        Constant.minPtl, Constant.minPtl, Constant.minPtl,
        Constant.minPtl, 1.0, Constant.minPtl
    };

    public static double[] Word0Ptl = new double[]{
        Constant.minPtl, Constant.minPtl, Constant.minPtl,
        1.0, Constant.minPtl, Constant.minPtl
    };

    public static double[] PositiveWordPtl = new double[]{
        Constant.minPtl, Constant.minPtl, Constant.minPtl,
        Constant.minPtl, Constant.minPtl, 1.0,
        Constant.minPtl, Constant.minPtl, 2.0,
        Constant.minPtl, Constant.minPtl, 3.0,
        Constant.minPtl, Constant.minPtl, 4.0,
        Constant.minPtl, Constant.minPtl, 5.0,
        Constant.minPtl, Constant.minPtl, 6.0,
        Constant.minPtl, Constant.minPtl, 7.0,
        Constant.minPtl, Constant.minPtl, 8.0,
        Constant.minPtl, Constant.minPtl, 9.0
    };

    public static double[] NeutralWordPtl = new double[]{
        Constant.minPtl, Constant.minPtl, Constant.minPtl,
        Constant.minPtl, 1.0, Constant.minPtl,
        Constant.minPtl, 2.0, Constant.minPtl,
        Constant.minPtl, 3.0, Constant.minPtl,
        Constant.minPtl, 4.0, Constant.minPtl,
        Constant.minPtl, 5.0, Constant.minPtl,
        Constant.minPtl, 6.0, Constant.minPtl,
        Constant.minPtl, 7.0, Constant.minPtl,
        Constant.minPtl, 8.0, Constant.minPtl,
        Constant.minPtl, 9.0, Constant.minPtl
    };

    public static double[] NegativeWordPtl = new double[]{
        Constant.minPtl, Constant.minPtl, Constant.minPtl,
        1.0, Constant.minPtl, Constant.minPtl,
        2.0, Constant.minPtl, Constant.minPtl,
        3.0, Constant.minPtl, Constant.minPtl,
        4.0, Constant.minPtl, Constant.minPtl,
        5.0, Constant.minPtl, Constant.minPtl,
        6.0, Constant.minPtl, Constant.minPtl,
        7.0, Constant.minPtl, Constant.minPtl,
        8.0, Constant.minPtl, Constant.minPtl,
        9.0, Constant.minPtl, Constant.minPtl,
    };

    public static double[] SamePtl = new double[]{
        Constant.minPtl, Constant.minPtl, Constant.minPtl, Constant.minPtl, Constant.minPtl, Constant.minPtl,
        Constant.minPtl, Constant.minPtl, Constant.minPtl, 1.0, Constant.minPtl, Constant.minPtl,
        Constant.minPtl, 1.0, Constant.minPtl, Constant.minPtl, Constant.minPtl, 1.0
    };

    public static double[] DiffPtl = new double[]{
        Constant.minPtl, Constant.minPtl, Constant.minPtl, Constant.minPtl, Constant.minPtl, Constant.minPtl,
        Constant.minPtl, Constant.minPtl, Constant.minPtl, Constant.minPtl, 1.0, 1.0,
        1.0, Constant.minPtl, 1.0, 1.0, 1.0, Constant.minPtl
    };
}
