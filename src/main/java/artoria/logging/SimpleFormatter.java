package artoria.logging;

import artoria.util.Time;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * @author Kahle
 */
public class SimpleFormatter extends Formatter {

    private static final int MAX_LEVEL_LENGTH = 7;
    private static final String SINGLE_BLANK = " ";
    private static final String LEFT_SQUARE_BRACKET = "[";
    private static final String RIGHT_SQUARE_BRACKET = "]";

    @Override
    public String format(LogRecord record) {
        String time = printfTime(record);
        String level = printfLevel(record);
        String thread = printfThread(record);
        String source = printfSource(record);
        String message = printfMessage(record);
        String throwable = printfThrowable(record);

        String result = LEFT_SQUARE_BRACKET + time + RIGHT_SQUARE_BRACKET;
        if (level != null) { result += SINGLE_BLANK + LEFT_SQUARE_BRACKET + level + RIGHT_SQUARE_BRACKET; }
        if (thread != null) { result += SINGLE_BLANK + LEFT_SQUARE_BRACKET + thread + RIGHT_SQUARE_BRACKET; }
        if (source != null) { result += SINGLE_BLANK + LEFT_SQUARE_BRACKET + source + RIGHT_SQUARE_BRACKET; }
        if (message != null) { result += SINGLE_BLANK + LEFT_SQUARE_BRACKET + message + RIGHT_SQUARE_BRACKET; }
        if (throwable != null) { result += SINGLE_BLANK + LEFT_SQUARE_BRACKET + throwable + RIGHT_SQUARE_BRACKET; }
        result += "\n";
        return result;
    }

    private String printfTime(LogRecord record) {
        long millis = record.getMillis();
        Time time = Time.on(millis);
        return time.format();
    }

    private String printfLevel(LogRecord record) {
        Level level = record.getLevel();
        if (level == null) { return null; }
        String levelString = level.toString();
        if (levelString.length() < MAX_LEVEL_LENGTH) {
            int count = MAX_LEVEL_LENGTH - levelString.length();
            StringBuilder builder = new StringBuilder(levelString);
            for (int i = 0; i < count; i++) {
                builder.append(SINGLE_BLANK);
            }
            return builder.toString();
        }
        else {
            return levelString;
        }
    }

    private String printfThread(LogRecord record) {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        if (threadMXBean == null) { return null; }
        int threadID = record.getThreadID();
        ThreadInfo threadInfo = threadMXBean.getThreadInfo(threadID);
        if (threadInfo == null) { return null; }
        String threadName = threadInfo.getThreadName();
        return StringUtils.isNotBlank(threadName) ? threadName : null;
    }

    private String printfSource(LogRecord record) {
        String source = record.getSourceClassName();
        if (StringUtils.isNotBlank(source)) {
            String methodName = record.getSourceMethodName();
            if (StringUtils.isNotBlank(methodName)) {
                source += "." + methodName + "()";
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
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println();
        thrown.printStackTrace(pw);
        pw.close();
        String result = sw.toString();
        return StringUtils.isNotBlank(result) ? result : null;
    }

}
