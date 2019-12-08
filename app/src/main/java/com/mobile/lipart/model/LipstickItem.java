package com.mobile.lipart.model;

public class LipstickItem {
    private String id;
    private String color;
    private String name;
    private String brandName;
    private String seriesName;
    private String link;

    public LipstickItem(String id, String color, String name, String brandName, String seriesName, String link) {
        this.id = id;
        this.color = color;
        this.name = name;
        this.brandName = brandName;
        this.seriesName = seriesName;
        this.link = link;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color){
        this.color = color;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String name) {
        this.brandName = name;
    }
    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String name) {
        this.seriesName = name;
    }
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
