package edu.uta.leanmed.pojo;

import java.io.Serializable;

/**
 * Created by Vaibhav's Console on 3/8/2019.
 */

public class Zone implements Serializable {
    private String zoneId;
    private String zone;
    private String zoneName;
    private String zoneEmail;
    private String zoneCountry;

    public Zone() {
    }
    public Zone(String zoneId) {
        this.zoneId=zoneId;
    }

    public Zone(String zoneId, String zone, String zoneName, String zoneEmail, String zoneCountry) {
        this.zoneId = zoneId;
        this.zone = zone;
        this.zoneName = zoneName;
        this.zoneEmail = zoneEmail;
        this.zoneCountry = zoneCountry;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getZoneEmail() {
        return zoneEmail;
    }

    public void setZoneEmail(String zoneEmail) {
        this.zoneEmail = zoneEmail;
    }

    public String getZoneCountry() {
        return zoneCountry;
    }

    public void setZoneCountry(String zoneCountry) {
        this.zoneCountry = zoneCountry;
    }
}
