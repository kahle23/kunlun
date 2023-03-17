package artoria.ai.support;

/**
 * The simple ai message.
 * @author Kahle
 */
public class SimpleAiMessage {
    /**
     * The prompt(s) to generate completions for, encoded as a string.
     */
    private String  prompt;
    /**
     * The id or code of the model to use.
     */
    private String  model;
    /**
     * Whether to stream back partial progress.
     */
    private Boolean stream;

    public SimpleAiMessage(String prompt, String model, Boolean stream) {
        this.prompt = prompt;
        this.model = model;
        this.stream = stream;
    }

    public SimpleAiMessage(String prompt, String model) {
        this.prompt = prompt;
        this.model = model;
    }

    public SimpleAiMessage(String prompt) {

        this.prompt = prompt;
    }

    public SimpleAiMessage() {

    }

    public String getPrompt() {

        return prompt;
    }

    public void setPrompt(String prompt) {

        this.prompt = prompt;
    }

    public String getModel() {

        return model;
    }

    public void setModel(String model) {

        this.model = model;
    }

    public Boolean getStream() {

        return stream;
    }

    public void setStream(Boolean stream) {

        this.stream = stream;
    }

}
