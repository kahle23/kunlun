package sabertest.util;

import org.junit.Test;
import saber.util.Http;
import saber.util.RomeRss;

public class RomeRssTest {

    @Test
    public void test1() throws Exception {
        RomeRss rss = RomeRss.on("测试站点", "http://uux.me/feed", "这是测试站点吗？")
                .setFeedType(RomeRss.RssType.RSS_2_0);
        rss.addEntry("你好，世界！", "你好，世界！ 的 描述， 的 描述！ 啊啊啊")
                .addEntry("啊啊啊啊啊", "你阿斯顿撒旦飒飒的 描述！ 啊啊啊")
                .addEntry("额呃呃呃", "你好，啊是的撒大苏打述！ 啊啊啊");
        System.out.println(rss.outputString());
    }

    @Test
    public void test2() throws Exception {
        String xml = Http.on("http://uux.me/feed").get();
        RomeRss rss = RomeRss.on(xml, "utf-8");
        // System.out.println(rss.outputString());
        System.out.println(rss.getTitle());
        System.out.println(rss.getLink());
        System.out.println(rss.getDescription());
        System.out.println(rss.getFeed().getAuthors());
    }


}
