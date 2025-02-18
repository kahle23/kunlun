package kunlun.security.support;

import kunlun.core.DataController;
import kunlun.security.support.util.DataScope;
import kunlun.security.support.util.FieldScope;

import java.util.Collection;
import java.util.Map;

public abstract class AbstractDataController implements DataController {
    public static final String DATA_PERMISSION = "data_permission";

    public static class BaseRule implements Rule {
        private DataScope  dataScope;
        private FieldScope fieldScope;

        public BaseRule(DataScope dataScope) {
            this();
            this.setDataScope(dataScope);
        }

        public BaseRule() {
            this.fieldScope = FieldScope.ALL;
            this.dataScope = DataScope.ALL;
        }

        public DataScope getDataScope() {

            return dataScope;
        }

        public void setDataScope(DataScope dataScope) {
            if (dataScope == null) { return; }
            this.dataScope = dataScope;
        }

        public FieldScope getFieldScope() {

            return fieldScope;
        }

        public void setFieldScope(FieldScope fieldScope) {
            if (fieldScope == null) { return; }
            this.fieldScope = fieldScope;
        }
    }

    public static class Context implements kunlun.core.Context {
        private String permission;
        private Object userId;
        private Object userType;
        private Collection<String> userGroups;
        private Rule rule;

        public String getPermission() {

            return permission;
        }

        public void setPermission(String permission) {

            this.permission = permission;
        }

        public Object getUserId() {

            return userId;
        }

        public void setUserId(Object userId) {

            this.userId = userId;
        }

        public Object getUserType() {

            return userType;
        }

        public void setUserType(Object userType) {

            this.userType = userType;
        }

        public Collection<String> getUserGroups() {

            return userGroups;
        }

        public void setUserGroups(Collection<String> userGroups) {

            this.userGroups = userGroups;
        }

        public Rule getRule() {

            return rule;
        }

        public void setRule(Rule rule) {

            this.rule = rule;
        }

        public Map<String, Object> getRuntimeData() {

            return null;
        }
    }

}
