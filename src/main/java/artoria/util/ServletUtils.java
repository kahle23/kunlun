package artoria.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

import static artoria.util.StringConstant.*;

/**
 * @author Kahle
 */
public class ServletUtils {
    private static final String DEFAULT_CHARSET_NAME = Charset.defaultCharset().name();
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";

    public static void responseJson(HttpServletResponse response, String jsonString) throws IOException {
        responseJson(response, jsonString, DEFAULT_CHARSET_NAME);
    }

    public static void responseJson(HttpServletResponse response, String jsonString, String charset) throws IOException {
        Assert.notNull(response);
        Assert.notEmpty(jsonString);
        charset = StringUtils.isNotBlank(charset) ? charset : DEFAULT_CHARSET_NAME;
        response.setHeader(CONTENT_TYPE, APPLICATION_JSON + ";charset=" + charset);
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(jsonString);
            writer.flush();
        }
        finally {
            IOUtils.closeQuietly(writer);
        }
    }

    public static boolean isMultipartRequest(HttpServletRequest request) {
        String contentType = request.getContentType();
        return contentType != null && contentType.toLowerCase().contains("multipart");
    }

    public static StringBuilder simpleSummary(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();

        builder.append(STRING_AT).append(LEFT_SQUARE_BRACKET)
                .append(DateUtils.format()).append(RIGHT_SQUARE_BRACKET).append(BLANK_SPACE);

        builder.append(LEFT_SQUARE_BRACKET).append(request.getRemoteAddr())
                .append(COLON).append(request.getRemotePort())
                .append(RIGHT_SQUARE_BRACKET).append(BLANK_SPACE);

        builder.append(request.getMethod()).append(BLANK_SPACE);

        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap.entrySet().size() != 0) {
            builder.append(STRING_PARAMETER).append(LEFT_SQUARE_BRACKET)
                    .append(paramsSummary(parameterMap)).append(RIGHT_SQUARE_BRACKET)
                    .append(BLANK_SPACE);
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            builder.append(STRING_COOKIE).append(LEFT_SQUARE_BRACKET);
            for (Cookie cookie : cookies) {
                builder.append(cookie.getName()).append(EQUAL)
                        .append(cookie.getValue()).append(COMMA);
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.append(RIGHT_SQUARE_BRACKET).append(BLANK_SPACE);
        }

        String contentType = request.getContentType();
        if (StringUtils.isNotBlank(contentType)) {
            builder.append(STRING_CONTENT_TYPE).append(LEFT_SQUARE_BRACKET)
                    .append(contentType).append(RIGHT_SQUARE_BRACKET).append(BLANK_SPACE);
        }

        String characterEncoding = request.getCharacterEncoding();
        if (StringUtils.isNotBlank(characterEncoding)) {
            builder.append(STRING_ENCODE).append(LEFT_SQUARE_BRACKET)
                    .append(characterEncoding).append(RIGHT_SQUARE_BRACKET).append(BLANK_SPACE);
        }

        builder.append(STRING_TO).append(BLANK_SPACE).append(LEFT_SQUARE_BRACKET)
                .append(request.getRequestURL().toString()).append(RIGHT_SQUARE_BRACKET);

        return builder;
    }

    public static StringBuilder heavySummary(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder(ENDL)
                .append(ARROWHEAD_RIGHT).append(ARROWHEAD_RIGHT)
                .append(ARROWHEAD_RIGHT).append(ARROWHEAD_RIGHT).append(ENDL);

        builder.append(STRING_TIME).append(COLON).append(BLANK_SPACE)
                .append(DateUtils.format()).append(ENDL);

        builder.append(STRING_FROM).append(COLON).append(BLANK_SPACE)
                .append(request.getRemoteAddr()).append(COLON)
                .append(request.getRemotePort()).append(ENDL);

        builder.append(STRING_METHOD).append(COLON).append(BLANK_SPACE)
                .append(request.getMethod()).append(ENDL);

        builder.append(STRING_TARGET).append(COLON).append(BLANK_SPACE)
                .append(request.getRequestURL().toString()).append(ENDL);

        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap.entrySet().size() != 0) {
            builder.append(STRING_PARAMETER_LOWER_CASE).append(COLON).append(BLANK_SPACE);
            builder.append(paramsSummary(parameterMap)).append(ENDL);
        }

        String characterEncoding = request.getCharacterEncoding();
        if (StringUtils.isNotBlank(characterEncoding)) {
            builder.append(STRING_CHARACTER_ENCODING).append(COLON).append(BLANK_SPACE)
                    .append(characterEncoding).append(ENDL);
        }

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            builder.append(headerName).append(COLON).append(BLANK_SPACE)
                    .append(request.getHeader(headerName)).append(ENDL);
        }

        builder.append(ARROWHEAD_LEFT).append(ARROWHEAD_LEFT)
                .append(ARROWHEAD_LEFT).append(ARROWHEAD_LEFT).append(ENDL);

        return builder;
    }

    private static StringBuilder paramsSummary(Map<String, String[]> parameterMap) {
        StringBuilder builder = new StringBuilder();
        if (parameterMap == null) {
            return builder;
        }
        Set<Map.Entry<String, String[]>> entries = parameterMap.entrySet();
        for (Map.Entry<String, String[]> entry : entries) {
            String[] values = entry.getValue();
            if (values.length == 1) {
                builder.append(entry.getKey())
                        .append(EQUAL)
                        .append(values[0])
                        .append(COMMA);
            }
            else {
                builder.append(entry.getKey())
                        .append(LEFT_SQUARE_BRACKET)
                        .append(RIGHT_SQUARE_BRACKET)
                        .append(EQUAL)
                        .append(LEFT_CURLY_BRACKET);
                for (String value : values) {
                    builder.append(value)
                            .append(COMMA);
                }
                builder.deleteCharAt(builder.length() - 1)
                        .append(RIGHT_CURLY_BRACKET)
                        .append(COMMA);
            }
        }
        return builder.deleteCharAt(builder.length() - 1);
    }

    private static final String STRING_TO = "To";
    private static final String STRING_AT = "At";
    private static final String STRING_CONTENT_TYPE = "ContentType";
    private static final String STRING_ENCODE = "Encode";
    private static final String STRING_PARAMETER = "Parameter";
    private static final String STRING_COOKIE = "Cookie";

    private static final String ARROWHEAD_LEFT = "<<====";
    private static final String ARROWHEAD_RIGHT = "====>>";
    private static final String STRING_TIME = "$time";
    private static final String STRING_FROM = "$from";
    private static final String STRING_TARGET = "$target";
    private static final String STRING_METHOD = "$method";
    private static final String STRING_CHARACTER_ENCODING = "$character-encoding";
    private static final String STRING_PARAMETER_LOWER_CASE = "$parameter";

}
