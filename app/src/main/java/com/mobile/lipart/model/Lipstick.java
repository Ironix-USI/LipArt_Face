package com.mobile.lipart.model;

public class Lipstick {
    private String id;
    private String color;
    private String name;

    public Lipstick(String id, String color, String name) {
        this.id = id;
        this.color = color;
        this.name = name;
    }

    public Lipstick(String color, String name) { this("none",color, name); }


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
}
