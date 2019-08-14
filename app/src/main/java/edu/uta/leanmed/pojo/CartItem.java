package edu.uta.leanmed.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Vaibhav's Console on 3/23/2019.
 */

public class CartItem implements Serializable {
    private Inventory inventory;
    private int count;
    private Zone requestedZone;

    public CartItem() {
    }

    public CartItem(Inventory inventory, int count, Zone requestedZone) {
        this.inventory = inventory;
        this.count = count;
        this.requestedZone = requestedZone;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Zone getRequestedZone() {
        return requestedZone;
    }

    public void setRequestedZone(Zone requestedZone) {
        this.requestedZone = requestedZone;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "inventory=" + inventory +
                ", count=" + count +
                ", requestedZone=" + requestedZone +
                '}';
    }
}
