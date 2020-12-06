package com.example.mylist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class NewItemActivity extends AppCompatActivity {

    static int PHOTO_PICKER_REQUEST = 1;
    Uri selectPhotoLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        // Obtém a instância do botão de escolha da imagem no aparelho
        ImageButton imbChooseImage = findViewById(R.id.imbChooseImage);

        // Escuta o evento de clique do botão de escolher a imagem
        imbChooseImage.setOnClickListener(new View.OnClickListener() {
            // Define a função de callback de clique do botão de escolher a imagem
            @Override
            public void onClick(View v) {
                /* Criando a intenção/ação de abrir o documento/imagem a partir de um candidato (por
                * ser uma intenção implícita) */
                Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

                // Define o tipo de imagens que serão buscadas ao selecionar as imagens (no caso todos os tipos)
                photoPickerIntent.setType("image/*");

                /* Inicia a activity a partir de um resultado, sendo o resultado uma espécie de função
                *  função de callback, bem parecido com JS */
                startActivityForResult(photoPickerIntent, PHOTO_PICKER_REQUEST);
            }
        });

        // Obtém a instância do botão de adicionar o item
        Button btnAddNewItem = findViewById(R.id.btnAddItem);

        // Escuta o evento de clique do botão de adicionar novo item
        btnAddNewItem.setOnClickListener(new View.OnClickListener() {
            // Define a função de callback de clique do botão de adicionar novo item
            @Override
            public void onClick(View v) {
                // Caso usuário não selecione nenhuma foto mostra uma mensagem de erro para o usuário
                if (selectPhotoLocation == null) {
                    Toast.makeText(
                        NewItemActivity.this,
                        "Você precisa selecionar uma imagem",
                        Toast.LENGTH_LONG
                    ).show();

                    return;
                }

                // Obtém a instância do campo de título do novo item e pega seu conteúdo
                EditText etTitle = findViewById(R.id.etTitle);
                String title = etTitle.getText().toString();

                if (title.isEmpty()) {
                    Toast.makeText(
                            NewItemActivity.this,
                            "Você precisa selecionar definir um título",
                            Toast.LENGTH_LONG
                    ).show();

                    return;
                }

                // Obtém a instância do campo de descrição do novo item e pega seu conteúdo
                EditText etDescription = findViewById(R.id.etDescription);
                String description = etDescription.getText().toString();

                if (description.isEmpty()) {
                    Toast.makeText(
                            NewItemActivity.this,
                            "Você precisa selecionar definir uma descrição",
                            Toast.LENGTH_LONG
                    ).show();

                    return;
                }

                // Cria uma intenção e coloca os metadados dos dados preenchidos pelo usuário ao adicionar novo item
                Intent intent = new Intent();
                intent.setData(selectPhotoLocation);
                intent.putExtra("title", title);
                intent.putExtra("description", description);

                // Define o resultado como Ok determinando que foi finalizada a intenção
                setResult(Activity.RESULT_OK, intent);

                // Finaliza a activity e retorna para quem a iniciou
                finish();
            }
        });
    }

    // Função/método que é chamada após a intenção/ação de selecionar a imagem ser resolvida
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Caso a request de trazer a foto seja de trazer a foto e o seu result seja Ok, ou seja, pegou uma foto
        if (requestCode == PHOTO_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {
            // Pegando o caminho/path da imagem
            selectPhotoLocation = data.getData();

            // Obtém a instância do elemento que é responśavel por mostrar a imagem
            ImageView imvPhotoPreview = findViewById(R.id.imvPhotoPreview);

            // Seta o path da imagem no elemento que mostra a imagem
            imvPhotoPreview.setImageURI(selectPhotoLocation);
        }
    }

    private boolean ValidateImagePicked() {
        return true;
    }
}