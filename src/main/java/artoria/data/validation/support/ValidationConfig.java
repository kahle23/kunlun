package artoria.data.validation.support;

import java.io.Serializable;

/**
 * The validation config.
 * @author Kahle
 */
public class ValidationConfig implements Serializable {
    /**
     * The value's expression or property name.
     */
    private String expression;
    /**
     * The validator name.
     */
    private String validator;
    /**
     * The message of validation failure.
     */
    private String message;

    public ValidationConfig(String expression, String validator, String message) {
        this.expression = expression;
        this.validator = validator;
        this.message = message;
    }

    public ValidationConfig() {

    }

    public String getExpression() {

        return expression;
    }

    public void setExpression(String expression) {

        this.expression = expression;
    }

    public String getValidator() {

        return validator;
    }

    public void setValidator(String validator) {

        this.validator = validator;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {

        this.message = message;
    }

}
