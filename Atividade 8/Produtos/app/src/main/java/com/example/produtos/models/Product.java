package com.example.produtos.models;

import android.graphics.Bitmap;

public class Product {
    public String pid;
    public String name;
    public String price;
    public String description;
    public Bitmap photo;

    public Product(String pid, String nome) {
        this.pid = pid;
        this.name = nome;
    }

    public Product(String pid, String name, String price, String description, Bitmap photo) {
        this.pid = pid;
        this.name = name;
        this.price = price;
        this.description = description;
        this.photo = photo;
    }

    public Product(String name, String price, String description, Bitmap photo) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.photo = photo;
    }
}
