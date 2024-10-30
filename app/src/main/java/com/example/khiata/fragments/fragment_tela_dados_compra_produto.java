package com.example.khiata.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.khiata.R;
import com.example.khiata.classes.tela_carrinho;

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
    TextView escolher_outro_endereco;
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

        return view;
    }
}