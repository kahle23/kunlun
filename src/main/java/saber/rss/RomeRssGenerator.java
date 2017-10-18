package saber.rss;

import com.sun.syndication.feed.rss.Item;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.impl.RSS20Generator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jdom.CDATA;
import org.jdom.Element;

import java.util.Date;

public class RomeRssGenerator extends RSS20Generator {
    private boolean doCData;
    private String pattern;

    public RomeRssGenerator(boolean doCData, String pattern) {
        this("rss_2.0","2.0", doCData, pattern);
    }

    public RomeRssGenerator(String feedType, String version, boolean doCData, String pattern) {
        super(feedType, version);
        this.doCData = doCData;
        this.pattern = pattern;
    }

    protected void addItem(Item item, Element parent, int index) throws FeedException {
        Element eItem = new Element("item", getFeedNamespace());
        populateItem(item,eItem, index);
        checkItemConstraints(eItem);
        generateItemModules(item.getModules(),eItem);

        if (doCData) {
            Element desc = eItem.getChild("description");
            String text = desc.getValue();
            desc.setContent(new CDATA(text));
        }

        parent.addContent(eItem);
    }

    public void populateItem(Item item, Element eItem, int index) {
        super.populateItem(item, eItem, index);

        Date pubDate = item.getPubDate();
        if (pubDate != null && StringUtils.isNotBlank(pattern)) {
            eItem.removeChild("pubDate");
            eItem.addContent(generateSimpleElement("pubDate", DateFormatUtils.format(pubDate, pattern)));
        }

    }

}
