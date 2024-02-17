/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.generator.code;

import kunlun.generator.Generator;

public interface CodeGenerator extends Generator {

    Object generate(CodeConfig codeConfig);

}
