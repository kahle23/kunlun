/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.io;

import kunlun.generator.Generator;

/**
 * 路径生成器的抽象接口.
 * @author Kahle
 */
public interface PathGenerator extends Generator {

    /**
     * 基于策略和文件名生成路径.
     * @param strategy 路径生成策略，比如文件夹类型 或 某某特定场景的路径生成逻辑
     * @param filename 文件名称，一般情况下是指原始文件名称
     * @return 生成的文件路径，一般用于文件上传
     */
    String generate(String strategy, String filename, Object... arguments);

}
