package com.mobile.lipart.model;

import java.util.ArrayList;

/***
 * LipstickSeries model
 ***/
public class LipstickSeries {
    private String name;
    private String link;
    private ArrayList<Lipstick> lipsticks;

    public LipstickSeries(String name, String link, ArrayList<Lipstick> lipsticks) {
        this.name = name;
        this.link = link;
        this.lipsticks = lipsticks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ArrayList<Lipstick> getLipsticks() {
        return lipsticks;
    }

    public void setLipsticks(ArrayList<Lipstick> lipsticks) {
        this.lipsticks = lipsticks;
    }
}
