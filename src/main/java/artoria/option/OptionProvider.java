package artoria.option;

import java.util.Map;

public interface OptionProvider {

    <T> T getRequiredOption(String owner, String name, Class<T> type);

    <T> T getOption(String owner, String name, Class<T> type, T defaultValue);

    boolean containsOption(String owner, String name);

    Map<String, Object> getOptions(String owner);

    Object getOption(String owner, String name, Object defaultValue);

    Object setOption(String owner, String name, Object value);

    Object removeOption(String owner, String name);

}
