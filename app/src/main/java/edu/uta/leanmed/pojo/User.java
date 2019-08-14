package edu.uta.leanmed.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Vaibhav's Console on 3/8/2019.
 */

public class User implements Serializable{
    private String userName;
    private String emailId;
    private String contacts;
    private String leanIDF;
    private String leanIDAddress;
    private String identity;
    private String userAddress;
    private String city;
    private String state;
    private String country;
    private String password;
    private int type;
    private Zone zone;
    private int userStatus;
    private int languagePref;
    private String token;
    //private String currentRole;
    public User(){}

    public User(String emailId){
        this.emailId = emailId;
    }

    public User(String emailId, String password) {
        this.emailId = emailId;
        this.password = password;
    }

    public User(String userName, String emailId, String contacts, String userAddress, String city, String state, String country, String password, int type, String zoneId, int userStatus, int langPref){
        this.userName = userName;
        this.emailId = emailId;
        this.contacts = contacts;
        this.userAddress = userAddress;
        this.city = city;
        this.state = state;
        this.country = country;
        this.password = password;
        this.type = type;
        this.zone = new Zone(zoneId);
        this.userStatus = userStatus;
        this.languagePref = langPref;
    }

    public User(String userName, String emailId, String contacts, String leanIDF, String leanIDAddress, String identity, String userAddress, String city, String state, String country, String password, int type, Zone zone, int userStatus, int languagePref) {
        this.userName = userName;
        this.emailId = emailId;
        this.contacts = contacts;
        this.leanIDF = leanIDF;
        this.leanIDAddress = leanIDAddress;
        this.identity = identity;
        this.userAddress = userAddress;
        this.city = city;
        this.state = state;
        this.country = country;
        this.password = password;
        this.type = type;
        this.zone = zone;
        this.userStatus = userStatus;
        this.languagePref = languagePref;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getLeanIDF() {
        return leanIDF;
    }

    public void setLeanIDF(String leanIDF) {
        this.leanIDF = leanIDF;
    }

    public String getLeanIDAddress() {
        return leanIDAddress;
    }

    public void setLeanIDAddress(String leanIDAddress) {
        this.leanIDAddress = leanIDAddress;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public int getLanguagePref() {
        return languagePref;
    }

    public void setLanguagePref(int languagePref) {
        this.languagePref = languagePref;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
