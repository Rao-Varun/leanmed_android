package edu.uta.leanmed.pojo;

import java.util.List;

public class DonorResponse {
    private int status;
    private String message;
    private List<Donor> donor;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Donor> getDonor() {
        return donor;
    }

    public void setDonor(List<Donor> donors) {
        this.donor = donors;
    }


}
