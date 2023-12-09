package artoria.test.pojo.entity.system;

import java.io.Serializable;
import java.util.Date;

/**
 * The user (test bean).
 * @author Kahle
 */
public class User implements Serializable {
    private Long   id;
    private String nickname;
    private String avatar;
    private String name;
    private String gender;
    private Integer age;
    private Date   birthday;
    private String height;
    private String weigh;
    private String phoneNumber;
    private String introduce;

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public String getNickname() {

        return nickname;
    }

    public void setNickname(String nickname) {

        this.nickname = nickname;
    }

    public String getAvatar() {

        return avatar;
    }

    public void setAvatar(String avatar) {

        this.avatar = avatar;
    }

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

    public Date getBirthday() {

        return birthday;
    }

    public void setBirthday(Date birthday) {

        this.birthday = birthday;
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

    public String getPhoneNumber() {

        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {

        this.phoneNumber = phoneNumber;
    }

    public String getIntroduce() {

        return introduce;
    }

    public void setIntroduce(String introduce) {

        this.introduce = introduce;
    }

}
