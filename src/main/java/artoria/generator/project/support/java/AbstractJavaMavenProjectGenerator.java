package artoria.generator.project.support.java;

import artoria.data.bean.BeanUtils;
import artoria.data.tuple.PairImpl;
import artoria.exception.ExceptionUtils;
import artoria.generator.project.ProjectConfig;
import artoria.generator.project.ProjectGenerator;
import artoria.renderer.TextRenderer;
import artoria.time.DateUtils;
import artoria.util.Assert;
import artoria.util.CloseUtils;

import java.io.*;
import java.util.Map;

import static artoria.common.constant.Symbols.EMPTY_STRING;
import static artoria.common.constant.Symbols.NEWLINE;

/**
 * The abstract java maven project generator.
 * @author Kahle
 */
public abstract class AbstractJavaMavenProjectGenerator implements ProjectGenerator {
    protected static final String MAIN_CODE = "main";

    /**
     * Record log information.
     * @param context The context object of the project
     * @param format The log format
     * @param args The log arguments
     */
    protected void log(ProjectContextImpl context, String format, Object... args) {
        context.getMessageBuilder()
                .append(DateUtils.format())
                .append(" ")
                .append(String.format(format, args))
                .append(NEWLINE);
    }

    /**
     * Create directory.
     * @param parent The parent directory
     * @param child The child directory
     * @return The created directory
     */
    protected File mkdirs(String parent, String child) {

        return mkdirs(new File(parent), child);
    }

    /**
     * Create directory.
     * @param parent The parent directory
     * @param child The child directory
     * @return The created directory
     */
    protected File mkdirs(File parent, String child) {
        Assert.notNull(parent, "Parameter \"parent\" must not null. ");
        Assert.notBlank(child, "Parameter \"child\" must not blank. ");
        File newFile = new File(parent, child);
        mkdirs(newFile);
        return newFile;
    }

    /**
     * Create directory.
     * @param file The directory to be created
     */
    protected void mkdirs(File file) {
        Assert.notNull(file, "Parameter \"parent\" must not null. ");
        if (file.exists() && file.isFile()) {
            throw new IllegalStateException("Path \"" + file + "\" already exists and is a file! ");
        }
        if (!file.exists() && !file.mkdirs()) {
            throw new IllegalStateException("Failed to create directory \"" + file + "\"! ");
        }
    }

