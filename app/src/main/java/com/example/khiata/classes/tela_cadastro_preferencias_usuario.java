package com.example.khiata.classes;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.khiata.R;
import com.example.khiata.apis.CategoryApi;
import com.example.khiata.models.Category;
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

public class tela_cadastro_preferencias_usuario extends AppCompatActivity {
    Button salvar_preferencias;
    private List<Category> categoriasSelecionadas = new ArrayList();
    LinearLayout lista_categorias;

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

        lista_categorias = findViewById(R.id.lista_categorias);
        //Botão para salvar as preferências ao se cadastrar
        salvar_preferencias = findViewById(R.id.salvar_preferencias);
        salvar_preferencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    // Método para buscar as categorias e criar CheckBoxes
    private void buscarCategorias() {
        String API_BASE_URL = "https://interdisciplinarr.onrender.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CategoryApi categoryApi = retrofit.create(CategoryApi.class);
        Call<List<String>> call = categoryApi.getCategories();

        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Erro: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("Error", response.code() + "");
                    return;
                }

                List<String> jsonStringList = response.body();
                if (jsonStringList != null && !jsonStringList.isEmpty()) {
                    lista_categorias.removeAllViews(); // Limpa o LinearLayout antes de adicionar novos itens

                    Gson gson = new Gson();
                    Type categoryType = new TypeToken<Category>() {}.getType();
                    List<Category> categories = new ArrayList();

                    // Processa cada String JSON
                    for (String jsonString : jsonStringList) {
                        jsonString = jsonString.replace("'", "\""); // Corrige as aspas simples para aspas duplas
                        Category category = gson.fromJson(jsonString, categoryType);

                        if (category != null) {
                            categories.add(category);
                        } else {
                            Log.e("Error", "Erro ao converter categoria.");
                        }
                    }

                    // Popula o LinearLayout com CheckBoxes das categorias
                    for (Category category : categories) {
                        CheckBox checkBox = new CheckBox(getApplicationContext());
                        checkBox.setText(category.getType());
                        checkBox.setTag(category.getId()); // Armazena o ID da categoria no CheckBox
                        checkBox.setTextColor(Color.BLACK);
                        checkBox.setTextSize(16);

                        // Listener para armazenar/remover a seleção
                        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                            int categoryId = (int) buttonView.getTag();
                            String categoryTipo = buttonView.getText().toString();
                            if (isChecked) {
                                Category categoria = new Category(categoryId, categoryTipo);
                                // Adiciona o ID à lista se o CheckBox for marcado
                                categoriasSelecionadas.add(categoria);
                            } else {
                                // Remove o ID da lista se o CheckBox for desmarcado
                                categoriasSelecionadas.remove(category);
                            }
                        });

                        lista_categorias.addView(checkBox); // Adiciona o CheckBox ao LinearLayout
                    }

                    // Mostrar a lista de categorias selecionadas no Log
                    Log.d("Categorias Selecionadas", categoriasSelecionadas.toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Nenhuma categoria encontrada.", Toast.LENGTH_SHORT).show();
                    Log.e("Error", "Nenhuma categoria encontrada.");
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Error", throwable.getMessage());
            }
        });
    }
}