package saber.rss;

import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Description;
import com.sun.syndication.feed.rss.Image;
import com.sun.syndication.feed.rss.Item;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.WireFeedGenerator;
import com.sun.syndication.io.WireFeedInput;
import com.sun.syndication.io.XmlReader;
import com.sun.syndication.io.impl.FeedGenerators;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.output.DOMOutputter;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RomeRss {
    private final static FeedGenerators GENERATORS = new FeedGenerators();

    public enum FeedType {
        RSS_0_90("rss_0.90", "0.90"),
        RSS_0_91("rss_0.91", "0.91"),
        RSS_0_92("rss_0.92", "0.92"),
        RSS_0_93("rss_0.93", "0.93"),
        RSS_0_94("rss_0.94", "0.94"),
        RSS_1_0("rss_1.0", "1.0"),
        RSS_2_0("rss_2.0", "2.0"),
        ATOM_0_3("atom_0.3", "0.3")
        ;

        public static FeedType search(String type) {
            FeedType[] values = FeedType.values();
            for (FeedType value : values) {
                if (value.type.equalsIgnoreCase(type)) return value;
            }
            return null;
        }

        private String type;
        private String version;

        FeedType(String type, String version) {
            this.type = type;
            this.version = version;
        }

        public String getType() {
            return type;
        }

        public String getVersion() {
            return version;
        }

        @Override
        public String toString() {
            return type;
        }

    }

    public static RomeRss on() {
        return new RomeRss();
    }

    public static RomeRss on(String feedType) {
        return new RomeRss().setFeedType(feedType);
    }

    public static RomeRss on(FeedType feedType) {
        return new RomeRss().setFeedType(feedType);
    }

    public static RomeRss on(String feedType, String title, String link, String description) {
        return new RomeRss().setFeedType(feedType)
                .setTitle(title)
                .setLink(link)
                .setDescription(description);
    }

    public static RomeRss on(InputStream in) throws IOException, FeedException {
        WireFeedInput input = new WireFeedInput();
        XmlReader xmlReader = new XmlReader(in);
        Channel channel = (Channel) input.build(xmlReader);
        return new RomeRss().setChannel(channel);
    }

    public static RomeRss on(String xml, String charsetName) throws IOException, FeedException {
        Charset charset = Charset.forName(charsetName);
        return RomeRss.on(new ByteArrayInputStream(xml.getBytes(charset)));
    }

    public static WireFeedGenerator createGenerator(String feedType) {
        return GENERATORS.getGenerator(feedType);
    }

    private Channel channel;
    private List<Item> items = new ArrayList<>();
    private WireFeedGenerator feedGenerator;

    private RomeRss() {
        this.channel = new Channel();
        this.channel.setItems(items);
    }

    private WireFeedGenerator feedGenerator() {
        if (feedGenerator == null ||
                !feedGenerator.getType().equalsIgnoreCase(channel.getFeedType())) {
            feedGenerator = GENERATORS.getGenerator(channel.getFeedType());
        }
        return feedGenerator;
    }

    @SuppressWarnings("unchecked")
    public RomeRss setChannel(Channel channel) {
        this.channel = channel;
        if (CollectionUtils.isNotEmpty(channel.getItems())) {
            items.addAll(channel.getItems());
            channel.setItems(items);
        }
        return this;
    }

    public Channel getChannel() {
        return channel;
    }

    public RomeRss setFeedGenerator(WireFeedGenerator feedGenerator) {
        this.feedGenerator = feedGenerator;
        return this;
    }

    public WireFeedGenerator getFeedGenerator() {
        return feedGenerator;
    }

    public RomeRss setFeedType(FeedType feedType) {
        channel.setFeedType(feedType.toString());
        return this;
    }

    public RomeRss setFeedType(String feedType) {
        channel.setFeedType(feedType);
        return this;
    }

    public String getFeedType() {
        return channel.getFeedType();
    }

    public RomeRss setEncoding(String encoding) {
        channel.setEncoding(encoding);
        return this;
    }

    public String getEncoding() {
        return channel.getEncoding();
    }

    public RomeRss setTitle(String title) {
        channel.setTitle(title);
        return this;
    }

    public String getTitle() {
        return channel.getTitle();
    }

    public RomeRss setLink(String link) {
        channel.setLink(link);
        return this;
    }

    public String getLink() {
        return channel.getLink();
    }

    public RomeRss setDescription(String description) {
        channel.setDescription(description);
        return this;
    }

    public String getDescription() {
        return channel.getDescription();
    }

    public RomeRss setGenerator(String generator) {
        channel.setGenerator(generator);
        return this;
    }

    public String getGenerator() {
        return channel.getGenerator();
    }

    public RomeRss setImage(Image image) {
        channel.setImage(image);
        return this;
    }

    public RomeRss setImage(String title, String url) {
        return setImage(title, url, null, null);
    }

    public RomeRss setImage(String title, String url, String link, String description) {
        Image image = new Image();
        image.setTitle(title);
        image.setUrl(url);
        if (StringUtils.isNotBlank(link)) image.setLink(link);
        if (StringUtils.isNotBlank(link)) image.setDescription(description);
        channel.setImage(image);
        return this;
    }

    public Image getImage() {
        return channel.getImage();
    }

    public RomeRss setItems(List<Item> items) {
        this.items = items;
        this.channel.setItems(items);
        return this;
    }

    public List<Item> getItems() {
        return items;
    }

    public RomeRss addItems(List<? extends Item> items) {
        this.items.addAll(items);
        return this;
    }

    public RomeRss addItem(Item item) {
        this.items.add(item);
        return this;
    }

    public RomeRss addItem(String title, String description) {
        Item item = new Item();
        item.setTitle(title);
        Description desc = new Description();
        desc.setValue(description);
        item.setDescription(desc);
        this.items.add(item);
        return this;
    }

    public RomeRss addItem(String title, String link, Date pubDate, String description) {
        Item item = new Item();
        item.setTitle(title);
        if (StringUtils.isNotBlank(link)) item.setLink(link);
        if (pubDate != null) item.setPubDate(pubDate);
        Description desc = new Description();
        desc.setValue(description);
        item.setDescription(desc);
        this.items.add(item);
        return this;
    }

    public Document outputJDom() throws FeedException {
        return feedGenerator().generate(channel);
    }

    public org.w3c.dom.Document outputW3CDom() throws FeedException {
        Document doc = outputJDom();
        DOMOutputter outputter = new DOMOutputter();
        try {
            return outputter.output(doc);
        }
        catch (JDOMException jdomEx) {
            throw new FeedException("Could not create DOM", jdomEx);
        }
    }

    public String outputString(Format format) throws FeedException {
        Document doc = outputJDom();
        XMLOutputter outputter = new XMLOutputter(format);
        return outputter.outputString(doc);
    }

    public String outputString() throws FeedException {
        String encoding = channel.getEncoding();
        Format format = Format.getPrettyFormat();
        if (encoding != null) {
            format.setEncoding(encoding);
        }
        return outputString(format);
    }

    public RomeRss output(Format format, Writer writer) throws FeedException, IOException {
        Document doc = outputJDom();
        XMLOutputter outputter = new XMLOutputter(format);
        outputter.output(doc, writer);
        return this;
    }

    public RomeRss output(Writer writer) throws FeedException, IOException {
        String encoding = channel.getEncoding();
        Format format = Format.getPrettyFormat();
        if (encoding!=null) {
            format.setEncoding(encoding);
        }
        output(format, writer);
        return this;
    }

    public RomeRss output(File file) throws IOException, FeedException {
        Writer writer = new FileWriter(file);
        output(writer);
        writer.close();
        return this;
    }

}
