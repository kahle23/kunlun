/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.test.pojo.entity.animal;

import java.io.Serializable;
import java.util.Date;

/**
 * The abstract animal (test class).
 * @author Kahle
 */
public abstract class AbstractAnimal implements Animal, Serializable {
    private Long    id;
    private String  name;
    private Integer gender;
    private Date    birthday;
    private Integer age;

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public void setName(String name) {

        this.name = name;
    }

    @Override
    public Integer getSex() {

        return gender;
    }

    @Override
    public void setSex(Integer gender) {

        this.gender = gender;
    }

    @Override
    public Date getBirthday() {

        return birthday;
    }

    @Override
    public void setBirthday(Date birthday) {

        this.birthday = birthday;
    }

    @Override
    public Integer getAge() {

        return age;
    }

    @Override
    public void setAge(Integer age) {

        this.age = age;
    }

}
