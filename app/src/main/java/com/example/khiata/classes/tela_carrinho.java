package com.example.khiata.classes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khiata.R;
import com.example.khiata.adapters.AdapterProdutosCarrinho;
import com.example.khiata.apis.CartApi;
import com.example.khiata.apis.UserApi;
import com.example.khiata.fragments.fragment_tela_selecao_endereco_pagamento;
import com.example.khiata.models.Cart;
import com.example.khiata.models.CartItem;
import com.example.khiata.models.Product;
import com.example.khiata.models.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class tela_carrinho extends AppCompatActivity {

    ImageView voltar_home;
    Button btn_continuar_comprando, btn_finalizar_compra;
    RecyclerView lista_produtos_carrinho;
    List<Product> produtos = new ArrayList();
    private Retrofit retrofit;
    String userCpf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_carrinho);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Botão para voltar para a tela home
        voltar_home = findViewById(R.id.voltar_home);
        voltar_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        //Botão para continuar comprando
        btn_continuar_comprando = findViewById(R.id.btn_continuar_comprando);
        btn_continuar_comprando.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        //Botão para seguir para o pagamento
        btn_finalizar_compra = findViewById(R.id.btn_finalizar_compra);
        btn_finalizar_compra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("fragment", "selecao_endereco_pagamento");
                startActivity(intent);
            }
        });

        buscarCPFDoUsuario(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        //Definir a lista de produtos
        lista_produtos_carrinho = findViewById(R.id.lista_produtos_carrinho);
        AdapterProdutosCarrinho adapterProdutosCarrinho = new AdapterProdutosCarrinho(getApplicationContext(), produtos);
        lista_produtos_carrinho.setAdapter(adapterProdutosCarrinho);
        lista_produtos_carrinho.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    // Método para buscar os itens do carrinho do usuário
    private void pegarItensDoCarrinho(String userCpf) {
        Log.e("userCpf", userCpf);
        String API_BASE_URL = "https://api-khiata.onrender.com/";
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CartApi cartApi = retrofit.create(CartApi.class);
        Call<List<List<String>>> call = cartApi.getCartItens(userCpf);

        call.enqueue(new Callback<List<List<String>>>() {
            @Override
            public void onResponse(Call<List<List<String>>> call, Response<List<List<String>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<List<String>> responseBody = response.body();

                    // Logando a resposta completa da API
                    Log.d("API Response", responseBody.toString());

                    List<CartItem> items = new ArrayList<>();
                    String cartId = "";
                    String total = "";
                    List<String> itemNames = new ArrayList<>();

                    // Processa a resposta da API e cria o objeto Cart
                    for (List<String> item : responseBody) {
                        if (item.size() == 2) {
                            String key = item.get(0);
                            String value = item.get(1);

                            if (key.equals("id")) {
                                cartId = value;
                            } else if (key.equals("Total")) {
                                total = value;
                            } else {
                                items.add(new CartItem(key, value));
                                itemNames.add(key); // Adiciona apenas o nome do item à lista
                            }
                        }
                    }

                    // Cria o objeto Cart e exibe os itens no Adapter
                    Cart cart = new Cart(items, cartId, total);

                    // Logando as variáveis separadas
                    Log.d("Cart ID", cartId);
                    Log.d("Total Value", total);
                    Log.d("Item Names", itemNames.toString());

                    if (!items.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Itens do carrinho encontrados.", Toast.LENGTH_SHORT).show();
                        Log.d("Cart", cart.toString());
//                    AdapterItensCarrinho adapter = new AdapterItensCarrinho(getApplicationContext(), items);
//                    lista_itens_carrinho.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplicationContext(), "Nenhum item no carrinho encontrado.", Toast.LENGTH_SHORT).show();
                        Log.e("Error", "Nenhum item no carrinho encontrado.");
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Falha ao carregar o carrinho", Toast.LENGTH_SHORT).show();
                    Log.e("Error", "Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<List<String>>> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Error", throwable.getMessage());
            }
        });
    }

    //Método para buscar o CPF do usário
    private void buscarCPFDoUsuario(String userEmail) {
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
                if (response.isSuccessful() && response.body() != null) {
                    User userResponse = response.body();
                    userCpf= userResponse.getCpf();
                    pegarItensDoCarrinho(userCpf);
                } else {
                    Toast.makeText(getApplicationContext(), "Usuário não encontrado ou resposta inválida", Toast.LENGTH_SHORT).show();
                    Log.e("API Error", "Response code: " + response.code() + " | Error body: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}