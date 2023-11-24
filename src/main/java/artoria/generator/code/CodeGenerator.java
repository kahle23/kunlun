package artoria.generator.code;

import artoria.generator.Generator;

public interface CodeGenerator extends Generator {

    Object generate(CodeConfig codeConfig);

}
