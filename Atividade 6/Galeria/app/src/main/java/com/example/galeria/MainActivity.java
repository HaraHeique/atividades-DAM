package com.example.galeria;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RESULT_TAKE_PICTURE = 1;
    private static final int RESULT_REQUEST_PERMISSION = 2;

    String currentPhotoPath;
    List<String> photos = new ArrayList<>();
    MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtém a toolbar da activity e define ela como a toolbar da MainActivity
        Toolbar toolbar = findViewById(R.id.tbMain);
        setSupportActionBar(toolbar);

        // Cria uma lista de string para checar se o usuário tem permissão ou não de acessar determinados recursos
        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.CAMERA);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // Pede permissões ao usuário de acessar os recursos desejados
        checkPermission(permissions);

        // Carregando as fotos no diretório compartilhado Pictures
        loadPhotosInMemory();

        // Chama o adapter da activity main (para preencher/mostrar os itens do recycle view)
        mainAdapter = new MainAdapter(MainActivity.this, photos);

        // Obtém o recycle view da galeria e define o seu adaptador instanciado acima
        RecyclerView rvGallery = findViewById(R.id.rvGallery);
        rvGallery.setAdapter(mainAdapter);

        // Vai construir os itens e vai dispor na lista em forma de grid onde cada linha terá n elementos dado pela função utilitária chamada
        float width = getResources().getDimension(R.dimen.itemWidth);
        int numberOfColumns = Utils.calculateNumberOfColumns(MainActivity.this, width);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, numberOfColumns);
        rvGallery.setLayoutManager(gridLayoutManager);
    }

    // Sobrescrita da função que define que os itens do menu da activity criada (main_toolbar.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        /* Pega o inflador do menu e coloca o elementos do menu da main_toolbar.xml criada como
        filha do menu principal, ou seja, basicamente faz o ícone da foto aparecer no menu */
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_toolbar, menu);

        return true;
    }

    // Sobrescrita da função de quando é selectiona um item da activity
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        // Pega o Id do item que foi clicado/selecionado e realizada uma operação condicional de switch case
        switch (item.getItemId()) {
            case R.id.opCamera:
                dispatchTakePictureIntent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Após ser executado o intent da câmera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Caso o resultado da intenção seja o definido (RESULT_TAKE_PICTURE)
        if (requestCode == RESULT_TAKE_PICTURE) {
            // Salvar a foto na lista, caso o contrário deleta a foto do path
            if (resultCode == RESULT_OK) {
                photos.add(currentPhotoPath);

                // Notifica o adapter que foi inserido um novo item para compor o recycle view
                mainAdapter.notifyItemInserted(photos.size() - 1);
            } else {
                new File(currentPhotoPath).delete();
            }
        }
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
                if (!hasPermission(permission)) {
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

    public void startPhotoActivity(String photoPath) {
        Intent intent = new Intent(MainActivity.this, PhotoActivity.class);
        intent.putExtra("photo_path", photoPath);
        startActivity(intent);
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
            Toast.makeText(MainActivity.this, "Não foi possível criar o arquivo", Toast.LENGTH_LONG).show();
            return;
        }

        currentPhotoPath = file.getAbsolutePath();

        if (file != null) {
            // Pega o URI correspondente ao arquivo criado (imagem no caso) para que outras apps consigam utilizá-la
            Uri fUri = FileProvider.getUriForFile(MainActivity.this, "com.example.galeria.fileprovider", file);

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

    // Função que verifica se a lista de permissões passadas são válidas
    private void checkPermission(List<String> permissions) {
        List<String> permissionNotGranted = new ArrayList<>();

        // Verificar se permissão já é existente ou não
        for (String permission : permissions) {
            if (!hasPermission(permission)) {
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
            return ActivityCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_GRANTED;
        }

        return false;
    }

    // Obtém as fotos do diretório Pictures e as carrega em memória (photos)
    private void loadPhotosInMemory() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] files = dir.listFiles();

        for (int i = 0; i < files.length; i++) {
            photos.add(files[i].getAbsolutePath());
        }
    }
}