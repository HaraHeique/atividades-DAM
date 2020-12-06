package com.example.appemail;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtém a instância do botão de enviar o email pelo seu ID
        Button btnEnviar = findViewById(R.id.btnEnviar);

        // Define a escuta do evento de click do botão de enviar email
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View v) {
                // Obtém a instância do campo de email por seu ID e extrai o texto proveniente
                EditText etEmail = findViewById(R.id.etEmail);
                String email = etEmail.getText().toString();

                // Obtém a instância do campo de assunto e extrai o texto proveniente
                EditText etAssunto = findViewById(R.id.etAssunto);
                String assunto = etAssunto.getText().toString();

                // Obtém a instância do campo de descrição e extrai o texto proveniente
                EditText etMensagem = findViewById(R.id.etMensagem);
                String mensagem = etMensagem.getText().toString();

                // Intent explícito (quando sei exatamente para onde quero ir)
                //Intent i = new Intent(MainActivity.this, NextActivity.class);

                // Intent implícito (ação implícita/abstrata)
                Intent intent = new Intent(Intent.ACTION_SENDTO);

                // Define o metadado que algumas das aplicações que respondem o ACTION_SENDTO podem responder (são candidatos)
                intent.setData(Uri.parse("mailto:"));

                // Define os dados que serão enviados pela aplicação candidata do celular android
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
                intent.putExtra(Intent.EXTRA_SUBJECT, assunto);
                intent.putExtra(Intent.EXTRA_TEXT, mensagem);

                // Usando o recurso de try catch em casos de erros inesperados de tempo de execução na aplicação
                try {
                    // Executa a intenção/ação instanciada e populada com os dados/metadados
                    startActivity(intent);
                }
                catch (ActivityNotFoundException e) {
                    // Caso não ache nenhuma aplicação para envio de email mostra um toast com o erro
                    Toast.makeText(
                        MainActivity.this,
                        "Não há nenhuma app de email instalada",
                        Toast.LENGTH_LONG
                    );
                }
            }
        });
    }
}