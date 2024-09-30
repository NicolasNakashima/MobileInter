package com.example.khiata.classes;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {

    private fragment_tela_home fragment_tela_home = new fragment_tela_home();
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
                dialog.show();
            }
        });

    }
}