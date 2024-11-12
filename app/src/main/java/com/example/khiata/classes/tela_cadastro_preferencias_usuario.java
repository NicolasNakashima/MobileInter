package com.example.khiata.classes;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.khiata.R;
import com.example.khiata.apis.CategoryApi;
import com.example.khiata.apis.UserApi;
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

public class tela_cadastro_preferencias_usuario extends AppCompatActivity {
    Button salvar_preferencias, btn_home;
    ImageView voltar_inicio;
    private List<String> categoriasSelecionadas = new ArrayList();
    LinearLayout lista_categorias;
    int userId;
    private Retrofit retrofit;
    TratamentoErros tratamentoErros = new TratamentoErros();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_cadastro_preferencias_usuario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Buscar id do usuario
        buscarIdDoUsuario(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        //Botão para voltar ao inicio
        voltar_inicio = findViewById(R.id.voltar_inicio);
        voltar_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tela_cadastro_preferencias_usuario.this, tela_inicial.class);
                startActivity(intent);
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });

        //Botão para seguir para a tela home
        btn_home = findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(tela_cadastro_preferencias_usuario.this);
                LayoutInflater inflater = getLayoutInflater();
                View popup_opcao = inflater.inflate(R.layout.popup_opcao, null);

                TextView msgPopup = popup_opcao.findViewById(R.id.msg_popup);
                msgPopup.setText("Se você for para home, você pode defnir suas preferências depois.\n Deseja prosseguir?");
                ImageView imgPopup = popup_opcao.findViewById(R.id.img_popup);
                imgPopup.setImageResource(R.drawable.icon_pop_alert);
                Button btn_seguir = popup_opcao.findViewById(R.id.btn_seguir);
                btn_seguir.setText("Home");
                btn_seguir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(tela_cadastro_preferencias_usuario.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        dialog.cancel();
                    }
                });
                Button btn_cancelar = popup_opcao.findViewById(R.id.btn_cancelar);
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

        // Código para lidar com a seleção de categorias e clique no botão "salvar_preferencias"
        lista_categorias = findViewById(R.id.lista_categorias);
        buscarCategorias();
        salvar_preferencias = findViewById(R.id.salvar_preferencias);
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
                    Dialog dialog = new Dialog(tela_cadastro_preferencias_usuario.this);
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
                    Toast.makeText(getApplicationContext(), "Erro: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("Error", response.code() + "");
                    return;
                }

                List<Category> categories = response.body(); // Usa diretamente a resposta da API
                if (categories != null && !categories.isEmpty()) {
                    lista_categorias.removeAllViews(); // Limpa o LinearLayout antes de adicionar novos itens

                    // Popula o LinearLayout com CheckBoxes das categorias
                    for (Category category : categories) {
                        CheckBox checkBox = new CheckBox(getApplicationContext());
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
                    Dialog dialog = new Dialog(tela_cadastro_preferencias_usuario.this);

                    LayoutInflater inflater = getLayoutInflater();
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Não foi possível encontrar as categorias. Tente novamente mais tarde.");
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
                Dialog dialog = new Dialog(tela_cadastro_preferencias_usuario.this);
                LayoutInflater inflater = getLayoutInflater();
                View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                msgPopup.setText("Erro: "+tratamentoErros.tratandoErroThrowable(throwable));
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
                Dialog dialog = new Dialog(tela_cadastro_preferencias_usuario.this);

                LayoutInflater inflater = getLayoutInflater();
                View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                msgPopup.setText("Erro: "+tratamentoErros.tratandoErroThrowable(throwable));
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
                    // Pop up de erro
                    Dialog dialog = new Dialog(tela_cadastro_preferencias_usuario.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);
                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Erro " + tratamentoErros.tratandoErroApi(response));
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
                    Dialog dialog = new Dialog(tela_cadastro_preferencias_usuario.this);

                    LayoutInflater inflater = getLayoutInflater();
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Preferências cadastradas com sucesso!");
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
                    Intent intent = new Intent(tela_cadastro_preferencias_usuario.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Dialog dialog = new Dialog(tela_cadastro_preferencias_usuario.this);

                LayoutInflater inflater = getLayoutInflater();
                View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                msgPopup.setText("Erro: " + tratamentoErros.tratandoErroThrowable(t));
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