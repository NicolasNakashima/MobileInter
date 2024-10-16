package com.example.khiata.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.khiata.R;
import com.example.khiata.adapters.AdapterProdutosAdicionados;
import com.example.khiata.apis.ProductApi;
import com.example.khiata.models.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_tela_pesquisa#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_tela_pesquisa extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_tela_pesquisa() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_tela_pesquisa.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_tela_pesquisa newInstance(String param1, String param2) {
        fragment_tela_pesquisa fragment = new fragment_tela_pesquisa();
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

    ImageView voltar_home, btn_pesquisar;
    private Retrofit retrofit;
    RecyclerView lista_produtos_pesquisados;
    List<Product> produtos = new ArrayList();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tela_pesquisa, container, false);


        //Botão para voltar para tela home
        voltar_home = view.findViewById(R.id.voltar_home);
        voltar_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_home());
                transaction.commit();
            }
        });

        //Botão para pesquisar produtos
        btn_pesquisar = view.findViewById(R.id.btn_pesquisar);
        btn_pesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String busca = ((EditText) view.findViewById(R.id.barra_pesquisa)).getText().toString();
                if(!busca.isEmpty()){
                    Toast.makeText(getActivity(), "Nenhum produto encontrado", Toast.LENGTH_SHORT).show();
                }
                else{
                    lista_produtos_pesquisados = view.findViewById(R.id.lista_produtos_pesquisados);
                    lista_produtos_pesquisados.setLayoutManager(new LinearLayoutManager(getContext()));
                    pegarProdutosPesquisados(busca);
                }
            }
        });

        return view;
    }

    //Método para buscar os produtos pesquisados
    private void pegarProdutosPesquisados(String productName) {
        String API_BASE_URL = "https://interdisciplinarr.onrender.com/";
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductApi productApi = retrofit.create(ProductApi.class);
        Call<List<Product>> call = productApi.getByName(productName); // Mudança aqui

        call.enqueue(new Callback<List<Product>>() {  // Mudança aqui
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> listaDeProdutos = response.body();

                    if (listaDeProdutos != null && !listaDeProdutos.isEmpty()) {
                        produtos.clear();
                        produtos.addAll(listaDeProdutos);

                        // Atualiza o adapter com a lista de produtos
                        AdapterProdutosAdicionados adapter = new AdapterProdutosAdicionados(getActivity(), produtos);
                        lista_produtos_pesquisados.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "Nenhum produto encontrado.", Toast.LENGTH_SHORT).show();
                        Log.e("Error", "Nenhum produto encontrado.");
                    }
                } else {
                    Toast.makeText(getActivity(), "Falha ao carregar produtos", Toast.LENGTH_SHORT).show();
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable throwable) {
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Error", throwable.getMessage());
            }
        });
    }
}