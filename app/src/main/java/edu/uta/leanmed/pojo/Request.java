package edu.uta.leanmed.pojo;

import java.io.Serializable;

/**
 * Created by Vaibhav's Console on 3/23/2019.
 */

public class Request implements Serializable {
    private int requestId;
    private Inventory inventory;
    private int quantity;
    private Zone zone;
    private int status;
    private String comments;
    private int orderId;
    private String createdDate;
    private String modifiedDate;
    private User createdUser;
    private User acceptedUser;

    public Request() {
    }
    public Request(Inventory inventory,Zone zone,int quantity) {
        this.inventory=inventory;
        this.zone=zone;
        this.quantity=quantity;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public User getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(User createdUser) {
        this.createdUser = createdUser;
    }

    public User getAcceptedUser() {
        return acceptedUser;
    }

    public void setAcceptedUser(User acceptedUser) {
        this.acceptedUser = acceptedUser;
    }

    @Override
    public String toString() {
        return "Request{" +
                "requestId=" + requestId +
                ", inventory=" + inventory +
                ", quantity=" + quantity +
                ", zone=" + zone +
                ", status='" + status + '\'' +
                ", comments='" + comments + '\'' +
                ", orderId=" + orderId +
                ", createdDate='" + createdDate + '\'' +
                ", modifiedDate='" + modifiedDate + '\'' +
                ", createdUser=" + createdUser +
                ", acceptedUser=" + acceptedUser +
                '}';
    }
}
