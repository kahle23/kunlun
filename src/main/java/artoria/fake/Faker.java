package artoria.fake;

/**
 * Faker.
 * @author Kahle
 */
public interface Faker {

    String name();

    /**
     * eg. "person.name", "person.age[min:10,max:60]"
     * eg. "name=person.name|age=person.age[min:10,max:60]|birthday=time"
     */
    <T> T fake(String expression, Class<T> clazz);

}
