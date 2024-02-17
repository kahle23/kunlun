/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.test.pojo.entity.animal;

/**
 * The cat (test bean).
 * @author Kahle
 */
public class Cat extends AbstractAnimal {
    private String breed;

    public String getBreed() {

        return breed;
    }

    public void setBreed(String breed) {

        this.breed = breed;
    }

}
