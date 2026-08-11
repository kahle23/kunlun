/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action.invoke.proc;

import kunlun.action.invoke.InvokeAction;

/**
 * The process invoke configuration.
 * @author Kahle
 */
public class ProcInvokeConfig extends InvokeAction.AbstractConfig {

    /**
     * The work directory (default java.io.tmpdir).
     */
    private String workDir;
    /**
     * windows console charset maybe is gb2312
     */
    private String processCharset;
    /**
     * The main command.
     */
    private String mainCommand;
    /**
     * The command arguments.
     */
    private String otherCommandOrScript;
    /**
     * Like
     * python3 [python script]
     * python3 only exec a python script file.
     */
    private Boolean scriptNeedFile;
    private String  scriptCharset;
    private String  scriptFileSuffix;
    /**
     * The normal exit value (default 0).
     */
    private Integer normalExitValue;
    /**
     * Whether the process needs to be destroyed (default true)?
     */
    private Boolean destroyProcess;

    public String getWorkDir() {

        return workDir;
    }

    public void setWorkDir(String workDir) {

        this.workDir = workDir;
    }

    public String getProcessCharset() {

        return processCharset;
    }

    public void setProcessCharset(String processCharset) {

        this.processCharset = processCharset;
    }

    public String getMainCommand() {

        return mainCommand;
    }

    public void setMainCommand(String mainCommand) {

        this.mainCommand = mainCommand;
    }

    public String getOtherCommandOrScript() {

        return otherCommandOrScript;
    }

    public void setOtherCommandOrScript(String otherCommandOrScript) {

        this.otherCommandOrScript = otherCommandOrScript;
    }

    public Boolean getScriptNeedFile() {

        return scriptNeedFile;
    }

    public void setScriptNeedFile(Boolean scriptNeedFile) {

        this.scriptNeedFile = scriptNeedFile;
    }

    public String getScriptCharset() {

        return scriptCharset;
    }

    public void setScriptCharset(String scriptCharset) {

        this.scriptCharset = scriptCharset;
    }

    public String getScriptFileSuffix() {

        return scriptFileSuffix;
    }

    public void setScriptFileSuffix(String scriptFileSuffix) {

        this.scriptFileSuffix = scriptFileSuffix;
    }

    public Integer getNormalExitValue() {

        return normalExitValue;
    }

    public void setNormalExitValue(Integer normalExitValue) {

        this.normalExitValue = normalExitValue;
    }

    public Boolean getDestroyProcess() {

        return destroyProcess;
    }

    public void setDestroyProcess(Boolean destroyProcess) {

        this.destroyProcess = destroyProcess;
    }
}
