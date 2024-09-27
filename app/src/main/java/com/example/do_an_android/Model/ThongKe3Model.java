package com.example.do_an_android.Model;

public class ThongKe3Model {
    private String tenllinhkien;
    private int soluongban;
    private double phantram;

    public ThongKe3Model(String tenllinhkien, int soluongban, double phantram) {
        this.tenllinhkien = tenllinhkien;
        this.soluongban = soluongban;
        this.phantram = phantram;
    }

    public String getTenllinhkien() {
        return tenllinhkien;
    }

    public int getSoluongban() {
        return soluongban;
    }

    public double getPhantram() {
        return phantram;
    }
}
