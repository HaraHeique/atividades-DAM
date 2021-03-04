package com.example.produtos.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.produtos.BuildConfig;
import com.example.produtos.R;
import com.example.produtos.adapters.ProductAdapter;
import com.example.produtos.models.Product;
import com.example.produtos.viewmodels.MainViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_PRODUCT_RESULT = 1;
    private static final int RESULT_REQUEST_PERMISSION = 2;

    private Context context = MainActivity.this;
    private MainViewModel mainViewModel;
    private RecyclerView rvProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Cria uma lista de string para checar se o usuário tem permissão ou não de acessar determinados recursos
        List<String> permissions = Arrays.asList(Manifest.permission.CAMERA);

        // Pede permissões ao usuário de acessar os recursos desejados
        askForPermissions(permissions);

        setupMainViewModel();
        setupRecycleProductList();
        onClickBtnNewProduct();
    }

    private void setupRecycleProductList() {
        // Obtém o recycle view de lista de produtos e define um tamanho fixo e layout manager de lista
        rvProduct = findViewById(R.id.rv_new_product);
        rvProduct.setHasFixedSize(true);
        rvProduct.setLayoutManager(new LinearLayoutManager(context));
    }

    private void setupMainViewModel() {
        // Instanciando de forma singleton a view model da main activity
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Obtendo a lista de produtos com o wrapping do livedata para observar mudanças na lista
        LiveData<List<Product>> productsVM = mainViewModel.getProducts();

        // Binding do evento de observação da lista de produtos do livedata
        productsVM.observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                ProductAdapter adapter = new ProductAdapter(products);
                rvProduct.setAdapter(adapter);
            }
        });
    }

    private void onClickBtnNewProduct() {
        Button button = findViewById(R.id.btn_new_product);

        button.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddProductActivity.class);
            startActivityForResult(intent, ADD_PRODUCT_RESULT);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_PRODUCT_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                // Pedir produtos do server novamente
                mainViewModel.refreshProducts();
            }
        }
    }

    //region Permissions

    // Função que verifica se a lista de permissões passadas são válidas
    private void askForPermissions(List<String> permissions) {
        List<String> permissionNotGranted = new ArrayList<>();

        // Verificar se permissão já é existente ou não
        for (String permission : permissions) {
            if (hasPermission(permission)) {
                permissionNotGranted.add(permission);
            }
        }

        // Se não tem aquela permissão tem que requerí-la
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionNotGranted.size() > 0) {
                // Pedir ao usuário para conceder permissão de acessar os dados de foto
                requestPermissions(permissionNotGranted.toArray(new String[permissionNotGranted.size()]), RESULT_REQUEST_PERMISSION);
            }
        }
    }

    // Checa se há ou não a permissão a partir da versão do SO Android, pois como este código utiliza
    // android na versão 30 é necessário
    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ActivityCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    // Após ser pedido a permissão de acessar a câmera
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        List<String> permissionsRejected = new ArrayList<>();

        // Caso o código da permissão seja de acessar a câmera
        if (requestCode == RESULT_REQUEST_PERMISSION) {
            // Checa quais permissões o usuário se negou a conceder
            for (String permission : permissions) {
                if (hasPermission(permission)) {
                    permissionsRejected.add(permission);
                }
            }
        }

        // O android com sua chata política pergunta ao usuário novamente se ele quer dar permissão
        if (permissionsRejected.size() > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Pergunta se deve fazer a pergunta se foi negada anteriormente
                if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage("Para utilizar este APP é preciso conceder essas permissões!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), RESULT_REQUEST_PERMISSION);
                                }
                            })
                            .create()
                            .show();
                }
            }
        }
    }

    //endregion
}