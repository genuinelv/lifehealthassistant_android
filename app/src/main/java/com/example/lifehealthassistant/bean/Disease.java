package com.example.lifehealthassistant.bean;

import java.io.Serializable;

public class Disease implements Serializable {
    private String datestart;
    private String dateend;
    private String diseasename;
    private String symptom;
    private String sympic;
    private String medicine;
    private String medpic;

    public Disease(){}

    public Disease(String datestart, String dateend, String diseasename,
                   String symptom, String sympic, String medicine, String medpic) {
        this.datestart = datestart;
        this.dateend = dateend;
        this.diseasename = diseasename;
        this.symptom = symptom;
        this.sympic = sympic;
        this.medicine = medicine;
        this.medpic = medpic;
    }

    @Override
    public String toString() {
        return "Disease{" +
                "datestart='" + datestart + '\'' +
                ", dateend='" + dateend + '\'' +
                ", diseasename='" + diseasename + '\'' +
                ", symptom='" + symptom + '\'' +
                ", sympic='" + sympic + '\'' +
                ", medicine='" + medicine + '\'' +
                ", medpic='" + medpic + '\'' +
                '}';
    }

    public String getDatestart() {
        return datestart;
    }

    public void setDatestart(String datestart) {
        this.datestart = datestart;
    }

    public String getDateend() {
        return dateend;
    }

    public void setDateend(String dateend) {
        this.dateend = dateend;
    }

    public String getDiseasename() {
        return diseasename;
    }

    public void setDiseasename(String diseasename) {
        this.diseasename = diseasename;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public String getSympic() {
        return sympic;
    }

    public void setSympic(String sympic) {
        this.sympic = sympic;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public String getMedpic() {
        return medpic;
    }

    public void setMedpic(String medpic) {
        this.medpic = medpic;
    }
}
