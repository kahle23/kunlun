package kunlun.action.util.net;

import kunlun.core.Action;
import kunlun.util.StrUtil;

import java.io.File;
import java.net.URLConnection;

/**
 * The mime type action.
 * @author Kahle
 */
public class MediaTypeAction implements Action {
    public static final String DEFAULT_TYPE = "application/octet-stream";

    /*protected String getMimeType(Path file) {
        try {
            return Files.probeContentType(file);
        } catch (IOException e) {
            return null;
        }
    }*/

    protected String lookUpBuiltIn(String nameOrPath) {
        if (StrUtil.isBlank(nameOrPath)) { return null; }
        String tmp = nameOrPath.toLowerCase();
        if (tmp.endsWith(".css")) {
            return "text/css";
        } else if (tmp.endsWith(".js")) {
            return "application/x-javascript";
        } else if (tmp.endsWith(".rar")) {
            return "application/x-rar-compressed";
        } else if (tmp.endsWith(".7z")) {
            return "application/x-7z-compressed";
        } else if (tmp.endsWith(".wgt")) {
            return "application/widget";
        } else if (tmp.endsWith(".webp")) {
            return "image/webp";
        }
        return null;
    }

    protected String lookUpUrlConn(String nameOrPath) {
        if (StrUtil.isBlank(nameOrPath)) { return null; }
        return URLConnection.getFileNameMap().getContentTypeFor(nameOrPath);
    }

    protected String lookUpOthers(String nameOrPath) {

        return null;
    }

    protected String postProcess(String nameOrPath, String mimeType) {
        if (StrUtil.isBlank(mimeType)) {
            return DEFAULT_TYPE;
        }
        return mimeType;
    }

    @Override
    public Object execute(String strategy, Object input, Object[] arguments) {
        if (input == null) { return null; }
        String nameOrPath;
        if (input instanceof File) {
            nameOrPath = String.valueOf(input);
        } else if (input instanceof CharSequence) {
            nameOrPath = String.valueOf(input);
        } else {
            throw new IllegalArgumentException("Parameter \"input\" type is unsupported. ");
        }
        // Look up MIME type.
        String mimeType = lookUpBuiltIn(nameOrPath);
        if (StrUtil.isBlank(mimeType)) {
            mimeType = lookUpUrlConn(nameOrPath);
        }
        if (StrUtil.isBlank(mimeType)) {
            mimeType = lookUpOthers(nameOrPath);
        }
        return postProcess(nameOrPath, mimeType);
    }

}
