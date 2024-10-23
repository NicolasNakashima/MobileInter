package com.example.khiata.fragments;

import android.app.Dialog;
import android.content.Intent;
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
import com.example.khiata.classes.tela_inicial;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_tela_pagamento_plan_premium#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_tela_pagamento_plan_premium extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_tela_pagamento_plan_premium() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_tela_pagamento_plan_premium.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_tela_pagamento_plan_premium newInstance(String param1, String param2) {
        fragment_tela_pagamento_plan_premium fragment = new fragment_tela_pagamento_plan_premium();
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

    ImageView voltar_resumo_dados_pagamento;
    Button btn_pagar, btn_cancelar_pagamento;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tela_pagamento_plan_premium, container, false);

        //Botão voltar para tela de resumo dos dados de pagamento
        voltar_resumo_dados_pagamento = view.findViewById(R.id.voltar_resumo_dados_pagamento);
        voltar_resumo_dados_pagamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_dados_compra_premium());
                transaction.commit();
            }
        });

        //Botão para cancelar o pagamento
        btn_cancelar_pagamento = view.findViewById(R.id.btn_cancelar_pagamento);
        btn_cancelar_pagamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                View popup_opcao = inflater.inflate(R.layout.popup_opcao, null);

                TextView msgPopup = popup_opcao.findViewById(R.id.msg_popup);
                msgPopup.setText("Deseja cancelar o processo de pagamento?");
                ImageView imgPopup = popup_opcao.findViewById(R.id.img_popup);
                imgPopup.setImageResource(R.drawable.icon_pop_alert);
                Button btn_seguir = popup_opcao.findViewById(R.id.btn_seguir);
                btn_seguir.setText("Sim");
                btn_seguir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_conteudo, new fragment_tela_plan_premium());
                        transaction.commit();
                        dialog.cancel();
                    }
                });
                Button btn_cancelar = popup_opcao.findViewById(R.id.btn_cancelar);
                btn_cancelar.setText("Não");
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

        //Botão para realizar o pagamento
        btn_pagar = view.findViewById(R.id.btn_pagar);
        btn_pagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    //Método para mudar o status premium para 2 (pendente)
    private void deixarStatusPremiumPendente(String email, Map<String, Object> atualizacoes) {
        String API_BASE_URL = "https://apikhiata.onrender.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi userApi = retrofit.create(UserApi.class);
        Call<Void> call = userApi.atualizarUsuario(email, atualizacoes);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    // Captura a mensagem de erro diretamente
                    String errorMessage = "Erro: " + response.code(); // Exibe o código do erro
                    if (response.errorBody() != null) {
                        errorMessage += " - " + response.errorBody().toString();
                    }
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                    Log.e("Error", errorMessage);
                } else {
                    // A atualização foi bem-sucedida
                    Toast.makeText(getActivity(), "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_conteudo, new fragment_tela_perfil());
                    transaction.commit();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getActivity(), "Erro ao atualizar perfil: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}