package com.example.khiata.classes;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.khiata.R;
import com.example.khiata.fragments.fragment_tela_avaliacoes;
import com.example.khiata.fragments.fragment_tela_cursos;
import com.example.khiata.fragments.fragment_tela_enderecos;
import com.example.khiata.fragments.fragment_tela_home;
import com.example.khiata.fragments.fragment_tela_perfil;
import com.example.khiata.fragments.fragment_tela_compras;
import com.example.khiata.fragments.fragment_tela_favoritos;
import com.example.khiata.fragments.fragment_tela_statistics;
import com.example.khiata.fragments.fragment_tela_area_costureira;
import com.example.khiata.fragments.fragment_tela_premium_plan;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private fragment_tela_favoritos fragment_tela_favoritos = new fragment_tela_favoritos();
    private fragment_tela_home fragment_tela_home = new fragment_tela_home();
    private fragment_tela_compras fragment_tela_compras = new fragment_tela_compras();
    private fragment_tela_perfil fragment_tela_perfil = new fragment_tela_perfil();
    private fragment_tela_cursos fragment_tela_cursos = new fragment_tela_cursos();
    private fragment_tela_statistics fragment_tela_statistics = new fragment_tela_statistics();
    private fragment_tela_area_costureira fragment_tela_area_costureira = new fragment_tela_area_costureira();
    private fragment_tela_enderecos fragment_tela_enderecos = new fragment_tela_enderecos();
    private fragment_tela_avaliacoes fragment_tela_avaliacoes = new fragment_tela_avaliacoes();
    private fragment_tela_premium_plan fragment_tela_premium_plan = new fragment_tela_premium_plan();
    ImageView btn_lateral_menu, btn_navigation_favoritos, btn_navigation_home, btn_navigation_compras, btn_navigation_perfil;
    View navigation_perfil, navigation_cursos, navigation_statistics, navigation_area_costureira, navigation_enderecos, navigation_avaliacoes, btn_logout;
    Button btn_adquirir_premium;

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

                //Ir para tela de perfil
                navigation_perfil = menu_lateral.findViewById(R.id.navigation_perfil);
                navigation_perfil.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_conteudo, fragment_tela_perfil);
                        transaction.commit();
                        dialog.cancel();
                    }
                });

                //Ir para tela de cursos
                navigation_cursos = menu_lateral.findViewById(R.id.navigation_cursos);
                navigation_cursos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_conteudo, fragment_tela_cursos);
                        transaction.commit();
                        dialog.cancel();
                    }
                });

                //Ir para tela de estatísticas
                navigation_statistics = menu_lateral.findViewById(R.id.navigation_statistics);
                navigation_statistics.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_conteudo, fragment_tela_statistics);
                        transaction.commit();
                        dialog.cancel();
                    }
                });

                //Ir para tela da área de costureira
                navigation_area_costureira = menu_lateral.findViewById(R.id.navigation_area_costureira);
                navigation_area_costureira.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_conteudo, fragment_tela_area_costureira);
                        transaction.commit();
                        dialog.cancel();
                    }
                });

                //Ir para tela de endereços
                navigation_enderecos = menu_lateral.findViewById(R.id.navigation_enderecos);
                navigation_enderecos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_conteudo, fragment_tela_enderecos);
                        transaction.commit();
                        dialog.cancel();
                    }
                });

                //Ir para tela de avaliações
                navigation_avaliacoes = menu_lateral.findViewById(R.id.navigation_avaliacoes);
                navigation_avaliacoes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_conteudo, fragment_tela_avaliacoes);
                        transaction.commit();
                        dialog.cancel();
                    }
                });

                //Ir para tela do plano premium
                btn_adquirir_premium = menu_lateral.findViewById(R.id.btn_adquirir_premium);
                btn_adquirir_premium.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_conteudo, fragment_tela_premium_plan);
                        transaction.commit();
                        dialog.cancel();
                    }
                });

                //Logout do App
                btn_logout = menu_lateral.findViewById(R.id.btn_logout);
                btn_logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainActivity.this, tela_inicial.class);
                        startActivity(intent);
                        finish();
                        dialog.cancel();
                    }
                });


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