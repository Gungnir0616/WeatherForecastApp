package com.lhanman.lhanweather.db;

import java.io.Serializable;

public class Redian implements Serializable {
    private int id;
    private String author;
    private String leixing;
    private String timest;
    private String biaoti;
    private String xiangqing;
    private String imag;
    public Redian() {

    }
    public Redian(int id, String author, String timest, String leixing, String biaoti, String xiangqing, String imag) {
        this.id = id;
        this.author = author;
        this.timest = timest;
        this.leixing=leixing;
        this.biaoti = biaoti;
        this.xiangqing = xiangqing;
        this.imag = imag;
    }

    public String getLeixing() {
        return leixing;
    }

    public void setLeixing(String leixing) {
        this.leixing = leixing;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTimest() {
        return timest;
    }

    public void setTimest(String timest) {
        this.timest = timest;
    }

    public String getBiaoti() {
        return biaoti;
    }

    public void setBiaoti(String biaoti) {
        this.biaoti = biaoti;
    }

    public String getXiangqing() {
        return xiangqing;
    }

    public void setXiangqing(String xiangqing) {
        this.xiangqing = xiangqing;
    }

    public String getImag() {
        return imag;
    }

    public void setImag(String imag) {
        this.imag = imag;
    }
}
