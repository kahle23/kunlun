package artoria.test.bean;

import java.util.Date;

public class User extends Person {
    private Long   id;
    private String uid;
    private String nickname;
    private String avatar;
    private Date   birthday;
    private String phoneNumber;
    private String introduce;

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public String getUid() {

        return uid;
    }

    public void setUid(String uid) {

        this.uid = uid;
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

    public Date getBirthday() {

        return birthday;
    }

    public void setBirthday(Date birthday) {

        this.birthday = birthday;
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
