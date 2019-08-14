package edu.uta.leanmed.pojo;

import java.io.Serializable;

/**
 * Created by Vaibhav's Console on 3/23/2019.
 */

public class Inventory implements Serializable {
    private int inventoryId;
    private Medicine medicine;
    private int units;
    private String expiryDate;
    private String donationDate;
    private String idBox = "0.0";
    private Donor donor;
    private Zone zone;
    private User user;

    public Inventory() {
    }

    public Inventory(int inventoryId, Medicine medicine, int units, String expiryDate, String donationDate, String idBox, Donor donor,Zone zone) {
        this.inventoryId = inventoryId;
        this.medicine = medicine;
        this.units = units;
        this.expiryDate = expiryDate;
        this.donationDate = donationDate;
        this.idBox = idBox;
        this.donor = donor;
        this.zone=zone;
    }
    public Inventory(int inventoryId, Medicine medicine, int units, String expiryDate, String donationDate, String idBox, Donor donor,Zone zone, User user) {
        this.inventoryId = inventoryId;
        this.medicine = medicine;
        this.units = units;
        this.expiryDate = expiryDate;
        this.donationDate = donationDate;
        this.idBox = idBox;
        this.donor = donor;
        this.zone=zone;
        this.user = user;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(String donationDate) {
        this.donationDate = donationDate;
    }

    public String getIdBox() {
        return idBox;
    }

    public void setIdBox(String idBox) {
        this.idBox = idBox;
    }

    public Donor getDonor() {
        return donor;
    }

    public void setDonor(Donor donor) {
        this.donor = donor;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "inventoryId=" + inventoryId +
                ", medicine=" + medicine +
                ", units=" + units +
                ", expiryDate='" + expiryDate + '\'' +
                ", donationDate='" + donationDate + '\'' +
                ", idBox='" + idBox + '\'' +
                ", donor=" + donor +
                ", zone=" + zone +
                '}';
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }
}
