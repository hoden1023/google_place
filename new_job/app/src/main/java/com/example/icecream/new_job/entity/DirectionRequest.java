package com.example.icecream.new_job.entity;

import com.example.icecream.new_job.data.Constants;

/**
 * Created by IceCream on 2/11/2018.
 */

public class DirectionRequest {
    private String origin;
    private String destination;
    private String mode;
    private String avoid;

    public DirectionRequest() {
        this.origin = "";
        this.destination = "";
        this.mode = "";
        this.avoid = "";
    }

    public String getUrlRequest()
    {
        return Constants.SEARCH_ROUTES_URL+"origin="+origin+"&destination="+destination+"&key="+Constants.GOOGLE_MAP_KEY;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getAvoid() {
        return avoid;
    }

    public void setAvoid(String avoid) {
        this.avoid = avoid;
    }
}
