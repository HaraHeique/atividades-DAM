 package com.example.mylist;

 import android.app.Activity;
 import android.content.Intent;
 import android.net.Uri;
 import android.os.Bundle;
 import android.util.Log;
 import android.view.View;

 import androidx.annotation.Nullable;
 import androidx.appcompat.app.AppCompatActivity;
 import androidx.lifecycle.ViewModelProvider;
 import androidx.recyclerview.widget.DividerItemDecoration;
 import androidx.recyclerview.widget.LinearLayoutManager;
 import androidx.recyclerview.widget.RecyclerView;

 import com.google.android.material.floatingactionbutton.FloatingActionButton;

 import java.util.List;

 public class MainActivity extends AppCompatActivity {
     static int NEW_ITEM_REQUEST = 1;
     MyAdapter myAdapter;
     String LOG_TAG_INFO = "Ciclo de Vida MainActivity";
     MainActivityViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(LOG_TAG_INFO, "Método onCreate() foi chamado");

        /* Usa o método get e a instância do ViewModelProvider porque caso exista o objeto em memória
        ele recupera ele. Caso contrário cria novo (padrão Singleton) */
        vm = new ViewModelProvider(this).get(MainActivityViewModel.class);

        // Constrói a tela dentro de res/layout/activity_main.xml seguindo os conjuntos de regras definidos dentro deste arquivo
        setContentView(R.layout.activity_main);

        // Obtém a instância do botão flutuante de adicionar novo item
        FloatingActionButton fabAddItem = findViewById(R.id.fabAddNewItem);

        // Escuta o evento de clique do botão flutuante obtido
        fabAddItem.setOnClickListener(new View.OnClickListener() {
            // Define a função de callback do evento de clique do botão flutuante
            @Override
            public void onClick(View v) {
                // Instancia a intenção/ação de mudança de tela
                Intent intent = new Intent(MainActivity.this, NewItemActivity.class);

                /* Inicia a activity a partir de um resultado, sendo o resultado uma espécie de função
                 *  função de callback, bem parecido com JS. */
                startActivityForResult(intent, NEW_ITEM_REQUEST);
            }
        });

        // Recupera os itens dentro da view model
        List<MyItem> itens = vm.getItens();

        // Instancia o adaptador passando a lista de itens
        myAdapter = new MyAdapter(this, itens);

        // Obtém a instância do recycle view de itens (lista de itens)
        RecyclerView rvItens = findViewById(R.id.rvItems);

        // Cada item da lista tem o mesmo tamanho aumentando assim a performance
        rvItens.setHasFixedSize(true);

        // Define que a lista será criada de forma linear (lista contínua de elementos, ou seja, igual uma tabela)
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        // Seta o layout manager do recycle view criado igual ao instanciado, no caso o linear
        rvItens.setLayoutManager(layoutManager);

        /* O adapter terá a função de construir o item dentro do recycle view e preencher os itens
        para aparecer, por isso é definido o adapter para o recycle criado */
        rvItens.setAdapter(myAdapter);

        // Adicionar as divisões entre os itens da lista
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvItens.getContext(), DividerItemDecoration.VERTICAL);
        rvItens.addItemDecoration(dividerItemDecoration);
    }

     // Método de callback que é chamada ao finalizar a activity de criar novo item
     @Override
     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);

         // Caso o resultado seja do novo item adicionado e estiver tudo Ok (correto)
         if (requestCode == NEW_ITEM_REQUEST && resultCode == Activity.RESULT_OK) {
             // Obtém os dados proveniente da intenção que foi finalizada na outra activity de adicionar novo item
             Uri selectedPhotoLocation = data.getData();
             String title = data.getStringExtra("title");
             String description = data.getStringExtra("description");

             // Instância o novo item de uma classe anêmica (só serve para armazenar dados)
             MyItem newItem = new MyItem();
             newItem.photo = selectedPhotoLocation;
             newItem.title = title;
             newItem.description = description;
             vm.getItens().add(newItem);

             // Notificar ao adapter que foi inserido um novo item na última posição
             myAdapter.notifyItemInserted(vm.getItens().size() - 1);
         }
     }

     /* Métodos do ciclo de vida das Activities */

     @Override
     protected void onStart() {
         super.onStart();
         Log.i(LOG_TAG_INFO, "Método onStart() foi chamado");
     }

     @Override
     protected void onResume() {
         super.onResume();
         Log.i(LOG_TAG_INFO, "Método onResume() foi chamado");
     }

     @Override
     protected void onRestart() {
         super.onRestart();
         Log.i(LOG_TAG_INFO, "Método onRestart() foi chamado");
     }

     @Override
     protected void onPause() {
         super.onPause();
         Log.i(LOG_TAG_INFO, "Método onPause() foi chamado");
     }

     @Override
     protected void onStop() {
         super.onStop();
         Log.i(LOG_TAG_INFO, "Método onStop() foi chamado");
     }

     @Override
     protected void onDestroy() {
         super.onDestroy();
         Log.i(LOG_TAG_INFO, "Método onDestroy() foi chamado");
     }
 }