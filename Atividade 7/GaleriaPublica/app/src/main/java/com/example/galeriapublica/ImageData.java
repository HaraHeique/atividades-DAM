package com.example.galeriapublica;

import android.graphics.Bitmap;

import java.util.Date;

public class ImageData {

    public Bitmap thumb;
    public String filename;
    public Date date;
    public int size;

    public ImageData(Bitmap thumb, String filename, Date date, int size) {
        this.thumb = thumb;
        this.filename = filename;
        this.date = date;
        this.size = size;
    }
}
