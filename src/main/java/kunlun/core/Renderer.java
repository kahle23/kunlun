/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core;

import kunlun.core.function.Consumer;
import kunlun.util.Assert;

import java.io.Serializable;

/**
 * The renderer for handling bytes or strings.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Render">Render</a>
 * @see <a href="https://en.wikipedia.org/wiki/Template_processor">Template processor</a>
 * @see <a href="https://en.wikipedia.org/wiki/Rendering_(computer_graphics)">Rendering (computer graphics)</a>
 * @author Kahle
 */
public interface Renderer {

    /**
     * Get the template loader.
     * @return The template loader
     */
    Consumer<Tpl> getTemplateLoader();

    /**
     * Set the template loader.
     * @param loader The template loader
     */
    void setTemplateLoader(Consumer<Tpl> loader);

    /**
     * Render the data to the output through the template.
     * @param template The template (stream, reader, string, etc.) to be rendered
     * @param data The data to use in rendering input template
     * @param output The output stream or writer in which to render the output
     */
    void render(Object template, Object data, Object output);

    /**
     * The unified template object of the renderer.
     * @author Kahle
     */
    class Tpl implements Serializable {

        public static Tpl of(String name, String charset) {

            return of(name).setCharset(charset);
        }

        public static Tpl of(String name) {

            return of().setName(name);
        }

        public static Tpl of() {

            return new Tpl();
        }

        private Object content;
        private String charset;
        private String name;

        public String getName() {

            return name;
        }

        public Tpl setName(String name) {
            this.name = Assert.notBlank(name);
            return this;
        }

        public String getCharset() {

            return charset;
        }

        public Tpl setCharset(String charset) {
            this.charset = charset;
            return this;
        }

        public Object getContent() {

            return content;
        }

        public Tpl setContent(Object content) {
            Assert.isFalse(content instanceof Tpl);
            this.content = content;
            return this;
        }
    }

}
