package com.example.firstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    /* Automaticamente o android chama esta função que é sobreescrita, onde nela contém o método
    *  setContentView responsável por definir a Activity (view/tela) deste controlador*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Criação dos elementos de interface da view correspondente a este controlador
        setContentView(R.layout.activity_main);

        // Obtém a instância do elemento botão da interface pelo seu ID (parecido com o document.getElementById({id}) do js)
        Button btnSend = findViewById(R.id.btnSend);

        // Realizando o binding do evento de click do botão de envio (parecido com o elem.addEventListener({event}, func()) do js)
        btnSend.setOnClickListener(new View.OnClickListener() {

            // Ao clicar o botão executará esta função de onClick
            @Override
            public void onClick(View v) {
                // Obtém a instância do elemento de texto da interface
                EditText etMessage = findViewById(R.id.etMessage);

                // Obtém o conteúdo do texto digitado no campo de texto
                String message = etMessage.getText().toString();

                /* Basicamente instancia uma intenção de executar algo. Neste caso seria de ir de uma
                interface/activity para outra activity. Parecido com o window.location.href = {link} do js
                * */
                Intent intent = new Intent(MainActivity.this, NextActivity.class);

                /* Contém um tipo de dicionário contendo as informações desejadas ao ser realizada
                a ação da intenção. Como se fosse metadados que usamos no window.localstorage do js,
                 porém específico para a intenção instanciada pelo programador */
                intent.putExtra("text", message);

                /* Executar a intenção. Onde no nosso caso pela a instância que fizemos é fechar a
                * view da MainActivity e iniciar/abrir a NextActivity. */
                startActivity(intent);
            }
        });
    }
}