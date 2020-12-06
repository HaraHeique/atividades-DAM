package com.example.mylist;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

// ViewModel relativa a activity de NewItem, onde define as propriedades referente ao elemento de layout que aparece na view
public class NewItemActivityViewModel extends ViewModel {

    Uri selectPhotoLocation;

    // Estes elementos não tem necessidade de estar dentro da view model relativo a mudança de forma de visualização (landscape ou não)
    // String title;
    // String description;

    // getter de foto
    public Uri getSelectPhotoLocation() {
        return selectPhotoLocation;
    }

    // setter de foto
    public void setSelectPhotoLocation(Uri selectPhotoLocation) {
        this.selectPhotoLocation = selectPhotoLocation;
    }
}
