package edu.uta.leanmed.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Vaibhav's Console on 3/23/2019.
 */

public class Order implements Serializable {
    private int orderId;
    private String orderDate;
    private String recievingDate;
    private String prescriptionImage;
    private String confirmationImage;
    private String comments;
    private String status;
    private User createdUser;
    private Patient patient;
    private List<Request> requests;

    public Order() {
    }

    public Order(int orderId, String orderDate, String recievingDate, String prescriptionImage, String confirmationImage, String comments, String status, User createdUser, Patient patient, List<Request> requests) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.recievingDate = recievingDate;
        this.prescriptionImage = prescriptionImage;
        this.confirmationImage = confirmationImage;
        this.comments = comments;
        this.status = status;
        this.createdUser = createdUser;
        this.patient = patient;
        this.requests = requests;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getRecievingDate() {
        return recievingDate;
    }

    public void setRecievingDate(String recievingDate) {
        this.recievingDate = recievingDate;
    }

    public String isPrescriptionImage() {
        return prescriptionImage;
    }

    public void setPrescriptionImage(String prescriptionImage) {
        this.prescriptionImage = prescriptionImage;
    }

    public String isConfirmationImage() {
        return confirmationImage;
    }

    public void setConfirmationImage(String confirmationImage) {
        this.confirmationImage = confirmationImage;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(User createdUser) {
        this.createdUser = createdUser;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderDate='" + orderDate + '\'' +
                ", recievingDate='" + recievingDate + '\'' +
                ", prescriptionImage='" + prescriptionImage + '\'' +
                ", confirmationImage='" + confirmationImage + '\'' +
                ", comments='" + comments + '\'' +
                ", status='" + status + '\'' +
                ", createdUser=" + createdUser +
                ", patient=" + patient +
                ", requests=" + requests +
                '}';
    }
}
