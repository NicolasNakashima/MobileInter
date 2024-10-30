package com.example.khiata.classes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.khiata.R;

public class SplashScreen extends AppCompatActivity {

    // Duração da Splash Screen em milissegundos (3 segundos)
    private static final int SPLASH_DURATION = 3000;

    ImageView splash_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        splash_logo = findViewById(R.id.splash_logo);
//        Glide.with(this)
//                .asGif().load(R.drawable.splash_screen).centerCrop()
//                .into(splash_logo);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                abrirTela();
            }
        }, 4000);
    }

    private void abrirTela(){
        Intent intent = new Intent(SplashScreen.this, tela_inicial.class);
        startActivity(intent);
        // Aplique as animações de transição
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        // Finalize a SplashScreen
        finish();
    }
}