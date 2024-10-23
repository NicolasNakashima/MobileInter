package com.example.khiata.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khiata.R;
import com.example.khiata.apis.UserApi;
import com.example.khiata.models.User;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_tela_dados_compra_premium#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_tela_dados_compra_premium extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_tela_dados_compra_premium() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_dados_compra_premium.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_tela_dados_compra_premium newInstance(String param1, String param2) {
        fragment_tela_dados_compra_premium fragment = new fragment_tela_dados_compra_premium();
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

    ImageView voltar_plan_premium;
    Button btn_confirmar_dados, btn_cancelar_confirmacao_dados;
    TextView nome_usuario, phone_usuario, cpf_usuario;
    private Retrofit retrofit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tela_dados_compra_premium, container, false);

        //Botão para voltar para tela do plan premium
        voltar_plan_premium = view.findViewById(R.id.voltar_plan_premium);
        voltar_plan_premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_plan_premium());
                transaction.commit();
            }
        });

        //Botão para cancelar a confirmação dos dados
        btn_cancelar_confirmacao_dados = view.findViewById(R.id.btn_cancelar_confirmacao_dados);
        btn_cancelar_confirmacao_dados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_plan_premium());
                transaction.commit();
            }
        });

        //Botão para confirmar os dados e avancar para o pagamento
        btn_confirmar_dados = view.findViewById(R.id.btn_confirmar_dados);
        btn_confirmar_dados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_pagamento_plan_premium());
                transaction.commit();
            }
        });

        //Informações do usuário
        nome_usuario = view.findViewById(R.id.nome_usuario);
        phone_usuario = view.findViewById(R.id.phone_usuario);
        cpf_usuario = view.findViewById(R.id.cpf_usuario);
        buscarInformacoesDoUsuario(FirebaseAuth.getInstance().getCurrentUser().getEmail());

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