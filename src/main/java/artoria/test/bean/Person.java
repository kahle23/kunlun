package artoria.test.bean;

import java.io.Serializable;

public class Person implements Serializable {
    private String name;
    private String gender;
    private Integer age;
    private String height;
    private String weigh;

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getGender() {

        return gender;
    }

    public void setGender(String gender) {

        this.gender = gender;
    }

    public Integer getAge() {

        return age;
    }

    public void setAge(Integer age) {

        this.age = age;
    }

    public String getHeight() {

        return height;
    }

    public void setHeight(String height) {

        this.height = height;
    }

    public String getWeigh() {

        return weigh;
    }

    public void setWeigh(String weigh) {

        this.weigh = weigh;
    }

}
