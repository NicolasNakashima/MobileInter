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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khiata.R;
import com.example.khiata.adapters.AdapterProdutosCarrinho;
import com.example.khiata.fragments.fragment_tela_selecao_endereco_pagamento;
import com.example.khiata.models.Product;

import java.util.ArrayList;
import java.util.List;

public class tela_carrinho extends AppCompatActivity {

    ImageView voltar_home;
    Button btn_continuar_comprando, btn_finalizar_compra;
    RecyclerView lista_produtos_carrinho;
    List<Product> produtos = new ArrayList();
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

        //Definir a lista de produtos
        lista_produtos_carrinho = findViewById(R.id.lista_produtos_carrinho);
        produtos.add(new Product("Camiseta regata", 23.00, "Imagem", 1, "Joana", 3.2, "Camiseta regata branca, tamanho adulto", "M"));
        AdapterProdutosCarrinho adapterProdutosCarrinho = new AdapterProdutosCarrinho(getApplicationContext(), produtos);
        lista_produtos_carrinho.setAdapter(adapterProdutosCarrinho);
        lista_produtos_carrinho.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
    }
}