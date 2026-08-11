/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action.invoke.proc;

import kunlun.action.invoke.InvokeAction;
import kunlun.common.constant.Charsets;
import kunlun.common.constant.Env;
import kunlun.core.function.Consumer;
import kunlun.exception.ExceptionUtil;
import kunlun.generator.id.IdUtil;
import kunlun.io.util.FileUtil;
import kunlun.io.util.IoUtil;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.renderer.RenderUtil;
import kunlun.util.CollUtil;
import kunlun.util.StrUtil;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static kunlun.common.constant.Algorithms.UUID;
import static kunlun.common.constant.Numbers.ZERO;
import static kunlun.common.constant.Symbols.DOT;
import static kunlun.common.constant.Symbols.EMPTY_STRING;

public class ProcCoreExecutor implements Consumer<InvokeAction.InvokeContext> {
    private static final Logger log = LoggerFactory.getLogger(ProcCoreExecutor.class);

    protected String extractFileSuffix(String fileSuffix) {
        if (StrUtil.isBlank(fileSuffix)) {
            return EMPTY_STRING;
        }
        if (!fileSuffix.startsWith(DOT)) {
            return DOT + fileSuffix;
        }
        return fileSuffix;
    }

    @Override
    public void accept(InvokeAction.InvokeContext context) {
        ProcInvokeConfig config = (ProcInvokeConfig) context.getConfig();
        String commandOrScript = config.getOtherCommandOrScript();
        String mainCommand  = config.getMainCommand();
        Integer normalExitValue = config.getNormalExitValue();
        Boolean destroyProcess = config.getDestroyProcess();
        Boolean needFile = config.getScriptNeedFile();
        String processCharset = config.getProcessCharset();
        String scriptCharset = config.getScriptCharset();
        String workDir = config.getWorkDir();
        String scriptEngine = config.getScriptEngine();
        String rendererName = config.getRendererName();
        if (StrUtil.isBlank(workDir)) { workDir = Env.TMP_DIR; }
        if (normalExitValue == null) { normalExitValue = ZERO; }
        if (destroyProcess == null) { destroyProcess = true; }
        if (StrUtil.isBlank(processCharset)) { processCharset = Charsets.STR_UTF_8; }
        if (StrUtil.isBlank(scriptCharset)) { scriptCharset = Charsets.STR_UTF_8; }
        // Render main command.
        if (StrUtil.isNotBlank(mainCommand)) {
            mainCommand = RenderUtil.renderToString(rendererName, mainCommand, context.getConvertedInput());
        }
        // Render other command or script.
        File scriptFile = null;
        if (StrUtil.isNotBlank(commandOrScript)) {
            commandOrScript = RenderUtil.renderToString(rendererName, commandOrScript, context.getConvertedInput());
            // Create and write file if necessary.
            if (needFile != null && needFile) {
                String filename = IdUtil.nextString(UUID) + extractFileSuffix(config.getScriptFileSuffix());
                scriptFile = new File(workDir, filename);
                FileUtil.writeString(commandOrScript, scriptFile, Charset.forName(scriptCharset));
                commandOrScript = String.valueOf(scriptFile);
            }
        }
        // Build parameters.
        List<String> commands = new ArrayList<String>();
        if (StrUtil.isNotBlank(mainCommand)) { commands.add(mainCommand); }
        if (StrUtil.isNotBlank(commandOrScript)) { commands.add(commandOrScript); }
        // If empty, finish.
        if (CollUtil.isEmpty(commands)) {
            context.setRawOutput(null);
            return;
        }
        // Build process and execute.
        InputStream resultStream = null;
        Process process = null;
        String result;
        int exitValue;
        try {
            process = new ProcessBuilder(commands)
                    .redirectErrorStream(true)
                    .start();
            result = IoUtil.read(resultStream = process.getInputStream(), Charset.forName(processCharset));
            exitValue = process.waitFor();
            context.setRawOutput(result);
        } catch (Exception e) {
            throw ExceptionUtil.wrap(e);
        } finally {
            IoUtil.closeQuietly(resultStream);
            if (needFile != null && needFile) {
                FileUtil.deleteFile(scriptFile);
            }
            if (destroyProcess && null != process) {
                process.destroy();
            }
        }
        // Process error message.
        if (exitValue != normalExitValue) {
            throw new IllegalStateException("The exit value not be "
                    + normalExitValue + "! The output result is \"" + result + "\". ");
        }
        // Finish.
        context.setRawOutput(result);
    }

}
