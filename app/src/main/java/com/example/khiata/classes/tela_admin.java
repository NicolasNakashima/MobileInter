package com.example.khiata.classes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.khiata.R;
import com.example.khiata.fragments.fragment_tela_home;

public class tela_admin extends AppCompatActivity {

    ImageView voltar_inicio;
    WebView webViewAreaOculta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Botão para voltar para a tela inicial
        voltar_inicio = findViewById(R.id.voltar_inicio);
        voltar_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), tela_inicial.class);
                startActivity(intent);
                finish();
            }
        });

        //Carregando a Área Oculta no WebView
        webViewAreaOculta = findViewById(R.id.webViewAreaOculta);
        webViewAreaOculta.loadUrl("http://ec2-54-161-187-70.compute-1.amazonaws.com:3000/login");
    }
}