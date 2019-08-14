package edu.uta.leanmed.pojo;

import java.util.List;

/**
 * Created by Vaibhav's Console on 4/21/2019.
 */

public class InventoryResponse {
    private int status;
    private String message;
    private List<Inventory> inventory;

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

    public List<Inventory> getInventory() {
        return inventory;
    }

    public void setInventory(List<Inventory> inventory) {
        this.inventory = inventory;
    }
}
