package com.example.etubeattempt2;

public class ProductsModel {
    private String title;
    private String month;
    private  String price;
    private  String name;

    public ProductsModel(String title, String month, String price, String name) {
        this.title = title;
        this.month = month;
        this.price = price;
        this.name = name;
    }


    public ProductsModel() {
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


