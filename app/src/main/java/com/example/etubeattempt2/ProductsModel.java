package com.example.etubeattempt2;

public class ProductsModel {
    private String title;
    private String date;

    public ProductsModel() {
    }

    public ProductsModel(String title, String date) {
        this.title = title;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}


