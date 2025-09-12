/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.io.util;

import kunlun.io.PathGenerator;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.time.DateUtil;
import kunlun.util.StrUtil;

import static kunlun.common.constant.Symbols.EMPTY_STRING;
import static kunlun.common.constant.Symbols.SLASH;
import static kunlun.util.Assert.notNull;

/**
 * 路径工具类.
 * @author Zerox
 */
public class PathUtil {
    private static final Logger log = LoggerFactory.getLogger(PathUtil.class);
    public static volatile PathGenerator pathGenerator;


    // region ======== 路径生成器管理 ========

    public static PathGenerator getPathGenerator() {
        if (pathGenerator != null) { return pathGenerator; }
        synchronized (PathUtil.class) {
            if (pathGenerator != null) { return pathGenerator; }
            PathUtil.setPathGenerator(new PathGeneratorImpl());
            return pathGenerator;
        }
    }

    public static void setPathGenerator(PathGenerator pathGenerator) {

        PathUtil.pathGenerator = notNull(pathGenerator);
    }
    // endregion


    // region ======== 路径生成相关方法 ========

    public static String gen(String strategy, String filename, Object... arguments) {

        return getPathGenerator().generate(strategy, filename, arguments);
    }
    // endregion




    // region ======== 路径相关内部类 ========
    /**
     * 简单的（默认的）路径生成器.
     * @author Zerox
     */
    public static class PathGeneratorImpl implements PathGenerator {
        private final String defaultFolderName;

        public PathGeneratorImpl(String defaultFolderName) {

            this.defaultFolderName = notNull(defaultFolderName);
        }

        public PathGeneratorImpl() {

            this(EMPTY_STRING);
        }

        @Override
        public String generate(String strategy, String filename, Object... arguments) {
            String folderName = strategy != null ? strategy : defaultFolderName;
            folderName = StrUtil.isNotBlank(folderName) ? folderName + SLASH : EMPTY_STRING;
            String time = DateUtil.format("yyyy/MM/dd/HH/mm");
            return String.format("%s%s/%s", folderName, time, filename);
        }
    }
    // endregion

}
