package com.example.galeriapublica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int RESULT_REQUEST_PERMISSION = 2;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtenção da View Model de activity main
        MainActivityViewModel vm = new ViewModelProvider(this).get(MainActivityViewModel.class);

        // Obter o botão de navegação
        bottomNavigationView = findViewById(R.id.btNav);

        // Ao clicar em um dos botões da navigation será chamado este evento
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.gridViewOp:
                        // Lógica referente ao fragmento do botão de grid
                        GridViewFragment gridViewFragment = GridViewFragment.newInstance();

                        // Define como o novo fragmento no layout
                        setFragment(gridViewFragment);

                        // Guarda o estado de visualização em grid
                        vm.setNavigationOpSelected(R.id.gridViewOp);

                        break;
                    case R.id.listViewOp:
                        // Lógica referente ao fragmento do botão de lista
                        ListViewFragment listViewFragment = ListViewFragment.newInstance();

                        // Define como o novo fragmento no layout
                        setFragment(listViewFragment);

                        // Guarda o estado de visualização em lista de itens
                        vm.setNavigationOpSelected(R.id.listViewOp);

                        break;
                }

                return true;
            }
        });
    }

    // É feito o override neste ciclo de vida da Activity porque toda vez que a aplicação ir para este estado é recarregado as imagens
    @Override
    protected void onResume() {
        super.onResume();

        // Cria uma lista de string para checar se o usuário tem permissão ou não de acessar determinados recursos
        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        // Pede permissões ao usuário de acessar os recursos desejados
        checkPermission(permissions);
    }

    // Método que define qual dos fragmentos será colocado dentro do layout da main_activity.xml
    void setFragment(Fragment fragment) {
        FragmentTransaction fragTran = getSupportFragmentManager().beginTransaction();

        // Substitui o fragmento passado como argumento para dentro do layout
        fragTran.replace(R.id.fragContainer, fragment);

        // Volta para o momento anterior do fragmento substituído
        fragTran.addToBackStack(null);

        // Commita as alterações realizadas dos fragmentos (bem parecido com banco de dados)
        fragTran.commit();
    }

    void loadImageData() {
        Log.i("Galeria Pública", "Imagens carregadas da galeria pública do celular!");

        // Colunas desejadas da tabela de Imagens
        String[] projection = new String[] {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.SIZE
        };

        String selection = null; // Representa o filtro/predicado da consulta
        String selectionArgs[] = null; // Representa os argumento da query criada
        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " ASC"; // Equivalente ao ORDERBY do sql

        try {
            // Realizando a query efetivamente e colocando dentro de um cursor que deve ser percorrido
            Cursor cursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    sortOrder
            );

            // Obtendo os indices de cada coluna
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            int dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);

            // Obtém os dados das imagens
            MainActivityViewModel vm = new ViewModelProvider(this).get(MainActivityViewModel.class);
            HashMap<Long, ImageData> imageDataList = vm.getImageDataList();
            int widthImg = (int)getResources().getDimension(R.dimen.im_width);
            int heightImg = (int)getResources().getDimension(R.dimen.im_heigth);

            // Loop para pegar as info das imagens através do cursor retornado da consulta realizada
            while (cursor.moveToNext()) {
                // Obtém os dados da consulta realizada
                long id = cursor.getLong(idColumn);

                // Checa se o ID já está contido no HashMap para não ter repetições de imagens
                if (imageDataList.containsKey(id)) continue;

                String name = cursor.getString(nameColumn);
                int dateAdded = cursor.getInt(dateAddedColumn);
                int size = cursor.getInt(sizeColumn);

                // Obtém a imagem em bitmap a partir do id da imagem crindo um URI
                Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                Bitmap thumb = Utils.getBitmap(MainActivity.this, imageUri, widthImg, heightImg);;

                ImageData img = new ImageData(thumb, name, new Date(dateAdded), size);
                imageDataList.put(id, img);
            }

            // Obtendo o estado do item selecionado guardado dentro da view model da MainActivity
            bottomNavigationView.setSelectedItemId(vm.getNavigationOpSelected());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


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

        if (permissionNotGranted.size() > 0) {
            // Se não tem aquela permissão tem que requerí-la
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Pedir ao usuário para conceder permissão de acessar os dados de foto
                requestPermissions(permissionNotGranted.toArray(new String[permissionNotGranted.size()]), RESULT_REQUEST_PERMISSION);
            }
        } else {
            loadImageData();
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

    // Após ser pedido a permissão de acessar a câmera
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        List<String> permissionsRejected = new ArrayList<>();

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
        } else {
            loadImageData();
        }
    }
}