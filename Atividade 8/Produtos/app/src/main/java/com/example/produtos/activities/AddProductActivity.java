package com.example.produtos.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.produtos.BuildConfig;
import com.example.produtos.R;
import com.example.produtos.models.Product;
import com.example.produtos.utils.HttpRequest;
import com.example.produtos.utils.Util;
import com.example.produtos.viewmodels.AddProductViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddProductActivity extends AppCompatActivity {

    private static final int RESULT_TAKE_PICTURE = 1;

    private Context context = AddProductActivity.this;
    private AddProductViewModel addProductViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        setupAddProductViewModel();
        onClickBtnRegisterProduct();
        onClickTakePhoto();
    }

    private void setupAddProductViewModel() {
        // Instanciando a view model como singleton
        addProductViewModel = new ViewModelProvider(this).get(AddProductViewModel.class);

        // Setando a foto no layout caso tenha a foto (string não vazia)
        if (!addProductViewModel.currentPhotoPath.isEmpty()) {
            // Obtém o image view e depois cria a imagem em bitmap e seta no imageview recuperado
            ImageView imageView = findViewById(R.id.imv_product_photo);
            Bitmap imgProduct = Util.getBitmap(addProductViewModel.currentPhotoPath, imageView.getWidth(), imageView.getHeight());
            imageView.setImageBitmap(imgProduct);
        }
    }

    private void onClickBtnRegisterProduct() {
        Button btnRegisterProduct = findViewById(R.id.btn_add_product);
        btnRegisterProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((EditText)findViewById(R.id.et_product_name)).getText().toString();
                String price = ((EditText)findViewById(R.id.et_product_price)).getText().toString();
                String description = ((EditText)findViewById(R.id.et_product_description)).getText().toString();
                String imagePath = addProductViewModel.currentPhotoPath;

                Product product = new Product(name, price, description, Util.getBitmap(imagePath));

                if (!validateRegisterProduct(product)) return;

                // Cria a thread paralela diferente da thread principal voltada para interface
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        // Cria a url request do get do produto específico e monta o objeto responsável por realizar o request
                        String urlRequest = BuildConfig.API_URL + "/create_product.php";
                        HttpRequest request = new HttpRequest(urlRequest, "POST", "UTF-8");
                        request.addParam("name", product.name);
                        request.addParam("price", product.price);
                        request.addParam("description", product.description);
                        request.addFile("img", new File(imagePath));

                        try {
                            // Executa a request e obtém a resposta como string
                            InputStream inputStream = request.execute();
                            String response = request.getResponseString(inputStream, "UTF-8");
                            request.finish();

                            // Realizando o parse da resposta para um objeto JSON
                            JSONObject jsonObject = new JSONObject(response);

                            // Checar se a resposta está correta
                            int success = jsonObject.getInt("success");

                            // Voltar para a thread de UI (principal no caso)
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (success == 1) {
                                        // Mostra sucesso no toast e vai para a activity que o gerou/chamou
                                        Toast.makeText(context, "Produto cadastrado com sucesso", Toast.LENGTH_LONG).show();
                                        setResult(RESULT_OK);
                                        finish();
                                    } else {
                                        Toast.makeText(context, "Produto não foi cadastrado com sucesso", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void onClickTakePhoto() {
        ImageView imvPhoto = findViewById(R.id.imv_product_photo);
        imvPhoto.setOnClickListener(v -> dispatchTakePictureIntent());
    }

    private boolean validateRegisterProduct(Product product) {
        if (product == null) {
            Toast.makeText(context, "O Produto a ser registrado é inválido.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (product.name.isEmpty()) {
            Toast.makeText(context, "O campo Nome do Produto não foi preenchido.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (product.price.isEmpty()) {
            Toast.makeText(context, "O campo Preço não foi preenchido.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (product.description.isEmpty()) {
            Toast.makeText(context, "O campo Descrição não foi preenchido.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (product.photo == null) {
            Toast.makeText(context, "O campo de Foto não foi preenchido.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    // Função que fará a ação de tirar a foto
    private void dispatchTakePictureIntent() {
        // Intent para disparar a câmera
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = null;

        try {
            file = createImageFile();
        } catch (IOException e) {
            // Mostra um toast caso tenha falha no processo de tirar foto
            Toast.makeText(context, "Não foi possível criar o arquivo", Toast.LENGTH_LONG).show();
            return;
        }

        addProductViewModel.currentPhotoPath = file.getAbsolutePath();

        if (file != null) {
            // Pega o URI correspondente ao arquivo criado (imagem no caso) para que outras apps consigam utilizá-la
            Uri fUri = FileProvider.getUriForFile(context, "com.example.produtos.fileprovider", file);

            // Passa o endereço do URI para a câmera por meio de um intent e depois disparar o intent
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fUri);
            startActivityForResult(intent, RESULT_TAKE_PICTURE);
        }
    }

    private File createImageFile() throws IOException {
        // Obtem a data atual que foi tirada a foto e colocá-la no formato definido (yyyyMMdd_HHmmss) e define o nome da imagem
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFilename = "JPEG_" + timeStamp;

        // Salva as imagens dentro da aplicação no diretório pictures,
        // porém de forma compartilhável para que outras aplicações também a enxergue
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Cria de fato o arquivo de forma temporária e o retorna na função
        File file = File.createTempFile(imageFilename, ".jpg", storageDir);

        return file;
    }

    // Após ser executado o intent da câmera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Caso o resultado da intenção seja o definido (RESULT_TAKE_PICTURE)
        if (requestCode == RESULT_TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                // Mostrar imagem no imageview após tirar a câmera
                ImageView imvFoto = findViewById(R.id.imv_product_photo);
                Bitmap bitmap = Util.getBitmap(addProductViewModel.currentPhotoPath, imvFoto.getWidth(), imvFoto.getHeight());
                imvFoto.setImageBitmap(bitmap);
            } else {
                new File(addProductViewModel.currentPhotoPath).delete();
            }
        }
    }
}