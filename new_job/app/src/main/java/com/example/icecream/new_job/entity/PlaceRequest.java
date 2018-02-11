package com.example.icecream.new_job.entity;

import com.example.icecream.new_job.data.Constants;

/**
 * Created by IceCream on 2/11/2018.
 */

public class PlaceRequest {
    private String location;
    private String radius;
    private String type;
    private String pagetoken;

    public PlaceRequest(String location, String radius, String type, String pagetoken) {
        this.location = location;
        this.radius = radius;
        this.type = type;
        this.pagetoken = pagetoken;
    }


    public PlaceRequest() {
        this.location = "";
        this.radius = "";
        this.type = "";
        this.pagetoken = "";
    }

    public String getUrlRequest() {
        if ("".equals(pagetoken))
        {
            return Constants.SEARCH_PLACE_URL+"location="+location+"&radius="+radius+"&type="+type+"&key="+Constants.GOOGLE_MAP_KEY;
        }

        return Constants.SEARCH_PLACE_URL+"pagetoken="+pagetoken+"&key="+Constants.GOOGLE_MAP_KEY;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getPagetoken() {
        return pagetoken;
    }

    public void setPagetoken(String pagetoken) {
        this.pagetoken = pagetoken;
    }
}
