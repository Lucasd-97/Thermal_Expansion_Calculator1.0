package com.example.myapplication1;

public class MetalModel {

    private final int id;
    private final String type;
    private final Float expansion;

    public MetalModel(int id, String type, Float expansion) {
        this.id = id;
        this.type = type;
        this.expansion = expansion;
    }

    @Override
    public String toString() {
        return "id:" + id +"   " + type +"   " + expansion;
    }

    public Float getExpansion() {
        return expansion;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }
}
