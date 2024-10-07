package com.example.khiata.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.khiata.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_tela_adicionar_endereco#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_tela_adicionar_endereco extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_tela_adicionar_endereco() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_tela_adicionar_endereco.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_tela_adicionar_endereco newInstance(String param1, String param2) {
        fragment_tela_adicionar_endereco fragment = new fragment_tela_adicionar_endereco();
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

    ImageView voltar_enderecos;
    Button btn_cancelar_novo_endereco;
    private fragment_tela_enderecos fragment_tela_enderecos= new fragment_tela_enderecos();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tela_adicionar_endereco, container, false);

        voltar_enderecos = view.findViewById(R.id.voltar_enderecos);
        voltar_enderecos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, fragment_tela_enderecos);
                transaction.commit();
            }
        });

        btn_cancelar_novo_endereco = view.findViewById(R.id.btn_cancelar_novo_endereco);
        btn_cancelar_novo_endereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, fragment_tela_enderecos);
                transaction.commit();
            }
        });

        return view;
    }
}