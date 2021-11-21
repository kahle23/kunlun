package artoria.message;

import artoria.lang.Code;

/**
 * The type of message.
 * @author Kahle
 */
public enum MessageType implements Code<Integer> {
    /**
     * The unknown type.
     */
    UNKNOWN(0, "Unknown type"),
    /**
     * The console message.
     */
    CONSOLE(1, "Console message"),
    /**
     * The log message.
     */
    LOG(2, "Log message"),
    /**
     * The database message.
     */
    DATABASE(3, "Database message"),
    /**
     * The message queue.
     */
    MQ(4, "Message queue"),
    /**
     * The event message.
     */
    EVENT(11, "Event message"),
    /**
     * The email message.
     */
    EMAIL(12, "Email message"),
    /**
     * The failure message (like dead letter queue).
     */
    FAILURE(20, "Failure message"),
    ;

    private String  description;
    private Integer code;

    MessageType(Integer code, String description) {
        this.description = description;
        this.code = code;
    }

    @Override
    public Integer getCode() {

        return code;
    }

    @Override
    public String getDescription() {

        return description;
    }

    public static MessageType parse(Object input) {
        if (input == null) { return null; }
        if (input instanceof MessageType) { return (MessageType) input; }
        int inputInt;
        if (!(input instanceof Integer)) {
            inputInt = Integer.parseInt(String.valueOf(input));
        }
        else { inputInt = (Integer) input; }
        MessageType[] values = values();
        for (MessageType value : values) {
            if (value.getCode().equals(inputInt)) { return value; }
        }
        return UNKNOWN;
    }

}
