package com.example.khiata.classes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.khiata.R;

public class tela_inicial extends AppCompatActivity {

    Button btn_login_inicio, btn_cadastrar_inicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_inicial);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Botão para ir para tela de login
        btn_login_inicio = findViewById(R.id.btn_login_inicio);
        btn_login_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tela_inicial.this, tela_login.class);
                startActivity(intent);
            }
        });

        //Botão para ir para tela de cadastro
        btn_cadastrar_inicio = findViewById(R.id.btn_cadastrar_inicio);
        btn_cadastrar_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tela_inicial.this, tela_cadastro.class);
                startActivity(intent);
            }
        });
    }
}