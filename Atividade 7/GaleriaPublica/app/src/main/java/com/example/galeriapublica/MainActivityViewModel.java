package com.example.galeriapublica;

import androidx.lifecycle.ViewModel;

import java.util.HashMap;

// Basicamente serve para guardar os estados de uma Activity. Lembrando que info com dados primitivos já são guardados pelo próprio Android
public class MainActivityViewModel extends ViewModel {
    HashMap<Long, ImageData> imageDataList = new HashMap<>();
    int navigationOpSelected = R.id.gridViewOp;

    public HashMap<Long, ImageData> getImageDataList() {
        return imageDataList;
    }

    public int getNavigationOpSelected() {
        return navigationOpSelected;
    }

    public void setNavigationOpSelected(int navigationOpSelected) {
        this.navigationOpSelected = navigationOpSelected;
    }
}
