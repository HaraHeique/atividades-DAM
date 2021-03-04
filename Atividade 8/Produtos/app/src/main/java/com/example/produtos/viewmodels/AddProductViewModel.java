package com.example.produtos.viewmodels;

import androidx.lifecycle.ViewModel;

public class AddProductViewModel extends ViewModel {

    public AddProductViewModel() {
        this.currentPhotoPath = "";
    }

    // Guarda a foto do produto tirada pelo usuário
    public String currentPhotoPath;
}
