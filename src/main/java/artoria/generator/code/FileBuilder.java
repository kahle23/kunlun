package artoria.generator.code;

import artoria.core.Renderer;
import artoria.generator.Generator;

public interface FileBuilder extends Generator {

    String getName();

    Renderer getEngine();

    String build(FileContext context);

}
