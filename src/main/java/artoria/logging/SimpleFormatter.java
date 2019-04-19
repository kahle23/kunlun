package artoria.logging;

import artoria.exception.ExceptionUtils;
import artoria.time.DateUtils;
import artoria.util.StringUtils;
import artoria.util.ThreadUtils;

import java.lang.management.ThreadInfo;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static artoria.common.Constants.*;

/**
 * Simple jdk logger formatter.
 * @author Kahle
 */
public class SimpleFormatter extends java.util.logging.SimpleFormatter {
    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final Integer MAX_SOURCE_LENGTH = 40;
    private static final Integer MAX_THREAD_LENGTH = 15;
    private static final Integer LEVEL_LENGTH = 7;
    private static final String REGEX_DOT = "\\.";
    private static final boolean CAN_COLORING;

    static {
        String stdoutEncoding = System.getProperty("sun.stdout.encoding");
        String stderrEncoding = System.getProperty("sun.stderr.encoding");
        String encoding = "ms936";
        CAN_COLORING = !encoding.equalsIgnoreCase(stdoutEncoding)
                && !encoding.equalsIgnoreCase(stderrEncoding);
    }

    private String coloring(String content, Integer beginColor, Integer endColor) {
        if (StringUtils.isBlank(content) || !CAN_COLORING) { return content; }
        return "\033[" + beginColor + "m" + content + "\033[" + endColor + "m";
    }

    private String fillBlank(String content, int wantLength) {
        int length;
        if ((length = content.length()) < wantLength) {
            StringBuilder builder = new StringBuilder(content);
            length = wantLength - length;
            for (int i = 0; i < length; i++) {
                builder.append(BLANK_SPACE);
            }
            content = builder.toString();
        }
        return content;
    }

    private String printfTime(LogRecord record) {
        long millis = record.getMillis();
        return DateUtils.format(millis, DEFAULT_DATE_PATTERN);
    }

    private String printfLevel(LogRecord record) {
        Level level = record.getLevel();
        if (level == null) { return EMPTY_STRING; }
        StringBuilder levelStr = new StringBuilder();
        levelStr.append(level.toString());
        while (levelStr.length() < LEVEL_LENGTH) {
            levelStr.append(BLANK_SPACE);
        }
        return levelStr.toString();
    }

    private String printfThread(LogRecord record) {
        int threadId = record.getThreadID();
        ThreadInfo threadInfo = ThreadUtils.getThreadInfo(threadId);
        String threadName = threadInfo != null ? threadInfo.getThreadName() : EMPTY_STRING;
        threadName = StringUtils.isNotBlank(threadName) ? threadName : EMPTY_STRING;
        int length;
        if ((length = threadName.length()) > MAX_THREAD_LENGTH) {
            threadName = threadName.substring(length - MAX_THREAD_LENGTH, length);
        }
        threadName = this.fillBlank(threadName, MAX_THREAD_LENGTH);
        threadName = LEFT_SQUARE_BRACKET + threadName + RIGHT_SQUARE_BRACKET;
        return threadName;
    }

    private String printfSource(LogRecord record) {
        String source = record.getSourceClassName();
        source = StringUtils.isBlank(source) ? record.getLoggerName() : source;
        source = StringUtils.isBlank(source) ? EMPTY_STRING : source;
        int length;
        if (source.length() > MAX_SOURCE_LENGTH) {
            StringBuilder builder = new StringBuilder();
            String[] split = source.split(REGEX_DOT);
            if ((length = split.length) > 1) {
                for (int i = 0; i < length; i++) {
                    String tmp = split[i];
                    if (i == length - 1) {
                        builder.append(tmp);
                        break;
                    }
                    if (tmp.length() > 0) {
                        builder.append(tmp.charAt(0));
                    }
                    builder.append(DOT);
                }
                source = builder.toString();
            }
        }
        if ((length = source.length()) > MAX_SOURCE_LENGTH) {
            source = source.substring(length - MAX_SOURCE_LENGTH, length);
        }
        source = this.fillBlank(source, MAX_SOURCE_LENGTH);
        return source;
    }

    private String printfMessage(LogRecord record) {
        String message = record.getMessage();
        return StringUtils.isNotBlank(message) ? message : EMPTY_STRING;
    }

    private String printfThrowable(LogRecord record) {
        Throwable thrown = record.getThrown();
        if (thrown == null) { return null; }
        String result = ExceptionUtils.toString(thrown);
        return StringUtils.isNotBlank(result) ? NEWLINE + result : null;
    }

    @Override
    public String format(LogRecord record) {
        String throwable = this.printfThrowable(record);
        return this.coloring(this.printfTime(record), 39, 39)
                + BLANK_SPACE
                + BLANK_SPACE
                + this.coloring(this.printfLevel(record), 32, 39)
                + BLANK_SPACE
                + this.coloring(this.printfThread(record), 39, 39)
                + BLANK_SPACE
                + this.coloring(this.printfSource(record), 36, 39)
                + BLANK_SPACE
                + COLON
                + BLANK_SPACE
                + this.coloring(this.printfMessage(record), 39, 39)
                + (
                    throwable != null
                        ? BLANK_SPACE + this.coloring(throwable, 39, 39)
                        : EMPTY_STRING
                )
                + NEWLINE;
    }

}
