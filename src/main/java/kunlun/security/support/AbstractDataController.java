package kunlun.security.support;

import kunlun.core.DataController;
import kunlun.security.support.util.DataScope;
import kunlun.security.support.util.FieldScope;

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

}
