package artoria.entity;

import java.io.Serializable;

public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private Integer age;
    private Integer sex;
    private Double height;
    private Double weight;

    public String getName() {

        return this.name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public Integer getAge() {

        return this.age;
    }

    public void setAge(Integer age) {

        this.age = age;
    }

    public Integer getSex() {

        return this.sex;
    }

    public void setSex(Integer sex) {

        this.sex = sex;
    }

    public Double getHeight() {

        return this.height;
    }

    public void setHeight(Double height) {

        this.height = height;
    }

    public Double getWeight() {

        return this.weight;
    }

    public void setWeight(Double weight) {

        this.weight = weight;
    }

}
