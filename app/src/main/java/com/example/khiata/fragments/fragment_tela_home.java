package com.example.khiata.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.khiata.R;
import com.example.khiata.adapters.AdapterCostureirasRecomendadas;
import com.example.khiata.adapters.AdapterProdutosRecomendados;
import com.example.khiata.models.Costureira;
import com.example.khiata.models.Produto;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_tela_home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_tela_home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_tela_home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_tela_home.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_tela_home newInstance(String param1, String param2) {
        fragment_tela_home fragment = new fragment_tela_home();
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

    RecyclerView costureiras_recomendas, produtos_recomendados;
    List<Costureira> listaCostureira = new ArrayList();
    List<Produto> listaProdutos = new ArrayList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_tela_home, container, false);

        costureiras_recomendas = view.findViewById(R.id.costureiras_recomendas);

        AdapterCostureirasRecomendadas costureirasRecomendadas = new AdapterCostureirasRecomendadas(listaCostureira);
        costureiras_recomendas.setAdapter(costureirasRecomendadas);
        costureiras_recomendas.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        try{
            listaCostureira.add(new Costureira("Maria"));
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        produtos_recomendados = view.findViewById(R.id.produtos_recomendados);

        AdapterProdutosRecomendados produtosRecomendados = new AdapterProdutosRecomendados(listaProdutos);
        produtos_recomendados.setAdapter(produtosRecomendados);
        produtos_recomendados.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        try{
            listaProdutos.add(new Produto("Blusa Tricot Manga Longa Inverno Cores Tendência",35.99));
        } catch (Exception e){
            throw new RuntimeException(e);
        }


        return view;
    }
}