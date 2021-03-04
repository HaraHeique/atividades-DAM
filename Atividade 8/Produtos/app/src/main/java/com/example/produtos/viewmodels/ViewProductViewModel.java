package com.example.produtos.viewmodels;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.produtos.BuildConfig;
import com.example.produtos.models.Product;
import com.example.produtos.utils.HttpRequest;
import com.example.produtos.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ViewProductViewModel extends ViewModel {

    public MutableLiveData<Product> product;
    public String pid;

    public ViewProductViewModel(String pid) {
        this.pid = pid;
    }

    public LiveData<Product> getProduct() {
        // Caso o produto não tenha sido carregado é realizado uma chamada no servidor obtendo as informações do produto
        if (this.product == null) {
            this.product = new MutableLiveData<>();
            loadProduct();
        }

        return this.product;
    }

    private void loadProduct() {
        // Cria a thread paralela diferente da thread principal voltada para interface
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                // Cria a url request do get do produto específico e monta o objeto responsável por realizar o request
                String urlRequest = BuildConfig.API_URL + "/get_product_details.php";
                HttpRequest request = new HttpRequest(urlRequest, "GET", "UTF-8");
                request.addParam("pid", pid);

                try {
                    // Executa a request e obtém a resposta como string
                    InputStream inputStream = request.execute();
                    String response = request.getResponseString(inputStream, "UTF-8");
                    request.finish();

                    // Realizando o parse da resposta para um objeto JSON
                    JSONObject jsonObject = new JSONObject(response);

                    // Checar se a resposta está correta
                    int success = jsonObject.getInt("success");

                    if (success == 1) {
                        // Obtém o array de produtos do json
                        JSONArray jsonArray = jsonObject.getJSONArray("product");

                        // Obtém o produto na primeira posição do array porque só contém um
                        JSONObject jProduct = jsonArray.getJSONObject(0);

                        // Obtém as info do jProduct
                        String name = jProduct.getString("name");
                        String price = jProduct.getString("price");
                        String description = jProduct.getString("description");
                        Bitmap image = Util.base64ToBitmap(jProduct.getString("img"));

                        // Instancia o objeto produto e realiza o postvalue dela para que os observadores sejam acionados
                        Product productView = new Product(pid, name, price, description, image);
                        product.postValue(productView);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Factory da view model de product que serve especificamente para passar parâmetros no construtor da classe
    static public class ViewProductViewModelFactory implements ViewModelProvider.Factory {

        private String pid;

        public ViewProductViewModelFactory(String pid) {
            this.pid = pid;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new ViewProductViewModel(pid);
        }
    }
}
