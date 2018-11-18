package artoria.entity;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String uid;
    private String username;
    private String password;
    private String nickname;
    private String realName;
    private String avatar;
    private String intro;

    public String getUid() {

        return this.uid;
    }

    public void setUid(String uid) {

        this.uid = uid;
    }

    public String getUsername() {

        return this.username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getPassword() {

        return this.password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public String getNickname() {

        return this.nickname;
    }

    public void setNickname(String nickname) {

        this.nickname = nickname;
    }

    public String getRealName() {

        return this.realName;
    }

    public void setRealName(String realName) {

        this.realName = realName;
    }

    public String getAvatar() {

        return this.avatar;
    }

    public void setAvatar(String avatar) {

        this.avatar = avatar;
    }

    public String getIntro() {

        return this.intro;
    }

    public void setIntro(String intro) {

        this.intro = intro;
    }

}