    /**
     * Rendering template.
     * @param projectContext The context object of the project
     * @param templateChildPath The child path of template (excluding template suffix)
     * @param outputChildPath The child path of the output file
     */
    protected void render(ProjectContextImpl projectContext, ModuleInfo moduleInfo, String templateChildPath, String outputChildPath) {
        Assert.notBlank(templateChildPath, "Parameter \"templateChildPath\" must not blank. ");
        Assert.notBlank(outputChildPath, "Parameter \"outputChildPath\" must not blank. ");
        Assert.notNull(projectContext, "Parameter \"projectContext\" must not null. ");
        Assert.notNull(moduleInfo, "Parameter \"moduleInfo\" must not null. ");
        // template path
        String tRootPath = moduleInfo.getTemplateRootPath();
        String tSuffix = moduleInfo.getTemplateSuffix();
        String templatePath = String.valueOf(new File(tRootPath, templateChildPath + tSuffix));
        // output path
        String mRootPath = moduleInfo.getModuleRootPath();
        File outputPath = new File(mRootPath, outputChildPath);
        // log
        log(projectContext, "render template \"%s\" to \"%s\". ", templatePath, outputPath);
        // output parent path
        mkdirs(outputPath.getParentFile());
        // render
        BufferedWriter bufferedWriter = null;
        try {
            TextRenderer textRenderer = getTextRenderer(projectContext);
            Map<String, Object> data = BeanUtils.beanToMap(projectContext);
            OutputStream out = new FileOutputStream(outputPath);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(out));
            textRenderer.render(new PairImpl<String, String>(templatePath, null), templatePath, data, bufferedWriter);
            bufferedWriter.flush();
        }
        catch (IOException e) {
            throw ExceptionUtils.wrap(e);
        }
        finally {
            CloseUtils.closeQuietly(bufferedWriter);
        }
    }

    /**
     * Rendering template.
     * @param projectContext The context object of the project
     * @param targetChildPath The child path of target
     *                        (Mean "templateChildPath" is equal to "outputChildPath")
     */
    protected void render(ProjectContextImpl projectContext, ModuleInfo moduleInfo, String targetChildPath) {

        render(projectContext, moduleInfo, targetChildPath, targetChildPath);
    }

    protected void buildRootPath(ProjectContextImpl projectContext, ModuleInfo moduleInfo) {
        Assert.notNull(projectContext, "Parameter \"projectContext\" must not null. ");
        render(projectContext, moduleInfo, "\\pom.xml");
        if (MAIN_CODE.equals(moduleInfo.getCode())) {
            render(projectContext, moduleInfo, "\\README.md");
            render(projectContext, moduleInfo, "\\.gitignore");
        }
    }

    protected void buildMainJavaPath(ProjectContextImpl projectContext, ModuleInfo moduleInfo) {
        Assert.notNull(projectContext, "Parameter \"projectContext\" must not null. ");
        String mainJavaChildPath = moduleInfo.getMainJavaChildPath();
        String moduleRootPath = moduleInfo.getModuleRootPath();
        // main java path
        log(projectContext, "mkdirs parent \"%s\" child \"%s\". ", moduleRootPath, mainJavaChildPath);
        moduleInfo.setMainJavaPath(mkdirs(moduleRootPath, mainJavaChildPath));
        // main java base package child path
        String packageChildPath = moduleInfo.getBasePackageName()
                .trim().replaceAll("\\.", "\\\\");
        packageChildPath = mainJavaChildPath + "\\" + packageChildPath;
        moduleInfo.setMainJavaBasePackageChildPath(packageChildPath);
    }

    public void buildMainResourcesPath(ProjectContextImpl projectContext, ModuleInfo moduleInfo) {
        Assert.notNull(projectContext, "Parameter \"projectContext\" must not null. ");
        String mainResChildPath = moduleInfo.getMainResourcesChildPath();
        String moduleRootPath = moduleInfo.getModuleRootPath();
        // main resources path
        log(projectContext, "mkdirs parent \"%s\" child \"%s\". ", moduleRootPath, mainResChildPath);
        moduleInfo.setMainResourcesPath(mkdirs(moduleRootPath, mainResChildPath));
    }

    public void buildTestJavaPath(ProjectContextImpl projectContext, ModuleInfo moduleInfo) {
        Assert.notNull(projectContext, "Parameter \"projectContext\" must not null. ");
        String testJavaChildPath = moduleInfo.getTestJavaChildPath();
        String moduleRootPath = moduleInfo.getModuleRootPath();
        // test java path
        log(projectContext, "mkdirs parent \"%s\" child \"%s\". ", moduleRootPath, testJavaChildPath);
        moduleInfo.setTestJavaPath(mkdirs(moduleRootPath, testJavaChildPath));
        // test java base package child path
        String packageChildPath = moduleInfo.getBasePackageName()
                .trim().replaceAll("\\.", "\\\\");
        packageChildPath = testJavaChildPath + "\\" + packageChildPath;
        moduleInfo.setTestJavaBasePackageChildPath(packageChildPath);
    }

    public void buildTestResourcesPath(ProjectContextImpl projectContext, ModuleInfo moduleInfo) {
        Assert.notNull(projectContext, "Parameter \"projectContext\" must not null. ");
        String testResChildPath = moduleInfo.getTestResourcesChildPath();
        String moduleRootPath = moduleInfo.getModuleRootPath();
        // test resources path
        log(projectContext, "mkdirs parent \"%s\" child \"%s\". ", moduleRootPath, testResChildPath);
        moduleInfo.setTestResourcesPath(mkdirs(moduleRootPath, testResChildPath));
    }

    @Override
    public String generate(ProjectConfig projectConfig) {
        Assert.notNull(projectConfig, "Parameter \"projectConfig\" must not null. ");
        ProjectContextImpl context = createContext(projectConfig);
        ModuleInfo mainModule = context.getMainModule();
        log(context, ">> begin generating project \"%s\". ", context.getName());
        buildRootPath(context, mainModule);
        buildMainJavaPath(context, mainModule);
        buildMainResourcesPath(context, mainModule);
        buildTestJavaPath(context, mainModule);
        buildTestResourcesPath(context, mainModule);
        log(context, ">> end generate project \"%s\". ", context.getName());
        return String.valueOf(context.getMessageBuilder());
    }

    protected abstract TextRenderer getTextRenderer(ProjectContextImpl projectContext);

    protected abstract ProjectContextImpl createContext(ProjectConfig projectConfig);

    protected static class ProjectContextImpl implements ProjectContext {
        private String name;
        private String description;
        private String basePackageName;
        private String projectRootPath;
        private String templateRootPath;
        private String templateSuffix = ".txt";
        private StringBuilder messageBuilder;
        private ModuleInfo mainModule = new ModuleInfo(MAIN_CODE);

        @Override
        public String getName() {

            return name;
        }

        @Override
        public void setName(String name) {

            this.name = name;
        }

        @Override
        public String getDescription() {

            return description;
        }

        @Override
        public void setDescription(String description) {

            this.description = description;
        }

        public String getBasePackageName() {

            return basePackageName;
        }

        public void setBasePackageName(String basePackageName) {

            this.basePackageName = basePackageName;
        }

        public String getProjectRootPath() {

            return projectRootPath;
        }

        public void setProjectRootPath(String projectRootPath) {

            this.projectRootPath = projectRootPath;
        }

        public String getTemplateRootPath() {

            return templateRootPath;
        }

        public void setTemplateRootPath(String templateRootPath) {

            this.templateRootPath = templateRootPath;
        }

        public String getTemplateSuffix() {

            return templateSuffix;
        }

        public void setTemplateSuffix(String templateSuffix) {

            this.templateSuffix = templateSuffix;
        }

        public ModuleInfo getMainModule() {

            return mainModule;
        }

        public void setMainModule(ModuleInfo mainModule) {

            this.mainModule = mainModule;
        }

        public StringBuilder getMessageBuilder() {

            return messageBuilder;
        }

        public void setMessageBuilder(StringBuilder messageBuilder) {

            this.messageBuilder = messageBuilder;
        }
    }

    public static class ModuleInfo {
        private final String name;
        private final String code;
        private String basePackageName;

        private String moduleRootPath;
        private String templateRootPath;
        private String templateSuffix = ".txt";

        private File mainJavaPath;
        private File mainResourcesPath;
        private File testJavaPath;
        private File testResourcesPath;

        private String mainJavaChildPath;
        private String mainResourcesChildPath;
        private String testJavaChildPath;
        private String testResourcesChildPath;
        private String mainJavaBasePackageChildPath;
        private String testJavaBasePackageChildPath;

        public ModuleInfo(String moduleCode, String moduleName) {
            Assert.notBlank(moduleCode, "Parameter \"moduleCode\" must not blank. ");
            Assert.notNull(moduleName, "Parameter \"moduleName\" must not null. ");
            this.name = moduleName;
            this.code = moduleCode;
            this.mainJavaChildPath = "src\\main\\java";
            this.mainResourcesChildPath = "src\\main\\resources";
            this.testJavaChildPath = "src\\test\\java";
            this.testResourcesChildPath = "src\\test\\resources";
        }

        public ModuleInfo(String moduleCode) {

            this(moduleCode, EMPTY_STRING);
        }

        public String getName() {

            return name;
        }

        public String getCode() {

            return code;
        }

        public String getBasePackageName() {

            return basePackageName;
        }

        public void setBasePackageName(String basePackageName) {

            this.basePackageName = basePackageName;
        }

        public String getModuleRootPath() {

            return moduleRootPath;
        }

        public void setModuleRootPath(String moduleRootPath) {

            this.moduleRootPath = moduleRootPath;
        }

        public String getTemplateRootPath() {

            return templateRootPath;
        }

        public void setTemplateRootPath(String templateRootPath) {

            this.templateRootPath = templateRootPath;
        }

        public String getTemplateSuffix() {

            return templateSuffix;
        }

        public void setTemplateSuffix(String templateSuffix) {

            this.templateSuffix = templateSuffix;
        }

        public File getMainJavaPath() {

            return mainJavaPath;
        }

        public void setMainJavaPath(File mainJavaPath) {

            this.mainJavaPath = mainJavaPath;
        }

        public File getMainResourcesPath() {

            return mainResourcesPath;
        }

        public void setMainResourcesPath(File mainResourcesPath) {

            this.mainResourcesPath = mainResourcesPath;
        }

        public File getTestJavaPath() {

            return testJavaPath;
        }

        public void setTestJavaPath(File testJavaPath) {

            this.testJavaPath = testJavaPath;
        }

        public File getTestResourcesPath() {

            return testResourcesPath;
        }

        public void setTestResourcesPath(File testResourcesPath) {

            this.testResourcesPath = testResourcesPath;
        }

        public String getMainJavaChildPath() {

            return mainJavaChildPath;
        }

        public void setMainJavaChildPath(String mainJavaChildPath) {

            this.mainJavaChildPath = mainJavaChildPath;
        }

        public String getMainResourcesChildPath() {

            return mainResourcesChildPath;
        }

        public void setMainResourcesChildPath(String mainResourcesChildPath) {

            this.mainResourcesChildPath = mainResourcesChildPath;
        }

        public String getTestJavaChildPath() {

            return testJavaChildPath;
        }

        public void setTestJavaChildPath(String testJavaChildPath) {

            this.testJavaChildPath = testJavaChildPath;
        }

        public String getTestResourcesChildPath() {

            return testResourcesChildPath;
        }

        public void setTestResourcesChildPath(String testResourcesChildPath) {

            this.testResourcesChildPath = testResourcesChildPath;
        }

        public String getMainJavaBasePackageChildPath() {

            return mainJavaBasePackageChildPath;
        }

        public void setMainJavaBasePackageChildPath(String mainJavaBasePackageChildPath) {

            this.mainJavaBasePackageChildPath = mainJavaBasePackageChildPath;
        }

        public String getTestJavaBasePackageChildPath() {

            return testJavaBasePackageChildPath;
        }

        public void setTestJavaBasePackageChildPath(String testJavaBasePackageChildPath) {

            this.testJavaBasePackageChildPath = testJavaBasePackageChildPath;
        }
    }

}
