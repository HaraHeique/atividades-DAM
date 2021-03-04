package com.example.produtos.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.produtos.BuildConfig;
import com.example.produtos.models.Product;
import com.example.produtos.utils.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainViewModel extends ViewModel {

    // Wrapper com a classe de livedata que é uma espécie de estrutura que quando há mudanças ele é alterado
    MutableLiveData<List<Product>> products;

    public LiveData<List<Product>> getProducts() {
        if (this.products == null) {
            this.products = new MutableLiveData<>();
            loadProducts();
        }

        return this.products;
    }

    public void refreshProducts() {
        loadProducts();
    }

    private void loadProducts() {
        // Cria a thread paralela diferente da thread principal voltada para interface
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                // Lista de produtos para colocar os dados proveniente da request
                List<Product> productList = new ArrayList<>();

                // Cria a url request do get de produtos e monta o objeto responsável por realizar o request
                String urlRequest = BuildConfig.API_URL + "/get_all_products.php";
                HttpRequest request = new HttpRequest(urlRequest, "GET", "UTF-8");

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
                        JSONArray jsonArray = jsonObject.getJSONArray("products");

                        // Para cada item do array pega as suas info e coloca na lista de produtos
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jProduct = jsonArray.getJSONObject(i);

                            String pid = jProduct.getString("pid");
                            String name = jProduct.getString("name");

                            Product product = new Product(pid, name);
                            productList.add(product);
                        }

                        // Seta a lista de produtos nova para o product livedata disparando aos seus observadores o evento de observação da mudança da lista
                        products.postValue(productList);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
