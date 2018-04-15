package com.threenitas.basicnavigator;

import io.realm.RealmObject;

public class LocInfo extends RealmObject {

    public LocInfo() {
    }

    public LocInfo(double latFrom, double lngFrom, String addressFrom, String nameFrom, double latTo, double lngTo, String addressTo, String namePlaceTo) {

        this.latFrom = latFrom;
        this.lngFrom = lngFrom;
        this.addressFrom = addressFrom;
        this.nameFrom = nameFrom;
        this.latTo = latTo;
        this.lngTo = lngTo;
        this.addressTo = addressTo;
        this.namePlaceTo = namePlaceTo;
    }

    public double getLatFrom() {
        return latFrom;
    }

    public void setLatFrom(double latFrom) {
        this.latFrom = latFrom;
    }

    public double getLngFrom() {
        return lngFrom;
    }

    public void setLngFrom(double lngFrom) {
        this.lngFrom = lngFrom;
    }

    public String getAddressFrom() {
        return addressFrom;
    }

    public void setAddressFrom(String addressFrom) {
        this.addressFrom = addressFrom;
    }

    public String getNameFrom() {
        return nameFrom;
    }

    public void setNameFrom(String nameFrom) {
        this.nameFrom = nameFrom;
    }

    public double getLatTo() {
        return latTo;
    }

    public void setLatTo(double latTo) {
        this.latTo = latTo;
    }

    public double getLngTo() {
        return lngTo;
    }

    public void setLngTo(double lngTo) {
        this.lngTo = lngTo;
    }

    public String getAddressTo() {
        return addressTo;
    }

    public void setAddressTo(String addressTo) {
        this.addressTo = addressTo;
    }

    public String getNamePlaceTo() {
        return namePlaceTo;
    }

    public void setNamePlaceTo(String namePlaceTo) {
        this.namePlaceTo = namePlaceTo;
    }

    private double latFrom;
    private double lngFrom;
    private String addressFrom;
    private String nameFrom;
    private double latTo;
    private double lngTo;
    private String addressTo;
    private String namePlaceTo;

}
