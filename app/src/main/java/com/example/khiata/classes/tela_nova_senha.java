package com.example.khiata.classes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.khiata.R;

public class tela_nova_senha extends AppCompatActivity {

    ImageView voltar_login;
    Button btn_atualizar_senha, btn_cancelar_atualizar_senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_nova_senha);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        voltar_login = findViewById(R.id.voltar_login);
        voltar_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tela_nova_senha.this, tela_login.class);
                startActivity(intent);
            }
        });

        btn_cancelar_atualizar_senha = findViewById(R.id.btn_cancelar_atualizar_senha);
        btn_cancelar_atualizar_senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tela_nova_senha.this, tela_login.class);
                startActivity(intent);
            }
        });
    }
}