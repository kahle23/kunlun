/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.io.file.support;

import kunlun.exception.ExceptionUtils;
import kunlun.io.FileLoader;
import kunlun.io.util.IOUtils;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;
import kunlun.util.CloseUtils;
import kunlun.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static kunlun.common.constant.Numbers.FIVE;
import static kunlun.common.constant.Numbers.TWO;
import static kunlun.common.constant.Symbols.*;

/**
 * The jar file loader.
 *
 * @author Kahle
 */
public class JarFileLoader implements FileLoader {
    private static final Logger log = LoggerFactory.getLogger(JarFileLoader.class);
    private static final String JAR = "jar";
    private final ClassLoader classLoader;

    public JarFileLoader(ClassLoader classLoader) {
        Assert.notNull(classLoader, "Parameter \"classLoader\" must not null. ");
        this.classLoader = classLoader;
    }

    public JarFileLoader() {

        this(JarFileLoader.class.getClassLoader());
    }

    protected ClassLoader getClassLoader() {

        return classLoader;
    }

    @Override
    public InputStream load(Object param) {
        try {
            return doLoad(param);
        }
        catch (IOException e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    private InputStream doLoad(Object param) throws IOException {
        // Parameter validation and processing.
        Assert.notNull(param, "Parameter \"param\" must not null. ");
        String resource = String.valueOf(param);
        Assert.notBlank(resource, "Parameter \"param\" must not blank. ");
        resource = StringUtils.replace(resource, BACKSLASH, SLASH);
        // Get resource URL by resource name.
        // The example: “jar:file:/data/apps/demo-web.jar!/BOOT-INF/classes!/templates/controller.txt”
        URL resourceUrl = getClassLoader().getResource(resource);
        Assert.notNull(resourceUrl, "Variable \"resourceUrl\" must not null. ");
        log.debug("The found url is: \"{}\". ", resourceUrl);
        // Loading the resource.
        if (JAR.equals(resourceUrl.getProtocol())) {
            // Get resource path and index.
            String resourcePath = resourceUrl.getPath();
            int indexOf = resourcePath.indexOf(EXCLAMATION_MARK);
            // Get jar path and entry sub path.
            // The jar path example: “/data/apps/demo-web.jar“.
            // The entry sub path example: “BOOT-INF/classes!/templates/controller.txt“.
            String jarPath = resourcePath.substring(FIVE, indexOf);
            String entrySubPath = resourcePath.substring(indexOf + TWO);
            // In "JarEntry", the entry sub path does not have the exclamation mark.
            // The example: “BOOT-INF/classes/templates/controller.txt“.
            entrySubPath = entrySubPath.replaceAll(EXCLAMATION_MARK, EMPTY_STRING);
            log.debug("The jar path: \"{}\", the entry sub path: \"{}\". ", jarPath, entrySubPath);
            // Construct the "JarFile" object and load resources.
            JarFile jarFile = new JarFile(jarPath);
            try {
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (entry.getName().equals(entrySubPath) && !entry.isDirectory()) {
                        byte[] bytes = IOUtils.toByteArray(jarFile.getInputStream(entry));
                        return new ByteArrayInputStream(bytes);
                    }
                }
                return null;
            } finally { CloseUtils.closeQuietly(jarFile); }
        }
        else { return classLoader.getResourceAsStream(resource); }
    }

}
