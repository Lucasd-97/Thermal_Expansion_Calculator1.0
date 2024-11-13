package com.example.myapplication1;

public class Metaldb {

    private String type;
    private Float ratio;



    //construct


    public Metaldb(Float ratio, String type) {
        this.ratio = ratio;
        this.type = type;
    }

    public Metaldb() {
    }

    //Getters and Setters

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getRatio() {
        return ratio;
    }

    public void setRatio(Float ratio) {
        this.ratio = ratio;
    }

    //To String
    @Override
    public String toString() {
        return "Metaldb{" +
                "type='" + type + '\'' +
                ", ratio=" + ratio +
                '}';
    }
}
