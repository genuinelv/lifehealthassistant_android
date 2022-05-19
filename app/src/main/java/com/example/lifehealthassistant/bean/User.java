package com.example.lifehealthassistant.bean;

public class User {
    private String id;
    private String password;
    private String name;
    private String sex;
    private String photo;

    public User(){}

    public User(String id, String password, String name, String sex, String photo) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.sex = sex;
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
