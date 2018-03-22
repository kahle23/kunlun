package com.github.kahlkn.artoria.logging;

import com.github.kahlkn.artoria.exception.ExceptionUtils;
import com.github.kahlkn.artoria.time.DateUtils;
import com.github.kahlkn.artoria.util.StringUtils;
import com.github.kahlkn.artoria.util.ThreadLocalUtils;
import com.github.kahlkn.artoria.util.ThreadUtils;

import java.lang.management.ThreadInfo;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static com.github.kahlkn.artoria.util.Const.*;

/**
 * Simple jdk logger formatter.
 * @author Kahle
 */
public class SimpleFormatter extends Formatter {
    private static final String DEFAULT_DATE_FORMAT_KEY = SimpleDateFormat.class.getName() + UNDERLINE + DateUtils.DEFAULT_DATE_PATTERN;
    private static final int MAX_LEVEL_LENGTH = 7;

    @Override
    public String format(LogRecord record) {
        String time = this.printfTime(record);
        String level = this.printfLevel(record);
        String thread = this.printfThread(record);
        String source = this.printfSource(record);
        String message = this.printfMessage(record);
        String throwable = this.printfThrowable(record);
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
        SimpleDateFormat dateFormat =
                (SimpleDateFormat) ThreadLocalUtils.getValue(DEFAULT_DATE_FORMAT_KEY);
        if (dateFormat == null) {
            dateFormat =
                    new SimpleDateFormat(DateUtils.DEFAULT_DATE_PATTERN);
            ThreadLocalUtils.setValue(DEFAULT_DATE_FORMAT_KEY, dateFormat);
        }
        return dateFormat.format(new Date(millis));
    }

    private String printfLevel(LogRecord record) {
        Level level = record.getLevel();
        if (level == null) { return null; }
        return level.toString();
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
