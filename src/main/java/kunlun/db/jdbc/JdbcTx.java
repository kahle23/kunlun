/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.db.jdbc;

import java.io.Serializable;

public class JdbcTx implements Serializable {
    private JdbcAtom atom;
    private Integer  level;
    private String   configCode;

    public JdbcTx(JdbcAtom atom, String configCode) {
        this.configCode = configCode;
        this.atom = atom;
    }

    public JdbcTx(JdbcAtom atom) {

        this.atom = atom;
    }

    public JdbcTx() {

    }

    public JdbcAtom getAtom() {

        return atom;
    }

    public void setAtom(JdbcAtom atom) {

        this.atom = atom;
    }

    public Integer getLevel() {

        return level;
    }

    public void setLevel(Integer level) {

        this.level = level;
    }

    public String getConfigCode() {

        return configCode;
    }

    public void setConfigCode(String configCode) {

        this.configCode = configCode;
    }

}
