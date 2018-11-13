package artoria.logging;

import artoria.exception.ExceptionUtils;
import artoria.time.DateUtils;
import artoria.util.StringUtils;
import artoria.util.ThreadUtils;

import java.lang.management.ThreadInfo;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static artoria.common.Constants.*;

/**
 * Simple jdk logger formatter.
 * @author Kahle
 */
public class SimpleFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {
        String throwable = this.printfThrowable(record);
        return this.printfTime(record) + BLANK_SPACE
                + this.printfLevel(record) + BLANK_SPACE
                + LEFT_SQUARE_BRACKET + this.printfThread(record)
                + RIGHT_SQUARE_BRACKET + BLANK_SPACE
                + this.printfSource(record) + BLANK_SPACE
                + COLON + BLANK_SPACE + this.printfMessage(record)
                + (throwable != null ? BLANK_SPACE
                + throwable : EMPTY_STRING) + NEWLINE;
    }

    private String printfTime(LogRecord record) {
        long millis = record.getMillis();
        return DateUtils.format(new Date(millis));
    }

    private String printfLevel(LogRecord record) {
        Level level = record.getLevel();
        if (level == null) { return EMPTY_STRING; }
        StringBuilder levelStr = new StringBuilder();
        levelStr.append(level.toString());
        while (levelStr.length() < 7) {
            levelStr.append(BLANK_SPACE);
        }
        return levelStr.toString();
    }

    private String printfThread(LogRecord record) {
        int threadId = record.getThreadID();
        ThreadInfo threadInfo = ThreadUtils.getThreadInfo(threadId);
        if (threadInfo == null) { return EMPTY_STRING; }
        String threadName = threadInfo.getThreadName();
        return StringUtils.isNotBlank(threadName) ? threadName : EMPTY_STRING;
    }

    private String printfSource(LogRecord record) {
        String source = record.getSourceClassName();
        if (StringUtils.isNotBlank(source)) {
            String methodName = record.getSourceMethodName();
            source += StringUtils.isNotBlank(methodName)
                    ? DOT + methodName + LEFT_PARENTHESIS
                    + RIGHT_PARENTHESIS : EMPTY_STRING;
        }
        else {
            source = record.getLoggerName();
            source = StringUtils.isNotBlank(source) ? source : EMPTY_STRING;
        }
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

}
