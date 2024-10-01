package com.example.khiata.classes;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.khiata.R;
import com.example.khiata.fragments.fragment_tela_home;
import com.example.khiata.fragments.fragment_tela_perfil;
import com.example.khiata.fragments.fragment_tela_compras;
import com.example.khiata.fragments.fragment_tela_favoritos;

public class MainActivity extends AppCompatActivity {

    private fragment_tela_favoritos fragment_tela_favoritos = new fragment_tela_favoritos();
    private fragment_tela_home fragment_tela_home = new fragment_tela_home();
    private fragment_tela_compras fragment_tela_compras = new fragment_tela_compras();
    private fragment_tela_perfil fragment_tela_perfil = new fragment_tela_perfil();
    ImageView btn_lateral_menu, btn_navigation_favoritos, btn_navigation_home, btn_navigation_compras, btn_navigation_perfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_conteudo, fragment_tela_home);
            transaction.commit();
        }

        //Ir para tela de perfil
        btn_navigation_perfil = findViewById(R.id.btn_navigation_perfil);
        btn_navigation_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, fragment_tela_perfil);
                transaction.commit();
            }
        });

        //Ir para tela de minhas compras
        btn_navigation_compras = findViewById(R.id.btn_navigation_compras);
        btn_navigation_compras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, fragment_tela_compras);
                transaction.commit();
            }
        });

        //Ir para tela home
        btn_navigation_home = findViewById(R.id.btn_navigation_home);
        btn_navigation_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, fragment_tela_home);
                transaction.commit();
            }
        });

        //Ir para a tela de favoritos
        btn_navigation_favoritos = findViewById(R.id.btn_navigation_favoritos);
        btn_navigation_favoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, fragment_tela_favoritos);
                transaction.commit();
            }
        });

        //Abrir menu lateral
        btn_lateral_menu = findViewById(R.id.btn_lateral_menu);
        btn_lateral_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View menu_lateral = inflater.inflate(R.layout.menu_lateral, null);
                dialog.setContentView(menu_lateral);
                dialog.setCancelable(true);

                // Pega a janela do Dialog para ajustar o posicionamento
                Window window = dialog.getWindow();
                if (window != null) {
                    window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.MATCH_PARENT); // Ajuste para a largura desejada
                    window.setGravity(Gravity.START); // Alinhar o menu à esquerda
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // Remove espaços ao redor
                }

                dialog.show();
            }
        });

    }
}