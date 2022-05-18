package com.example.lifehealthassistant.bean;


import java.io.Serializable;
import java.util.Date;

public class Health implements Serializable {

    private String datetimehealth;
    private int age;
    private double height;
    private double weight;
    private int bloodpressurehigh;
    private int bloodpressurelow;
    private double bloodsugar;

    @Override
    public String toString() {
        return "Health{" +
                "datetimehealth='" + datetimehealth + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                ", bloodpressurehigh=" + bloodpressurehigh +
                ", bloodpressurelow=" + bloodpressurelow +
                ", bloodsugar=" + bloodsugar +
                '}';
    }

    public String getDatetimehealth() {
        return datetimehealth;
    }

    public void setDatetimehealth(String datetime_health) {
        this.datetimehealth = datetime_health;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getBloodpressurehigh() {
        return bloodpressurehigh;
    }

    public void setBloodpressurehigh(int bloodpressurehigh) {
        this.bloodpressurehigh = bloodpressurehigh;
    }

    public int getBloodpressurelow() {
        return bloodpressurelow;
    }

    public void setBloodpressurelow(int bloodpressurelow) {
        this.bloodpressurelow = bloodpressurelow;
    }

    public double getBloodsugar() {
        return bloodsugar;
    }

    public void setBloodsugar(double bloodsugar) {
        this.bloodsugar = bloodsugar;
    }
}
