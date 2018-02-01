package com.apyhs.artoria.logging;

import com.apyhs.artoria.exception.ExceptionUtils;
import com.apyhs.artoria.time.DateUtils;
import com.apyhs.artoria.util.StringUtils;
import com.apyhs.artoria.util.ThreadUtils;

import java.lang.management.ThreadInfo;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static com.apyhs.artoria.constant.Const.*;

/**
 * Simple jdk logger formatter.
 * @author Kahle
 */
public class SimpleFormatter extends Formatter {

    private static final int MAX_LEVEL_LENGTH = 7;

    @Override
    public String format(LogRecord record) {
        String time = printfTime(record);
        String level = printfLevel(record);
        String thread = printfThread(record);
        String source = printfSource(record);
        String message = printfMessage(record);
        String throwable = printfThrowable(record);

        String result = LEFT_SQUARE_BRACKET + time + RIGHT_SQUARE_BRACKET;
        if (level != null) { result += BLANK_SPACE + LEFT_SQUARE_BRACKET + level + RIGHT_SQUARE_BRACKET; }
        if (thread != null) { result += BLANK_SPACE + LEFT_SQUARE_BRACKET + thread + RIGHT_SQUARE_BRACKET; }
        if (source != null) { result += BLANK_SPACE + LEFT_SQUARE_BRACKET + source + RIGHT_SQUARE_BRACKET; }
        if (message != null) { result += BLANK_SPACE + LEFT_SQUARE_BRACKET + message + RIGHT_SQUARE_BRACKET; }
        if (throwable != null) { result += BLANK_SPACE + LEFT_SQUARE_BRACKET + throwable + RIGHT_SQUARE_BRACKET; }
        result += ENDL;
        return result;
    }

    private String printfTime(LogRecord record) {
        long millis = record.getMillis();
        // TODO: Optimize SimpleDateFormat by ThreadLocal
        SimpleDateFormat dateFormat =
                new SimpleDateFormat(DateUtils.DEFAULT_DATE_PATTERN);
        return dateFormat.format(new Date(millis));
    }

    private String printfLevel(LogRecord record) {
        Level level = record.getLevel();
        if (level == null) { return null; }
        String levelString = level.toString();
        if (levelString.length() < MAX_LEVEL_LENGTH) {
            int count = MAX_LEVEL_LENGTH - levelString.length();
            StringBuilder builder = new StringBuilder(levelString);
            for (int i = 0; i < count; i++) {
                builder.append(BLANK_SPACE);
            }
            return builder.toString();
        }
        else {
            return levelString;
        }
    }

    private String printfThread(LogRecord record) {
        int threadId = record.getThreadID();
        ThreadInfo threadInfo = ThreadUtils.getThreadInfo(threadId);
        if (threadInfo == null) { return null; }
        String threadName = threadInfo.getThreadName();
        return StringUtils.isNotBlank(threadName) ? threadName : null;
    }

    private String printfSource(LogRecord record) {
        String source = record.getSourceClassName();
        if (StringUtils.isNotBlank(source)) {
            String methodName = record.getSourceMethodName();
            if (StringUtils.isNotBlank(methodName)) {
                source += DOT + methodName;
                source += LEFT_PARENTHESIS + RIGHT_PARENTHESIS;
            }
        }
        else {
            source = record.getLoggerName();
            source = StringUtils.isNotBlank(source) ? source : null;
        }
        return source;
    }

    private String printfMessage(LogRecord record) {
        String message = record.getMessage();
        return StringUtils.isNotBlank(message) ? message : null;
    }

    private String printfThrowable(LogRecord record) {
        Throwable thrown = record.getThrown();
        if (thrown == null) { return null; }
        String result = ExceptionUtils.toString(thrown);
        return StringUtils.isNotBlank(result) ? ENDL + result : null;
    }

}
