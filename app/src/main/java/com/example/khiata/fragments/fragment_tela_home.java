package com.example.khiata.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.khiata.R;
import com.example.khiata.adapters.AdapterCostureirasRecomendadas;
import com.example.khiata.adapters.AdapterEnderecosUsuario;
import com.example.khiata.adapters.AdapterProdutosRecomendados;
import com.example.khiata.apis.AddressApi;
import com.example.khiata.apis.UserApi;
import com.example.khiata.models.Address;
import com.example.khiata.models.Produto;
import com.example.khiata.models.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    List<Produto> listaProdutos = new ArrayList();
    List<User> listaCostureira = new ArrayList();
    ImageView btn_pesquisa, btn_carrinho;
    private Retrofit retrofit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tela_home, container, false);

        // Carregar a lista de costureiras recomendadas
        costureiras_recomendas = view.findViewById(R.id.costureiras_recomendas);
        costureiras_recomendas.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        buscarCostureiras(FirebaseAuth.getInstance().getCurrentUser().getEmail());


        // Carregar a lista de produtos recomendados
        produtos_recomendados = view.findViewById(R.id.produtos_recomendados);
        try{
            listaProdutos.add(new Produto("Blusa Tricot Manga Longa Inverno Cores Tendência",35.99));
            listaProdutos.add(new Produto("edtrfgyuhiy",35.99));
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        AdapterProdutosRecomendados produtosRecomendados = new AdapterProdutosRecomendados(listaProdutos);
        produtos_recomendados.setAdapter(produtosRecomendados);
        produtos_recomendados.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        btn_pesquisa = view.findViewById(R.id.btn_pesquisa);
        btn_pesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_pesquisa());
                transaction.commit();
            }
        });

        return view;
    }

    //Método para buscar os usuários que são costureiras
    private void buscarCostureiras(String emailLogado){
        String API_BASE_URL = "https://apikhiata.onrender.com/";
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi userApi = retrofit.create(UserApi.class);
        Call<ArrayList<User>> call = userApi.selecionarTodos();
        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if (response.isSuccessful()) {
                    ArrayList<User> listaDeCostureiras = response.body();

                    if (listaDeCostureiras != null && !listaDeCostureiras.isEmpty()) {
                        // Limpa a lista antes de adicionar novos usuários
                        listaCostureira.clear();

                        // Filtrar apenas costureiras (isDressmaker == true) e e-mail diferente do logado
                        for (User usuario : listaDeCostureiras) {
                            if (usuario.isDressmaker() && !usuario.getEmail().equals(emailLogado)) {
                                listaCostureira.add(usuario);
                            }
                        }

                        if (!listaCostureira.isEmpty()) {
                            // Atualizar o Adapter apenas se houver costureiras correspondentes
                            AdapterCostureirasRecomendadas adapter = new AdapterCostureirasRecomendadas(getActivity(), listaCostureira);
                            costureiras_recomendas.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), "Nenhuma costureira encontrada.", Toast.LENGTH_SHORT).show();
                            // Exibir uma view alternativa aqui, se necessário
                        }
                    } else {
                        Toast.makeText(getActivity(), "Nenhum usuário encontrado.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Falha ao carregar usuários", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable throwable) {
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}