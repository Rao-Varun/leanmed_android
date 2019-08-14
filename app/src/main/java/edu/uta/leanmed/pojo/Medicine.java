package edu.uta.leanmed.pojo;

import java.io.Serializable;

/**
 * Created by Vaibhav's Console on 3/23/2019.
 */

public class Medicine implements Serializable{
    private int medicineId;
    private String genName;
    private String tradeName;
    private String medicineType;
    private String dosage;
    private String weight;

    public Medicine() {
    }

    public Medicine(int medicineId, String genName, String tradeName, String medicineType, String dosage, String weight) {
        this.medicineId = medicineId;
        this.genName = genName;
        this.tradeName = tradeName;
        this.medicineType = medicineType;
        this.dosage = dosage;
        this.weight = weight;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public String getGenName() {
        return genName;
    }

    public void setGenName(String genName) {
        this.genName = genName;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getMedicineType() {
        return medicineType;
    }

    public void setMedicineType(String medicineType) {
        this.medicineType = medicineType;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

}
