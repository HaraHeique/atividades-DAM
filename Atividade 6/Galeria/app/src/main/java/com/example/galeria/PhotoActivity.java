package com.example.galeria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;

public class PhotoActivity extends AppCompatActivity {

    String photoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        // Obtém o toolbar da activity e define ela como sendo da PhotoActivity
        Toolbar toolbar = findViewById(R.id.tbPhoto);
        setSupportActionBar(toolbar);

        // Colocar o botão de voltar (seta <-)
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Obtém a intenção e pega o path da foto proveniente do seus metadados
        Intent intent = getIntent();
        photoPath = intent.getStringExtra("photo_path");

        // Obtém o bitmap e seta ele dentro do image view da activity_photo.xml
        Bitmap bitmap = Utils.getBitmap(photoPath);
        ImageView imgPhoto = findViewById(R.id.imgPhoto);
        imgPhoto.setImageBitmap(bitmap);
    }

    // Sobrescrita da função que define que os itens do menu da activity criada (photo_toolbar.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        /* Pega o inflador do menu e coloca o elementos do menu da photo_toolbar.xml criada como
        filha do menu principal, ou seja, basicamente faz o ícone de compartilhar aparecer no menu */
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.photo_toolbar, menu);

        return true;
    }

    // Sobrescrita da função de quando é selectiona um item da activity
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        // Pega o Id do item que foi clicado/selecionado e realizada uma operação condicional de switch case
        switch (item.getItemId()) {
            case R.id.opShare:
                // Código de compartilhamento da foto
                Intent intent = new Intent(Intent.ACTION_SEND);

                // Cria uma URI para as aplicações externas consumirem a foto
                File file = new File(photoPath);
                Uri photoUri = FileProvider.getUriForFile(PhotoActivity.this, "com.example.galeria.fileprovider", file);

                // Adiciona a foto nos metadados da intenção e inicia a activity
                intent.putExtra(Intent.EXTRA_STREAM, photoUri);
                intent.setType("image/jpeg");
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}