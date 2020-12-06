package com.example.firstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class NextActivity extends AppCompatActivity {

    /* É chamada quando o android iniciar a NextActivity. É um comportamento bem parecido com os
    *  Life Cycles do Angular, o qual se assemelha ao life cycle hook OnInit do Angular */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        // Recebe o intent proveniente do MainActivity
        Intent intent = getIntent();

        // Recupera o valor dentro do dicionário dos dados armazenados do intent do MainActivity
        String message = intent.getStringExtra("text");

        // Obtém a instância do elemento da view pelo seu ID
        TextView tvMessage = findViewById(R.id.tvMessage);

        // Pegar a mensagem e exibir ela na view do NextActivity
        tvMessage.setText(message);
    }
}