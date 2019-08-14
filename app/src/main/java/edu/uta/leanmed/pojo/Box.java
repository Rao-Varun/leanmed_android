package edu.uta.leanmed.pojo;

import java.io.Serializable;
import java.util.List;

public class Box implements Serializable {
    private int boxId;
    private String boxName;
    private User createdUser;
    private Zone destinationZone;
    private int status;
    private String deliveredDate;
    private String creationDate;
    private List<BoxContent> boxContent;

    public Box(){

    }

    public Box(int boxId, String boxName, User createdUser, Zone destinationZone, String creationDate){
        this.boxId = boxId;
        this.boxName = boxName;
        this.createdUser = createdUser;
        this.destinationZone = destinationZone;
        this.creationDate = creationDate;
    }

    public Box(int boxId, String boxName, User createdUser, Zone destinationZone, int status, String deliveredDate, String creationDate, List<BoxContent> boxContent){
        this.boxId = boxId;
        this.boxName = boxName;
        this.createdUser = createdUser;
        this.destinationZone= destinationZone;
        this.status = status;
        this.deliveredDate = deliveredDate;
        this.creationDate = creationDate;
        this.boxContent = boxContent;

    }

    public String getBoxName() {
        return boxName;
    }

    public void setBoxName(String boxName) {
        this.boxName = boxName;
    }

    public int getBoxId() {
        return boxId;
    }

    public void setBoxId(int boxId) {
        this.boxId = boxId;
    }

    public User getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(User createdUser) {
        this.createdUser = createdUser;
    }

    public Zone getDestinationZone() {
        return destinationZone;
    }

    public void setDestinationZone(Zone destinationZone) {
        this.destinationZone = destinationZone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(String deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public List<BoxContent> getBoxContent() {
        return boxContent;
    }

    public void setBoxContent(List<BoxContent> boxContent) {
        this.boxContent = boxContent;
    }
}
