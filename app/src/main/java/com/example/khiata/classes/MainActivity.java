package com.example.khiata.classes;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.khiata.R;
import com.example.khiata.apis.UserApi;
import com.example.khiata.fragments.fragment_tela_avaliacoes;
import com.example.khiata.fragments.fragment_tela_cursos;
import com.example.khiata.fragments.fragment_tela_enderecos;
import com.example.khiata.fragments.fragment_tela_home;
import com.example.khiata.fragments.fragment_tela_perfil;
import com.example.khiata.fragments.fragment_tela_compras;
import com.example.khiata.fragments.fragment_tela_favoritos;
import com.example.khiata.fragments.fragment_tela_plan_premium;
import com.example.khiata.fragments.fragment_tela_produto;
import com.example.khiata.fragments.fragment_tela_selecao_endereco_pagamento;
import com.example.khiata.fragments.fragment_tela_statistics;
import com.example.khiata.fragments.fragment_tela_cadastrar_produto;
import com.example.khiata.fragments.fragment_tela_area_costureira;
import com.example.khiata.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    private fragment_tela_plan_premium fragment_tela_plan_premium = new fragment_tela_plan_premium();
    private fragment_tela_cadastrar_produto fragment_tela_cadastrar_produto = new fragment_tela_cadastrar_produto();
    private fragment_tela_produto fragment_tela_produto = new fragment_tela_produto();
    ImageButton btn_lateral_menu, btn_navigation_favoritos, btn_navigation_home, btn_navigation_compras, btn_navigation_perfil;
    ImageView foto_perfil;
    View navigation_perfil, navigation_cursos, navigation_statistics, navigation_area_costureira, navigation_enderecos, navigation_avaliacoes, btn_logout;
    Button btn_adquirir_premium;
    TextView nome_user;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    private Retrofit retrofit;

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

        Intent intent = getIntent();
        if (intent != null) {
            String fragmentName = intent.getStringExtra("fragment");
            Log.d("TAG", "FragmentName: " + fragmentName);
            String imgProductName = intent.getStringExtra("imgName");
            Log.d("TAG", "ImgName: " + imgProductName);
            String titulo_produto = intent.getStringExtra("titulo_produto");
            String vendedor_produto = intent.getStringExtra("vendedor_produto");
            double preco_produto = intent.getDoubleExtra("preco_produto", 0.0);
            String imagem_produto = intent.getStringExtra("imagem_produto");
            String descricao_produto = intent.getStringExtra("descricao_produto");
            String tamanho_produto = intent.getStringExtra("tamanho_produto");
            float avaliacao_produto = intent.getFloatExtra("avaliacao_produto", 0.0f);
            if ("cadastrar_produto".equals(fragmentName) && imgProductName != null) {
                // Carregar o fragmento de cadastrar produto
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("imgName", imgProductName);
                fragment_tela_cadastrar_produto.setArguments(bundle);
                transaction.replace(R.id.frame_conteudo, fragment_tela_cadastrar_produto);
                transaction.commit();
            }
            if ("tela_produto".equals(fragmentName) && titulo_produto != null && vendedor_produto != null && preco_produto != 0.0 && imagem_produto != null && descricao_produto != null) {
                // Carregar o fragmento da tela do produto
                fragment_tela_produto telaProdutoFragment = new fragment_tela_produto();

                Bundle bundle = new Bundle();
                bundle.putString("titulo_produto", titulo_produto);
                bundle.putString("vendedor_produto", vendedor_produto);
                bundle.putDouble("preco_produto", preco_produto);
                bundle.putString("imagem_produto", imagem_produto);
                bundle.putString("descricao_produto", descricao_produto);
                bundle.putString("tamanho_produto", tamanho_produto);
                bundle.putFloat("avaliacao_produto", avaliacao_produto);
                telaProdutoFragment.setArguments(bundle);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, telaProdutoFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            if("selecao_endereco_pagamento".equals(fragmentName)){
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_selecao_endereco_pagamento());
                transaction.addToBackStack(null);
                transaction.commit();
            }
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
                String userEmail = auth.getCurrentUser().getEmail();

                Dialog dialog = new Dialog(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View menu_lateral = inflater.inflate(R.layout.menu_lateral, null);

                //Carregar imagem do perfil no menu lateral
                foto_perfil= menu_lateral.findViewById(R.id.foto_perfil);
                StorageReference profileRef = storageRef.child("khiata_perfis/foto_"+userEmail+".jpg");
                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getApplicationContext()).load(uri).circleCrop().into(foto_perfil);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getApplicationContext(), "Falha ao obter URL da imagem"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "Falha ao obter URL da imagem"+ e.getMessage());
                        foto_perfil.setImageResource(R.drawable.empty_img);
                    }
                });

                //Carregar nome do usuário no menu lateral
                nome_user = menu_lateral.findViewById(R.id.nome_user);
                buscarNomeDoUsuario(userEmail);

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
                        usuarioCostureira(userEmail);
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
                        transaction.replace(R.id.frame_conteudo, fragment_tela_plan_premium);
                        transaction.commit();
                        dialog.cancel();
                    }
                });

                //Logout do App
                btn_logout = menu_lateral.findViewById(R.id.btn_logout);
                btn_logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dialog = new Dialog(MainActivity.this);
                        LayoutInflater inflater = getLayoutInflater();
                        View popup_opcao = inflater.inflate(R.layout.popup_opcao, null);

                        TextView msgPopup = popup_opcao.findViewById(R.id.msg_popup);
                        msgPopup.setText("Você está prestes a realizar o logout.\n Deseja prosseguir?");
                        ImageView imgPopup = popup_opcao.findViewById(R.id.img_popup);
                        imgPopup.setImageResource(R.drawable.icon_pop_logout);
                        Button btn_seguir = popup_opcao.findViewById(R.id.btn_seguir);
                        btn_seguir.setText("Sair");
                        btn_seguir.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(MainActivity.this, tela_inicial.class);
                                startActivity(intent);
                                finish();
                                dialog.cancel();
                            }
                        });
                        Button btn_cancelar = popup_opcao.findViewById(R.id.btn_cancelar);
                        btn_cancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });

                        dialog.setContentView(popup_opcao);
                        dialog.setCancelable(true);
                        dialog.show();
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

    //Pega o nome do usuário para o menu lateral
    private void buscarNomeDoUsuario(String userEmail) {
        String API_BASE_URL = "https://apikhiata.onrender.com/";
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi userApi = retrofit.create(UserApi.class);
        Call<User> call = userApi.buscarUsuarioPorEmail(userEmail);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User userResponse = response.body();
                nome_user.setText(userResponse.getName());
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Verifica se o usuário é costureira
    private void usuarioCostureira(String userEmail) {
        String API_BASE_URL = "https://apikhiata.onrender.com/";
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi userApi = retrofit.create(UserApi.class);
        Call<User> call = userApi.buscarUsuarioPorEmail(userEmail);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User userResponse = response.body();
                if (userResponse.isDressmaker()) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_conteudo, fragment_tela_area_costureira);
                    transaction.commit();
                }
                else{
                    Dialog dialog = new Dialog(MainActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Está área somente pode ser acessada por costureiras.");
                    ImageView imgPopup = popupView.findViewById(R.id.img_popup);
                    imgPopup.setImageResource(R.drawable.icon_pop_alert);
                    Button btnPopup = popupView.findViewById(R.id.btn_popup);
                    btnPopup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });

                    dialog.setContentView(popupView);
                    dialog.setCancelable(true);
                    dialog.show();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}