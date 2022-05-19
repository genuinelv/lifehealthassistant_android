package com.example.lifehealthassistant.bean;


import java.util.Date;


public class Useremail {
    private String id;
    private String email;
    private int emailstate;
    private String existdate;
    private String checkcode;

    @Override
    public String toString() {
        return "Useremail{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", emailstate=" + emailstate +
                ", existdate='" + existdate + '\'' +
                ", checkcode='" + checkcode + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEmailstate() {
        return emailstate;
    }

    public void setEmailstate(int emailstate) {
        this.emailstate = emailstate;
    }

    public String getExistdate() {
        return existdate;
    }

    public void setExistdate(String existdate) {
        this.existdate = existdate;
    }

    public String getCheckcode() {
        return checkcode;
    }

    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode;
    }
}
