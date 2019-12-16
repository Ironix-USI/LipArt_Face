package com.mobile.lipart.model;

import java.util.ArrayList;

/***
 * Brand model
 ***/
public class Brand {
    private String name;
    private ArrayList<LipstickSeries> lipstickSeries;

    public Brand(String name, ArrayList<LipstickSeries> lipstickSeries) {
        this.name = name;
        this.lipstickSeries = lipstickSeries;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<LipstickSeries> getLipstickSeries() {
        return lipstickSeries;
    }

    public void setLipstickSeries(ArrayList<LipstickSeries> lipstickSeries) {
        this.lipstickSeries = lipstickSeries;
    }
}
