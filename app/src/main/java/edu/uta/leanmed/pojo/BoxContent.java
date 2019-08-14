package edu.uta.leanmed.pojo;

import java.io.Serializable;

public class BoxContent implements Serializable {

    private int boxContentId;
    private Inventory inventory;
    private Request request;
    private int boxId;
    private User receivingUser;
    private int units;

    public BoxContent(){

    }

    public BoxContent(int boxContentId, Inventory inventory, Request request, int boxId, User receivingUser, int units){
        this.boxContentId = boxContentId;
        this.inventory = inventory;
        this.request = request;
        this.boxId = boxId;
        this.receivingUser = receivingUser;
        this.units = units;

    }

    public BoxContent(Inventory inventory, Request request, int boxId, User receivingUser, int units){
        this.inventory = inventory;
        this.request = request;
        this.boxId = boxId;
        this.receivingUser = receivingUser;
        this.units = units;

    }

    public BoxContent(Inventory inventory, Request request, User receivingUser, int units){
        this.inventory = inventory;
        this.request = request;
        this.receivingUser = receivingUser;
        this.units = units;

    }

    public BoxContent(Inventory inventory, int boxId, User receivingUser, int units){
        this.inventory = inventory;
        this.boxId = boxId;
        this.receivingUser = receivingUser;
        this.units = units;
    }

    public int getBoxContentId() {
        return boxContentId;
    }

    public void setBoxContentId(int boxContentId) {
        this.boxContentId = boxContentId;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public int getBoxId() {
        return boxId;
    }

    public void setBoxId(int boxId) {
        this.boxId = boxId;
    }

    public User getReceivingUser() {
        return receivingUser;
    }

    public void setReceivingUser(User receivingUser) {
        this.receivingUser = receivingUser;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }
}
