package com.example.khiata.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.khiata.R;
import com.example.khiata.adapters.AdapterEnderecosUsuario;
import com.example.khiata.models.Address;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_tela_enderecos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_tela_enderecos extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_tela_enderecos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_tela_enderecos.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_tela_enderecos newInstance(String param1, String param2) {
        fragment_tela_enderecos fragment = new fragment_tela_enderecos();
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

    ImageView voltar_home;
    ImageButton btn_adicionar_endereco;
//    private fragment_tela_adicionar_endereco fragment_tela_adicionar_endereco= new fragment_tela_adicionar_endereco();
//    private fragment_tela_home fragment_tela_home= new fragment_tela_home();
    List<Address> enderecos = new ArrayList();
    RecyclerView lista_enderecos;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tela_enderecos, container, false);

        //Ir para tela de home
//        voltar_home = view.findViewById(R.id.voltar_home);
//        voltar_home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.frame_conteudo, fragment_tela_home.newInstance("", ""));
//                transaction.commit();
//            }
//        });

        //Ir para tela de adicionar endereço
//        btn_adicionar_endereco = view.findViewById(R.id.btn_adicionar_endereco);
//        btn_adicionar_endereco.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.frame_conteudo, fragment_tela_adicionar_endereco.newInstance("", ""));
//                transaction.commit();
//            }
//        });

        //Carregar endereços do usuário
        lista_enderecos = view.findViewById(R.id.lista_enderecos);

        AdapterEnderecosUsuario enderecosUsuario = new AdapterEnderecosUsuario(getContext(), enderecos);
        lista_enderecos.setAdapter(enderecosUsuario);

        return view;
    }
}