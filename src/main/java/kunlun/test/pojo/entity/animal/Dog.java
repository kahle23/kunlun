/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.test.pojo.entity.animal;

/**
 * The dog (test bean).
 * @author Kahle
 */
public class Dog extends AbstractAnimal {
    private String breed;
    private String size;
    private String sound;

    public String getBreed() {

        return breed;
    }

    public void setBreed(String breed) {

        this.breed = breed;
    }

    public String getSize() {

        return size;
    }

    public void setSize(String size) {

        this.size = size;
    }

    public String getSound() {

        return sound;
    }

    public void setSound(String sound) {

        this.sound = sound;
    }

}
