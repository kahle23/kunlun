/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.test.pojo.entity.animal;

import java.util.Date;

/**
 * The animal (test interface).
 * @author Kahle
 */
public interface Animal {

    /**
     * Get the animal name.
     * @return The animal name
     */
    String getName();

    /**
     * Set the animal name.
     * @param name The animal name
     */
    void setName(String name);

    /**
     * Get the animal sex.
     * @return The animal sex
     */
    Integer getSex();

    /**
     * Set the animal sex.
     * @param sex The animal sex
     */
    void setSex(Integer sex);

    /**
     * Get the animal birthday.
     * @return The animal birthday
     */
    Date getBirthday();

    /**
     * Set the animal birthday.
     * @param birthday The animal birthday
     */
    void setBirthday(Date birthday);

    /**
     * Get the animal age.
     * @return The animal age
     */
    Integer getAge();

    /**
     * Set the animal age.
     * @param age The animal age
     */
    void setAge(Integer age);

}
