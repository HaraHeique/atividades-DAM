package com.example.mylist;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

// Classe de modelo de visualização dos dados de uma view/arquivo de layout da aplicação
public class MainActivityViewModel extends ViewModel {

    List<MyItem> itens = new ArrayList<>();

    // getter dos itens da lista de itens da página principal
    public List<MyItem> getItens() {
        return this.itens;
    }
}
