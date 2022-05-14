package com.example.lifehealthassistant.bean;


public class Diet {


    private String datetime;

    private String food;
    private String dietname;
    private String picture1;
    private String picture2;
    private String picture3;

    public Diet(){}
    public Diet(String datetime, String food, String dietname) {
        this.datetime = datetime;

        this.food = food;
        this.dietname = dietname;
        this.picture1 = null;
        this.picture2 = null;
        this.picture3 = null;
    }
    public Diet(String datetime, String food, String dietname,String picture1,String picture2,String picture3) {
        this.datetime = datetime;

        this.food = food;
        this.dietname = dietname;
        this.picture1 = picture1;
        this.picture2 = picture2;
        this.picture3 = picture3;
    }

    @Override
    public String toString() {

            return "Diet{" +
                    "datetime=" + datetime +
                    ", food='" + food + '\'' +
                    ", dietname='" + dietname + '\'' +
                    ", picture1='" + picture1 + '\'' +
                    ", picture2='" + picture2 + '\'' +
                    ", picture3='" + picture3 + '\'' +
                    '}';



    }


    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getDietname() {
        return dietname;
    }

    public void setDietname(String dietname) {
        this.dietname = dietname;
    }

    public String getPicture1() {
        return picture1;
    }

    public void setPicture1(String picture1) {
        this.picture1 = picture1;
    }

    public String getPicture2() {
        return picture2;
    }

    public void setPicture2(String picture2) {
        this.picture2 = picture2;
    }

    public String getPicture3() {
        return picture3;
    }

    public void setPicture3(String picture3) {
        this.picture3 = picture3;
    }
}
