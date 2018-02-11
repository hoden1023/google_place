package com.example.icecream.new_job.entity;

/**
 * Created by IceCream on 2/10/2018.
 */

public class PlaceInfo {
    private Double lat;
    private Double lng;
    private String name;
    private String vicinity;

    public PlaceInfo() {
        this.lat = null;
        this.lng = null;
        this.name = "";
        this.vicinity = "";
    }

    public PlaceInfo(Double lat, Double lng, String name, String vicinity) {
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.vicinity = vicinity;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
