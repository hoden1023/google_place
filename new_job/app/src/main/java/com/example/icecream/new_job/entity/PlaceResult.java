package com.example.icecream.new_job.entity;

import java.util.ArrayList;

/**
 * Created by IceCream on 2/11/2018.
 */

public class PlaceResult {
    private ArrayList<PlaceInfo> listResult;
    private String pagetoken;

    public PlaceResult() {
        this.listResult = new ArrayList<>();
        this.pagetoken = "";
    }

    public PlaceResult(ArrayList<PlaceInfo> listResult, String pagetoken) {
        this.listResult = listResult;
        this.pagetoken = pagetoken;
    }

    public ArrayList<PlaceInfo> getListResult() {
        return listResult;
    }

    public void setListResult(ArrayList<PlaceInfo> listResult) {
        this.listResult = listResult;
    }

    public String getPagetoken() {
        return pagetoken;
    }

    public void setPagetoken(String pagetoken) {
        this.pagetoken = pagetoken;
    }
}
