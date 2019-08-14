package edu.uta.leanmed.pojo;

/**
 * Created by Vaibhav's Console on 4/8/2019.
 */

public class Donor {
    private int donorId;
    private String donorName;
    private String donorPhone;

    public Donor() {
    }

    public Donor(int donorId, String donorName, String donorPhone) {
        this.donorId = donorId;
        this.donorName = donorName;
        this.donorPhone = donorPhone;
    }

    public int getDonorId() {
        return donorId;
    }

    public void setDonorId(int donorId) {
        this.donorId = donorId;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public String getDonorPhone() {
        return donorPhone;
    }

    public void setDonorPhone(String donorPhone) {
        this.donorPhone = donorPhone;
    }

}
