package com.example.khiata.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khiata.R;
import com.example.khiata.adapters.AdapterCostureirasRecomendadas;
import com.example.khiata.adapters.AdapterProdutosAdicionados;
import com.example.khiata.adapters.AdapterProdutosRecomendados;
import com.example.khiata.apis.ProductApi;
import com.example.khiata.apis.UserApi;
import com.example.khiata.classes.tela_carrinho;
import com.example.khiata.models.Product;
import com.example.khiata.models.User;
import com.example.khiata.models.UserPreference;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

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
    List<Product> produtos = new ArrayList();
    List<User> listaCostureira = new ArrayList();
    ImageView btn_pesquisa, btn_carrinho;
    List<String> userPreferences = new ArrayList();
    private Retrofit retrofit;
    String userName;

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
        produtos_recomendados.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        buscarPreferenciasUsuario(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        //Botão para ir para pesquisa
        btn_pesquisa = view.findViewById(R.id.btn_pesquisa);
        btn_pesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_pesquisa());
                transaction.commit();
            }
        });

        //Botão para ir para carrinho
        btn_carrinho = view.findViewById(R.id.btn_carrinho);
        btn_carrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), tela_carrinho.class);
                startActivity(intent);
            }
        });

        return view;
    }

    //Método para buscar os usuários que são costureiras
    private void buscarCostureiras(String emailUsuario) {
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
                            if (usuario.isDressmaker() && !usuario.getEmail().equals(emailUsuario)) {
                                listaCostureira.add(usuario);
                            }
                        }

                        if (!listaCostureira.isEmpty()) {
                            // Atualizar o Adapter apenas se houver costureiras correspondentes
                            AdapterCostureirasRecomendadas adapter = new AdapterCostureirasRecomendadas(getActivity(), listaCostureira);
                            costureiras_recomendas.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.e("Erro", "Nenhuma costureira encontrada.");
                        }
                    } else {
                        Log.e("Erro", "Nenhum usuário encontrado.");
                    }
                } else {
                    Dialog dialog = new Dialog(getActivity());
                    LayoutInflater inflater = getLayoutInflater();
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Falha ao carregar costureiras recomendadas, tente novamente mais tarde.");
                    ImageView imgPopup = popupView.findViewById(R.id.img_popup);
                    imgPopup.setImageResource(R.drawable.icon_pop_alert);
                    Button btnPopup = popupView.findViewById(R.id.btn_popup);
                    btnPopup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });

                    dialog.setContentView(popupView);
                    dialog.setCancelable(true);
                    dialog.show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable throwable) {
                if (getActivity() != null) {
                    Dialog dialog = new Dialog(getActivity());
                    LayoutInflater inflater = getLayoutInflater();
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Erro:" + throwable.getMessage());
                    ImageView imgPopup = popupView.findViewById(R.id.img_popup);
                    imgPopup.setImageResource(R.drawable.icon_pop_alert);
                    Button btnPopup = popupView.findViewById(R.id.btn_popup);
                    btnPopup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });

                    dialog.setContentView(popupView);
                    dialog.setCancelable(true);
                    dialog.show();
                }
            }
        });
    }

    //Método para buscar as preferências do usuário
    private void  buscarPreferenciasUsuario(String userEmail) {
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
                    userName = userResponse.getName();

                    List<UserPreference> preferences = userResponse.getUserPreferences();
                    if(preferences != null) {
                        for (UserPreference preference : preferences) {
                            userPreferences.add(preference.getValue());
                        }
                        Log.d("UserPreferences", userPreferences.toString());
                        pegarProdutosPreferenciais(userPreferences, userName);
                    }

                } else {
                    Dialog dialog = new Dialog(getActivity());
                    LayoutInflater inflater = getLayoutInflater();
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Erro:" + response.message());
                    ImageView imgPopup = popupView.findViewById(R.id.img_popup);
                    imgPopup.setImageResource(R.drawable.icon_pop_alert);
                    Button btnPopup = popupView.findViewById(R.id.btn_popup);
                    btnPopup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });

                    dialog.setContentView(popupView);
                    dialog.setCancelable(true);
                    dialog.show();
                    Log.e("API Error", "Response code: " + response.code() + " | Error body: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Dialog dialog = new Dialog(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                msgPopup.setText("Erro:" + throwable.getMessage());
                ImageView imgPopup = popupView.findViewById(R.id.img_popup);
                imgPopup.setImageResource(R.drawable.icon_pop_alert);
                Button btnPopup = popupView.findViewById(R.id.btn_popup);
                btnPopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                dialog.setContentView(popupView);
                dialog.setCancelable(true);
                dialog.show();
            }
        });
    }

    //Método para buscar os produtos com base na preferência do usuário
    private void pegarProdutosPreferenciais(List<String> userPreferences, String userName) {
        String API_BASE_URL = "https://api-khiata.onrender.com/";
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductApi productApi = retrofit.create(ProductApi.class);
        produtos.clear();  // Limpe a lista antes de iniciar o loop

        for (String preference : userPreferences) {
            Call<List<Product>> call = productApi.getProductsByCategory(preference);

            call.enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                    if (response.isSuccessful()) {
                        List<Product> productList = response.body();

                        if (productList != null && !productList.isEmpty()) {
                            produtos.addAll(productList);  // Adicione os produtos à lista
                        } else {
                            Dialog dialog = new Dialog(getActivity());
                            LayoutInflater inflater = getLayoutInflater();
                            View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                            TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                            msgPopup.setText("Você não possuí produtos recomendados.");
                            ImageView imgPopup = popupView.findViewById(R.id.img_popup);
                            imgPopup.setImageResource(R.drawable.icon_pop_alert);
                            Button btnPopup = popupView.findViewById(R.id.btn_popup);
                            btnPopup.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.cancel();
                                }
                            });

                            dialog.setContentView(popupView);
                            dialog.setCancelable(true);
                            dialog.show();
                            Log.e("Error", "Nenhum produto encontrado para a preferência: " + preference);
                        }

                        // Atualize o adapter após cada adição
                        if (produtos_recomendados.getAdapter() == null) {
                            AdapterProdutosRecomendados adapter = new AdapterProdutosRecomendados(getActivity(), produtos);
                            produtos_recomendados.setAdapter(adapter);
                        } else {
                            produtos_recomendados.getAdapter().notifyDataSetChanged();
                        }
                    } else {
                        Dialog dialog = new Dialog(getActivity());
                        LayoutInflater inflater = getLayoutInflater();
                        View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                        TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                        msgPopup.setText("Erro:" + response.message());
                        ImageView imgPopup = popupView.findViewById(R.id.img_popup);
                        imgPopup.setImageResource(R.drawable.icon_pop_alert);
                        Button btnPopup = popupView.findViewById(R.id.btn_popup);
                        btnPopup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });

                        dialog.setContentView(popupView);
                        dialog.setCancelable(true);
                        dialog.show();
                        Log.e("Error", response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<Product>> call, Throwable throwable) {
                    Dialog dialog = new Dialog(getActivity());
                    LayoutInflater inflater = getLayoutInflater();
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Erro:" + throwable.getMessage());
                    ImageView imgPopup = popupView.findViewById(R.id.img_popup);
                    imgPopup.setImageResource(R.drawable.icon_pop_alert);
                    Button btnPopup = popupView.findViewById(R.id.btn_popup);
                    btnPopup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });

                    dialog.setContentView(popupView);
                    dialog.setCancelable(true);
                    dialog.show();
                }
            });
        }
    }


}