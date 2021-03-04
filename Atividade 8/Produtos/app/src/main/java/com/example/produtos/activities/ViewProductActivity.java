package com.example.produtos.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.produtos.R;
import com.example.produtos.models.Product;
import com.example.produtos.viewmodels.ViewProductViewModel;

import java.util.ArrayList;
import java.util.List;

public class ViewProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        setupViewProductViewModel();
    }

    private void setupViewProductViewModel() {
        // Obtém a intent do evento de click do produto na MainActivity
        Intent intent = getIntent();
        String pid = intent.getStringExtra("pid");

        // Instancia a factory e passa ela como argumento ao instanciar a view model de view dos products de forma singleton
        ViewProductViewModel.ViewProductViewModelFactory factory = new ViewProductViewModel.ViewProductViewModelFactory(pid);
        ViewProductViewModel viewProductViewModel = new ViewModelProvider(this, factory).get(ViewProductViewModel.class);

        // Binding de observação da lista de produtos do livedata
        LiveData<Product> product = viewProductViewModel.getProduct();
        product.observe(this, new Observer<Product>() {
            @Override
            public void onChanged(Product product) {
                setInfoFields(product);
            }
        });
    }

    private void setInfoFields(Product product) {
        // Obtém o image view por ID da foto do produto e seta a imagem nele
        ImageView imvProductPhoto = findViewById(R.id.imv_product_image);
        imvProductPhoto.setImageBitmap(product.photo);

        // Obtém o text view por ID e seta a informação de nome do produto nele
        TextView tvName = findViewById(R.id.tv_product_name);
        tvName.setText(product.name);

        // Obtém o text view por ID e seta a informação de preço do produto nele
        TextView tvPreco = findViewById(R.id.tv_product_price);
        tvPreco.setText(product.price);

        // Obtém o text view por ID e seta a informação de descrição do produto nele
        TextView tvDescricao = findViewById(R.id.tv_product_description);
        tvDescricao.setText(product.description);
    }
}