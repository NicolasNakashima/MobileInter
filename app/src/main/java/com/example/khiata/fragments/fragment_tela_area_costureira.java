package com.example.khiata.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.khiata.R;
import com.example.khiata.adapters.AdapterEnderecosUsuario;
import com.example.khiata.adapters.AdapterProdutosAdicionados;
import com.example.khiata.apis.AddressApi;
import com.example.khiata.apis.ProductApi;
import com.example.khiata.apis.UserApi;
import com.example.khiata.models.Address;
import com.example.khiata.models.Product;
import com.example.khiata.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_tela_area_costureira#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_tela_area_costureira extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_tela_area_costureira() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_tela_area_costureira.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_tela_area_costureira newInstance(String param1, String param2) {
        fragment_tela_area_costureira fragment = new fragment_tela_area_costureira();
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
    ImageButton btn_adicionar_produto;
    String nome_usuario;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private Retrofit retrofit;
    RecyclerView lista_produtos_adicionados;
    List<Product> produtos = new ArrayList();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tela_area_costureira, container, false);

        //Ir para tela de home
        voltar_home = view.findViewById(R.id.voltar_home);
        voltar_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_home());
                transaction.commit();
            }
        });

        //Ir para tela de cadastrar produtos
        btn_adicionar_produto = view.findViewById(R.id.btn_adicionar_produto);
        btn_adicionar_produto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_cadastrar_produto());
                transaction.commit();
            }
        });

        lista_produtos_adicionados = view.findViewById(R.id.lista_produtos_adicionados);
        lista_produtos_adicionados.setLayoutManager(new LinearLayoutManager(getContext()));
        buscarNomeDoUsuario(auth.getCurrentUser().getEmail());

        return view;
    }

    //Pega o nome do usuário atual
    private void buscarNomeDoUsuario(String userEmail) {
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
                User userResponse = response.body();
                nome_usuario = userResponse.getName();
                pegarProdutosDoUsuario(nome_usuario);
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Método para buscar os endereços do usuário
    private void pegarProdutosDoUsuario(String userName) {
        String API_BASE_URL = "https://interdisciplinarr.onrender.com/";
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductApi productApi = retrofit.create(ProductApi.class);
        Call<String> call = productApi.getProductsByDressmarker(userName);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String responseBody = response.body();

                    // Usando Gson para converter a String JSON em uma lista de objetos Product
                    Gson gson = new Gson();
                    Type productListType = new TypeToken<ArrayList<Product>>(){}.getType();
                    List<Product> listaDeProdutos = gson.fromJson(responseBody, productListType);

                    if (listaDeProdutos != null && !listaDeProdutos.isEmpty()) {
                        produtos.clear();
                        produtos.addAll(listaDeProdutos);

                        // Atualiza o adapter com a lista de produtos
                        AdapterProdutosAdicionados adapter = new AdapterProdutosAdicionados(getActivity(), produtos);
                        lista_produtos_adicionados.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "Nenhum produto cadastrado.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Falha ao carregar produtos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}