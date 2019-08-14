package edu.uta.leanmed.pojo;

import java.util.List;

public class BoxResponse {

    private int status;
    private String message;
    private List<Box> box;


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

    public List<Box> getBox() {
        return box;
    }

    public void setBox(List<Box> box) {
        this.box = box;
    }
}
