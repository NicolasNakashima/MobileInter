package com.example.khiata.classes;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import android.Manifest;
import com.example.khiata.R;
import com.example.khiata.fragments.fragment_tela_home;

public class tela_admin extends AppCompatActivity {

    ImageView voltar_inicio;
    WebView webViewAreaOculta;
    private static final int REQUEST_BLUETOOTH_CONNECT_PERMISSION = 1;
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


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                        REQUEST_BLUETOOTH_CONNECT_PERMISSION);
            }
        }

        //Carregando a Área Oculta no WebView
        webViewAreaOculta = findViewById(R.id.webViewAreaOculta);
        webViewAreaOculta.setWebViewClient(new WebViewClient());
        webViewAreaOculta.getSettings().setJavaScriptEnabled(true);
        webViewAreaOculta.getSettings().setDomStorageEnabled(true);
        webViewAreaOculta.setWebViewClient(new WebViewClient());
        WebView.setWebContentsDebuggingEnabled(true);
        Log.d("Site", "admin");
        webViewAreaOculta.loadUrl("http://ec2-54-161-187-70.compute-1.amazonaws.com:3000/login");
    }
}