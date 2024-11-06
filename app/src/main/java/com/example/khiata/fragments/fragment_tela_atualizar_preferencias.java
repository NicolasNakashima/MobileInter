package com.example.khiata.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khiata.R;
import com.example.khiata.apis.CategoryApi;
import com.example.khiata.apis.UserApi;
import com.example.khiata.classes.MainActivity;
import com.example.khiata.classes.tela_cadastro_preferencias_usuario;
import com.example.khiata.classes.tela_login;
import com.example.khiata.models.Category;
import com.example.khiata.models.User;
import com.example.khiata.models.UserPreference;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_tela_atualizar_preferencias#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_tela_atualizar_preferencias extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_tela_atualizar_preferencias() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_tela_atualizar_preferencias.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_tela_atualizar_preferencias newInstance(String param1, String param2) {
        fragment_tela_atualizar_preferencias fragment = new fragment_tela_atualizar_preferencias();
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

    ImageView voltar_perfil;
    Button btn_cancelar, salvar_preferencias;
    private List<String> categoriasSelecionadas = new ArrayList();
    LinearLayout lista_categorias;
    int userId;
    private Retrofit retrofit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tela_atualizar_preferencias, container, false);

        //Botão para voltar para a tela de perfil
        voltar_perfil = view.findViewById(R.id.voltar_perfil);
        voltar_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_perfil());
                transaction.commit();
            }
        });

        //Botão para cancelar a atualização das preferências
        btn_cancelar = view.findViewById(R.id.btn_cancelar);
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_perfil());
                transaction.commit();
            }
        });

        //Buscar id do usuario
        buscarIdDoUsuario(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        // Código para lidar com a seleção de categorias e clique no botão "salvar_preferencias"
        lista_categorias = view.findViewById(R.id.lista_categorias);
        buscarCategorias();
        salvar_preferencias = view.findViewById(R.id.salvar_preferencias);
        salvar_preferencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoriasSelecionadas.size() != 0) {
                    List<UserPreference> userPreferences = new ArrayList<>();
                    for (String categoria : categoriasSelecionadas) {
                        UserPreference pref = new UserPreference(userId, categoria);
                        userPreferences.add(pref);  // Adiciona cada preferência na lista
                    }
                    // Chama o método para atualizar as preferências
                    atualizarPreferenciasUsuario(userId, userPreferences);
                } else {
                    // Mostra o pop-up de alerta se nenhuma categoria for selecionada
                    Dialog dialog = new Dialog(getActivity());
                    LayoutInflater inflater = getLayoutInflater();
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);
                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Por favor, selecione pelo menos uma categoria.");
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

        return view;
    }

    //Método para buscar o ID do usuário
    private void buscarIdDoUsuario(String userEmail){
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
                userId = userResponse.getId();
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Dialog dialog = new Dialog(getActivity());

                LayoutInflater inflater = getLayoutInflater();
                View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                msgPopup.setText("Erro: " + throwable.getMessage());
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

    // Método para buscar as categorias e criar CheckBoxes
    private void buscarCategorias() {
        String API_BASE_URL = "https://api-khiata.onrender.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CategoryApi categoryApi = retrofit.create(CategoryApi.class);
        Call<List<Category>> call = categoryApi.getCategories();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Erro: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("Error", response.code() + "");
                    return;
                }

                List<Category> categories = response.body(); // Usa diretamente a resposta da API
                if (categories != null && !categories.isEmpty()) {
                    lista_categorias.removeAllViews(); // Limpa o LinearLayout antes de adicionar novos itens

                    // Popula o LinearLayout com CheckBoxes das categorias
                    for (Category category : categories) {
                        CheckBox checkBox = new CheckBox(getActivity());
                        checkBox.setText(category.getCategory());
                        checkBox.setTag(category.getId()); // Armazena o ID da categoria no CheckBox
                        checkBox.setTextColor(Color.BLACK);
                        checkBox.setTextSize(25);
                        checkBox.setTypeface(null, Typeface.BOLD);

                        Log.d("Categorias", category.getCategory());

                        // Listener para armazenar/remover a seleção
                        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                            String categoryTipo = buttonView.getText().toString();

                            if (isChecked) {
                                // Adiciona o tipo de categoria à lista se o CheckBox for marcado
                                categoriasSelecionadas.add(categoryTipo);
                            } else {
                                // Remove o tipo de categoria da lista se o CheckBox for desmarcado
                                categoriasSelecionadas.remove(categoryTipo);
                            }

                            // Mostrar a lista de categorias selecionadas no Log
                            Log.d("Categorias Selecionadas", categoriasSelecionadas.toString());
                        });

                        lista_categorias.addView(checkBox); // Adiciona o CheckBox ao LinearLayout
                    }
                } else {
                    Dialog dialog = new Dialog(getActivity());

                    LayoutInflater inflater = getLayoutInflater();
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Não foi possível encontrar nenhuma categoria, tente mais tarde.");
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
                    Log.e("Error", "Nenhuma categoria encontrada.");
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable throwable) {
                Dialog dialog = new Dialog(getActivity());

                LayoutInflater inflater = getLayoutInflater();
                View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                msgPopup.setText("Erro: " + throwable.getMessage());
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
                Log.e("Error", throwable.getMessage());
            }
        });
    }

    //Método para atualizar o perfil do usuário para definir as preferências dele
    private void atualizarPreferenciasUsuario(int userId, List<UserPreference> preferencias) {
        String API_BASE_URL = "https://apikhiata.onrender.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi userApi = retrofit.create(UserApi.class);

        // Monta o mapa de atualizações
        Map<String, Object> atualizacoes = new HashMap<>();
        atualizacoes.put("userPreferences", preferencias);

        // Faz a chamada para a API
        Call<Void> call = userApi.atualizarPreferencias(userId, atualizacoes);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    // Captura a mensagem de erro diretamente
                    String errorMessage = "Erro: " + response.code();
                    if (response.errorBody() != null) {
                        try {
                            // Lê o conteúdo da mensagem de erro
                            errorMessage += " - " + response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Dialog dialog = new Dialog(getActivity());

                    LayoutInflater inflater = getLayoutInflater();
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Erro: " + errorMessage);
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
                    Log.e("Error", errorMessage);
                } else {
                    // A atualização foi bem-sucedida
                    Dialog dialog = new Dialog(getActivity());

                    LayoutInflater inflater = getLayoutInflater();
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Preferências atualizadas com sucesso.");
                    ImageView imgPopup = popupView.findViewById(R.id.img_popup);
                    imgPopup.setImageResource(R.drawable.icon_pop_sucesso);
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
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_conteudo, new fragment_tela_perfil());
                    transaction.commit();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Dialog dialog = new Dialog(getActivity());

                LayoutInflater inflater = getLayoutInflater();
                View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                msgPopup.setText("Erro: " + t.getMessage());
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