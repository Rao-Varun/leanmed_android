package edu.uta.leanmed.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Vaibhav's Console on 3/23/2019.
 */

public class Patient implements Serializable {
    private int patientId;
    private String firstName;
    private String lastName;
    private String dob;
    private String contact;
    private String address;
    private String email;
    private String city;
    private String state;
    private String country;
    private List<Integer> ordersIds;
    public Patient() {
    }
    public Patient(String firstName, String lastName, String dob, String contact, String address, String email, String city, String state, String country) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.contact = contact;
        this.address = address;
        this.email = email;
        this.city = city;
        this.state = state;
        this.country = country;
    }
    public Patient(int patientId, String firstName, String lastName, String dob, String contact, String address, String email, String city, String state, String country, List<Integer> ordersIds) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.contact = contact;
        this.address = address;
        this.email = email;
        this.city = city;
        this.state = state;
        this.country = country;
        this.ordersIds = ordersIds;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Integer> getOrdersIds() {
        return ordersIds;
    }

    public void setOrdersIds(List<Integer> ordersIds) {
        this.ordersIds = ordersIds;
    }
}
