package org.summer.domain;

public class User {
    private Long id;
    private String nickName;

    public User(Long id, String nickName) {
        this.id = id;
        this.nickName = nickName;
    }

    public Long getId() {
        return id;
    }

    public String getNickName() {
        return nickName;
    }

}
