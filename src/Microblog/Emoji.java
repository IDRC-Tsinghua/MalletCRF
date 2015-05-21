package Microblog;

import java.util.Arrays;

/**
 * Created by wyc on 2015/5/8.
 */
public class Emoji {
  private static String[] positiveEmoji = {"[哈哈]", "[偷笑]", "[嘻嘻]", "[爱你]", "[心]", "[挤眼]", "[抱抱]",
      "[鼓掌]", "[good]", "[花心]", "[赞]", "[给力]", "[酷]", "[害羞]", "[威武]", "[太阳]", "[亲爱的]",
      "[可爱]", "[兔子]", "[太开心]", "[耶]", "[亲亲]", "[蛋糕]", "[笑哈哈]", "[撒花]", "[不好意思]",
      "[可怜]", "[羊年大吉]", "[江南style]", "[偷乐]", "[得意地笑]", "[笑cry]", "[赞啊]", "[c期待]",
      "[馋嘴]", "[钱]", "[互粉]", "[ok]", "[礼物]", "[求关注]", "[好爱哦]", "[噢耶]", "[玫瑰]", "[xkl奔跑]",
      "[xkl撒花]", "[鲜花]", "[顶]", "[羞嗒嗒]", "[haha]", "[奥特曼]", "[lt切克闹]", "[握手]", "[好喜欢]",
      "[加油啊]", "[发红包]", "[星星眼儿]", "[马上有对象]", "[太開心]", "[擠眼]", "[親親]", "[许愿]", "[喜]",
      "[推荐]", "[可愛]", "[温暖帽子]", "[爱心传递]", "[女孩儿]", "[作揖]", "[拍手]", "[愛你]", "[新年快乐]",
      "[din推撞]", "[加油]", "[转发]", "[马到成功]", "[cc媚眼]", "[炸鸡和啤酒]", "[lxhx狂欢]", "[萌]",
      "[飞个吻]", "[给劲]", "[阳光]", "[亲一口]", "[呲牙]", "[喜欢]", "[hold住]", "[bm会心笑]", "[好棒]",
      "[干杯]", "[xkl转圈]", "[花]", "[lb撒欢]", "[互相膜拜]", "[右边亮了]", "[震撼]", "[c娇羞]", "[ali来吧]",
      "[带感]", "[bobo抛媚眼]", "[痞痞兔耶]"};
  private static String[] neutralEmoji = {"[doge]", "[泪]", "[呵呵]", "[阴险]", "[话筒]", "[围观]", "[din閃避]",
      "[喵喵]", "[带着微博去旅行]", "[思考]", "[月亮]", "[最右]", "[lt吃东西]", "[来]", "[电影]", "[猪头]", "[淚]",
      "[疑问]", "[熊猫]", "[微风]", "[浮云]", "[钟]", "[瞧瞧]", "[群体围观]", "[神马]", "[雪]", "[想一想]",
      "[睡觉]", "[飞机]"};
  private static String[] negativeEmoji = {"[抓狂]", "[挖鼻屎]", "[汗]", "[衰]", "[怒]", "[晕]", "[生病]",
      "[委屈]", "[鄙视]", "[泪流满面]", "[肥皂]", "[闭嘴]", "[懒得理你]", "[左哼哼]", "[右哼哼]", "[怒罵]",
      "[嘘]", "[吐]", "[打哈气]", "[拜拜]", "[困]", "[失望]", "[悲伤]", "[黑线]", "[哼]", "[抠鼻屎]",
      "[怒骂]", "[不要]", "[弱]", "[草泥马]", "[囧]", "[蜡烛]", "[巨汗]", "[崩溃]", "[打脸]", "[bm大哭]",
      "[傻眼]", "[吃惊]", "[惊讶]", "[伤心]", "[震惊]", "[非常汗]", "[好囧]", "[困死了]", "[别烦我]", "[感冒]",
      "[躁狂症]", "[bed凌乱]", "[c悲催]", "[震驚]", "[悲催]", "[不想上班]", "[怒駡]", "[抓沙发]", "[被电]",
      "[cai肚腩]", "[ala吐舌头]"};

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
