package com.example.khiata.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.khiata.R;
import com.example.khiata.apis.UserApi;
import com.example.khiata.classes.tela_carrinho;
import com.example.khiata.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_tela_dados_compra_produto#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_tela_dados_compra_produto extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_tela_dados_compra_produto() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_tela_dados_compra_produto.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_tela_dados_compra_produto newInstance(String param1, String param2) {
        fragment_tela_dados_compra_produto fragment = new fragment_tela_dados_compra_produto();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    ImageView voltar_carrinho;
    Button btn_cancelar_confirmacao_dados, btn_confirmar_dados;
    TextView escolher_outro_endereco, nome_usuario, phone_usuario, cpf_usuario, street_endereco, complement_endereco;
    private Retrofit retrofit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tela_dados_compra_produto, container, false);

        //Botão para voltar para tela de carrinho
        voltar_carrinho = view.findViewById(R.id.voltar_carrinho);
        voltar_carrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), tela_carrinho.class);
                startActivity(intent);
            }
        });

        //Botão para cancelar a compra
        btn_cancelar_confirmacao_dados = view.findViewById(R.id.btn_cancelar_confirmacao_dados);
        btn_cancelar_confirmacao_dados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), tela_carrinho.class);
                startActivity(intent);
            }
        });

        //Botão para voltar e escolher outro endereço
        escolher_outro_endereco = view.findViewById(R.id.escolher_outro_endereco);
        escolher_outro_endereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_selecao_endereco_pagamento());
                transaction.commit();
            }
        });

        btn_confirmar_dados = view.findViewById(R.id.btn_confirmar_dados);
        btn_confirmar_dados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_pagamento_produto());
                transaction.commit();
            }
        });

        //Carregar os dados para confirmar o pedido
        nome_usuario = view.findViewById(R.id.nome_usuario);
        phone_usuario = view.findViewById(R.id.phone_usuario);
        cpf_usuario = view.findViewById(R.id.cpf_usuario);
        buscarInformacoesDoUsuario(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        street_endereco = view.findViewById(R.id.street_endereco);
        complement_endereco = view.findViewById(R.id.complement_endereco);

        //Pegando informações do endereço selecionado
        Bundle bundle = getArguments();
        if(bundle != null){
            String street_endereco_txt = bundle.getString("street_endereco");
            String complement_endereco_txt = bundle.getString("complement_endereco");
            String number_endereco_txt = String.valueOf(bundle.getDouble("number_endereco"));
            String cep_endereco_txt = bundle.getString("cep_endereco");
            if (street_endereco_txt != null && complement_endereco_txt != null && number_endereco_txt != null && cep_endereco_txt != null) {
                street_endereco.setText(street_endereco_txt + " - " + number_endereco_txt + " - " + cep_endereco_txt);
                complement_endereco.setText(complement_endereco_txt);
            }
        }

        return view;
    }

    //Método para buscar as informações do perfil
    private void buscarInformacoesDoUsuario(String userEmail) {
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
                    nome_usuario.setText(userResponse.getName());
                    phone_usuario.setText(userResponse.getPhone());
                    cpf_usuario.setText(userResponse.getCpf());
                } else {
                    Toast.makeText(getContext(), "Usuário não encontrado ou resposta inválida", Toast.LENGTH_SHORT).show();
                    Log.e("API Error", "Response code: " + response.code() + " | Error body: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}