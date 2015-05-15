package Microblog;

import java.util.Arrays;

/**
 * Created by wyc on 2015/5/8.
 */
public class Emoji {
  private static String[] positiveEmoji = {"[哈哈]", "[偷笑]", "[嘻嘻]", "[爱你]", "[心]", "[挤眼]", "[抱抱]",
      "[鼓掌]", "[good]", "[花心]", "[赞]", "[给力]", "[酷]", "[害羞]", "[威武]",
      "[可爱]", "[兔子]", "[太开心]", "[耶]", "[亲亲]", "[蛋糕]", "[笑哈哈]", "[撒花]",
      "[可怜]", "[羊年大吉]", "[江南style]", "[偷乐]", "[得意地笑]", "[笑cry]",
      "[馋嘴]", "[钱]", "[互粉]", "[ok]", "[礼物]", "[求关注]", "[好爱哦]", "[噢耶]"};
  private static String[] neutralEmoji = {"[doge]", "[泪]", "[呵呵]", "[阴险]", "[话筒]"};
  private static String[] negativeEmoji = {"[抓狂]", "[挖鼻屎]", "[汗]", "[衰]", "[怒]", "[晕]", "[生病]",
      "[委屈]", "[鄙视]", "[泪流满面]", "[肥皂]", "[闭嘴]", "[懒得理你]", "[左哼哼]", "[右哼哼]",
      "[嘘]", "[吐]", "[打哈气]", "[拜拜]", "[困]", "[失望]", "[悲伤]", "[黑线]", "[哼]",
      "[怒骂]", "[不要]", "[弱]", "[草泥马]", "[囧]", "[蜡烛]", "[巨汗]", "[崩溃]", "[打脸]"};

  public static int getEmojiLabel(String emoji) {
    if (Arrays.asList(positiveEmoji).contains(emoji))
      return 1;
    else if (Arrays.asList(neutralEmoji).contains(emoji))
      return 0;
    else if (Arrays.asList(negativeEmoji).contains(emoji))
      return -1;
    else {
      System.out.println("Lost emoji: " + emoji);
      return 0;
    }
  }
}
