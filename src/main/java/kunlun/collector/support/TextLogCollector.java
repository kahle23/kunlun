/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.collector.support;

import kunlun.common.constant.TimePatterns;
import kunlun.core.Collector;
import kunlun.time.DateUtils;
import kunlun.util.Assert;

import static kunlun.common.constant.Symbols.BLANK_SPACE;
import static kunlun.common.constant.Symbols.NEWLINE;

/**
 * The simple text log collector.
 * @author Kahle
 */
public class TextLogCollector implements Collector {
    private final StringBuilder content = new StringBuilder();
    private final String timePattern;

    public TextLogCollector(String timePattern) {
        Assert.notBlank(timePattern, "Parameter \"timePattern\" must not blank. ");
        this.timePattern = timePattern;
    }

    public TextLogCollector() {

        this(TimePatterns.DEFAULT_DATETIME);
    }

    public String getContent() {

        return String.valueOf(content);
    }

    @Override
    public Object collect(Object data, Object... arguments) {
        if (data == null) { return null; }
        content.append(DateUtils.format(timePattern))
                .append(BLANK_SPACE)
                .append(String.format(String.valueOf(data), arguments))
                .append(NEWLINE);
        return null;
    }

    @Override
    public String toString() {

        return getContent();
    }

}
