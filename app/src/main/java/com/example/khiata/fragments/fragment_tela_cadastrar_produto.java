package com.example.khiata.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.khiata.R;
import com.example.khiata.apis.AddressApi;
import com.example.khiata.apis.CategoryApi;
import com.example.khiata.apis.ProductApi;
import com.example.khiata.apis.UserApi;
import com.example.khiata.classes.CameraPerfil;
import com.example.khiata.classes.CameraProduto;
import com.example.khiata.models.Address;
import com.example.khiata.models.Category;
import com.example.khiata.models.Product;
import com.example.khiata.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_tela_cadastrar_produto#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_tela_cadastrar_produto extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_tela_cadastrar_produto() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_tela_cadastrar_produto.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_tela_cadastrar_produto newInstance(String param1, String param2) {
        fragment_tela_cadastrar_produto fragment = new fragment_tela_cadastrar_produto();
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

    ImageView voltar_area_costureira, btn_tirar_foto_produto;
    Button btn_cancelar_cadastrar_produto, btn_adicionar_produto;
    String nome_usuario;
    private Retrofit retrofit;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    String imgName;
    RadioGroup radioGroup;
    int categoriaSelecionada=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tela_cadastrar_produto, container, false);

        //Ir para tela de area costureira
        voltar_area_costureira = view.findViewById(R.id.voltar_area_costureira);
        voltar_area_costureira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_area_costureira());
                transaction.commit();
            }
        });

        //Cancelar Cadastro de Produto
        btn_cancelar_cadastrar_produto = view.findViewById(R.id.btn_cancelar_cadastrar_produto);
        btn_cancelar_cadastrar_produto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_area_costureira());
                transaction.commit();
            }
        });

        ImageView img_produto = view.findViewById(R.id.img_produto);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String imgProduto = bundle.getString("imgName");
            imgName = imgProduto;
            Log.e("Imagem", imgName);

            // Buscar a imagem no Firebase usando o nome da imagem
            if (imgProduto != null) {
                StorageReference profileRef = storageRef.child("khiata_produtos/" + imgProduto + ".jpg");
                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getContext()).load(uri).into(img_produto);
                        Log.d("TAG", "URL da imagem do produto: " + uri);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "Falha ao obter URL da imagem do produto: " + e.getMessage());
                        img_produto.setImageResource(R.drawable.add_img);
                    }
                });
            }
        } else {
            Log.e("Imagem", "Nenhuma imagem");
        }

        btn_tirar_foto_produto = view.findViewById(R.id.btn_tirar_foto_produto);
        btn_tirar_foto_produto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CameraProduto.class);
                startActivity(intent);
            }
        });

        //Pegar a lista de categorias
        radioGroup = view.findViewById(R.id.radioGroupCategorias);
        buscarCategorias();

        //Adicionar Produto
        btn_adicionar_produto = view.findViewById(R.id.btn_adicionar_produto);
        btn_adicionar_produto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String novoTitulo = ((EditText) view.findViewById(R.id.editTitulo)).getText().toString();
                String precoText = ((EditText) view.findViewById(R.id.editPrice)).getText().toString();
                double novoPreco = 0;
                if (!precoText.isEmpty()) {
                    try {
                        novoPreco = Double.parseDouble(precoText);
                    } catch (NumberFormatException e) {
                        novoPreco = 0;
                    }
                }
                String novaDescricao = ((EditText) view.findViewById(R.id.editDescricao)).getText().toString();
                String drawableImg = ((ImageView) view.findViewById(R.id.img_produto)).getDrawable().toString();
                GridLayout opcoesTamanho = view.findViewById(R.id.opcoesTamanho);
                RadioButton tamanhoSelecionado = null;
                String novoTamanho;
                //Percorre os RadioButtons do GridLayout e verificar se algum está selecionado
                for (int i = 0; i < opcoesTamanho.getChildCount(); i++) {
                    View child = opcoesTamanho.getChildAt(i);
                    if (child instanceof RadioButton) {
                        RadioButton radioButton = (RadioButton) child;
                        if (radioButton.isChecked()) {
                            tamanhoSelecionado = radioButton;
                            break;
                        }
                    }
                }
                //Pega qual RadioButton foi selecionado
                if(tamanhoSelecionado != null){
                    novoTamanho = tamanhoSelecionado.getText().toString();
                } else{
                    novoTamanho = null;
                }

                if(novoTitulo.isEmpty() || novoPreco == 0 || novaDescricao.isEmpty() || drawableImg.equals("@drawable/add_img") || categoriaSelecionada ==0){
                    Dialog dialog = new Dialog(getActivity());
                    LayoutInflater inflater = getLayoutInflater();
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);
                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Por favor, tire uma foto e preencha todos os campos.");
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
                } else{
                    buscarNomeDoUsuario(auth.getCurrentUser().getEmail(), novoTitulo, novoPreco, novaDescricao, imgName, novoTamanho, categoriaSelecionada);
                }
            }
        });

        return view;
    }

    //Pegar a lista de categorias
    private void buscarCategorias() {
        String API_BASE_URL = "https://api-khiata.onrender.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CategoryApi categoryApi = retrofit.create(CategoryApi.class);
        Call<List<Category>> call = categoryApi.getCategories(); // Mudança no tipo de retorno para List<Category>

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (!response.isSuccessful()) {
                    Dialog dialog = new Dialog(getActivity());

                    LayoutInflater inflater = getLayoutInflater();
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Erro: " + response.message());
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
                    Log.e("Error", response.code() + "");
                    return;
                }

                List<Category> categories = response.body(); // Usa diretamente a lista de objetos Category
                if (categories != null && !categories.isEmpty()) {
                    radioGroup.removeAllViews(); // Limpa o RadioGroup antes de adicionar novos itens

                    // Popula o RadioGroup com os RadioButton das categorias
                    for (Category category : categories) {
                        RadioButton radioButton = new RadioButton(getActivity());
                        radioButton.setTextColor(Color.BLACK);
                        radioButton.setTextSize(16);
                        radioButton.setTypeface(null, Typeface.BOLD);
                        radioButton.setText(category.getCategory());
                        radioButton.setTag(category.getId()); // Armazena o ID da categoria no RadioButton
                        radioGroup.addView(radioButton);
                        Log.d("Categoria", category.getCategory());
                    }

                    // Listener para capturar o ID da categoria selecionada
                    radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                        RadioButton selectedRadioButton = group.findViewById(checkedId);
                        if (selectedRadioButton != null) {
                            int selectedCategoryId = (int) selectedRadioButton.getTag();
                            // Salve ou use o ID da categoria conforme necessário
                            Log.d("Categoria Selecionada", "ID: " + selectedCategoryId);
                            categoriaSelecionada = selectedCategoryId;
                        }
                    });
                } else {
                    Dialog dialog = new Dialog(getActivity());

                    LayoutInflater inflater = getLayoutInflater();
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Não foi possível encontrar nenhuma categoria, tente novamente mais tarde.");
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


    //Pega o nome do usuário atual
    private void buscarNomeDoUsuario(String userEmail, String novoTitulo, double novoPreco, String novaDescricao, String imgName, String novoTamanho, int categoriaSelecionada) {
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
                Log.e("Nome do Usuário", nome_usuario);
                cadastrarProdutoUsuario(nome_usuario, novoTitulo, novoPreco, novaDescricao, imgName, novoTamanho, categoriaSelecionada);
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
                Log.e("Error", throwable.getMessage());
            }
        });
    }

    //Método para cadastrar um novo produto
    private void cadastrarProdutoUsuario(String nome_usuario, String novoTitulo, double novoPreco, String novaDescricao, String imgName, String novoTamanho, int categoriaSelecionada) {
        String API_BASE_URL = "https://api-khiata.onrender.com/";
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ProductApi productApi = retrofit.create(ProductApi.class);
        Call<String> call = productApi.insertProduct(novoTitulo, novoPreco, imgName, categoriaSelecionada, nome_usuario, 0, novaDescricao, novoTamanho);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    try {
                        String errorBody = response.errorBody().string();
                        Toast.makeText(getActivity(), "Erro: " + errorBody, Toast.LENGTH_LONG).show();
                        Log.e("Error", errorBody);
                        Dialog dialog = new Dialog(getActivity());

                        LayoutInflater inflater = getLayoutInflater();
                        View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                        TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                        msgPopup.setText("Erro: " + errorBody);
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
                    } catch (IOException e) {
                        e.printStackTrace();
                        Dialog dialog = new Dialog(getActivity());

                        LayoutInflater inflater = getLayoutInflater();
                        View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                        TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                        msgPopup.setText("Erro: " + e.getMessage());
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
                        Log.e("Error", e.getMessage());
                    }
                } else {
                    Dialog dialog = new Dialog(getActivity());

                    LayoutInflater inflater = getLayoutInflater();
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Produto cadastrado com sucesso.");
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
                    transaction.replace(R.id.frame_conteudo, new fragment_tela_area_costureira());
                    transaction.commit();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
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
                Log.e("Error", t.getMessage());
            }
        });
    }
}