package sabertest.rss;

import org.junit.Test;
import saber.rss.RomeRss;
import saber.rss.RomeRssGenerator;

import java.util.Date;

public class RomeRssTest {

    public static RomeRss createRss(RomeRss.FeedType feedType) {
        RomeRss rss = RomeRss.on(feedType.toString(), "hello", "http://uux.me", "Hello, World! ");
        rss.addItem("文章1", "http://uux.me/1", new Date(), "文章1 的 描述");
        rss.addItem("文章2", "http://uux.me/2", new Date(), "文章2 的 描述");
        rss.addItem("文章3", "http://uux.me/3", new Date(), "文章3 的 描述");
        rss.addItem("文章4", "http://uux.me/4", new Date(), "文章4 的 描述");
        rss.addItem("文章5", "http://uux.me/5", new Date(), "文章5 的 描述");
        return rss;
    }

    @Test
    public void test1() throws Exception {
        RomeRss.FeedType feedType = RomeRss.FeedType.RSS_2_0;
        RomeRss rss = createRss(feedType);

        String outputString = rss.outputString();
        // System.out.println(outputString);

        RomeRss on = RomeRss.on(outputString, "utf-8");
        System.out.println(on.outputString());
    }

    @Test
    public void test2() throws Exception {
        RomeRss.FeedType feedType = RomeRss.FeedType.RSS_2_0;
        RomeRss rss = createRss(feedType);

        RomeRssGenerator generator = new RomeRssGenerator(true, "yyyy-MM-dd HH:mm:ss");
        System.out.println(rss.outputString(generator));
    }


}
