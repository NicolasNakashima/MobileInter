package com.example.khiata.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khiata.R;
import com.example.khiata.apis.AddressApi;
import com.example.khiata.apis.ProductApi;
import com.example.khiata.apis.UserApi;
import com.example.khiata.models.Address;
import com.example.khiata.models.Product;
import com.example.khiata.models.User;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

    ImageView voltar_area_costureira;
    Button btn_cancelar_cadastrar_produto, btn_adicionar_produto;
    String nome_usuario;
    private Retrofit retrofit;
    FirebaseAuth auth = FirebaseAuth.getInstance();
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

        //Adicionar Produto
        btn_adicionar_produto = view.findViewById(R.id.btn_adicionar_produto);
        btn_adicionar_produto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String novoTitulo = ((EditText) view.findViewById(R.id.editTitulo)).getText().toString();
                double novoPreco = Double.parseDouble(((EditText) view.findViewById(R.id.editPrice)).getText().toString());
                String novaDescricao = ((EditText) view.findViewById(R.id.editDescricao)).getText().toString();

                if(novoTitulo.isEmpty() || novoPreco == 0 || novaDescricao.isEmpty()){
                    Dialog dialog = new Dialog(getActivity());
                    LayoutInflater inflater = getLayoutInflater();
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);
                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Por favor, preencha todos os campos para cadastrar um novo produto.");
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
                    buscarNomeDoUsuario(auth.getCurrentUser().getEmail(), novoTitulo, novoPreco, novaDescricao);
                }
            }
        });

        return view;
    }

    //Pega o nome do usuário atual
    private void buscarNomeDoUsuario(String userEmail, String novoTitulo, double novoPreco, String novaDescricao) {
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
                cadastrarEnderecoUsuario(nome_usuario, novoTitulo, novoPreco, novaDescricao);
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Método para cadastrar um novo produto
    private void cadastrarEnderecoUsuario(String nome_usuario, String novoTitulo, double novoPreco, String novaDescricao) {
        String API_BASE_URL = "https://interdisciplinarr.onrender.com/";
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ProductApi productApi = retrofit.create(ProductApi.class);
        Call<String> call = productApi.insertProduct(0, novoTitulo, novoPreco, "", 0, nome_usuario, novaDescricao);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    try {
                        String errorBody = response.errorBody().string();
                        Toast.makeText(getActivity(), "Erro: " + errorBody, Toast.LENGTH_LONG).show();
                        Log.e("Error", errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "produto cadastrado com sucesso", Toast.LENGTH_LONG).show();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_conteudo, new fragment_tela_area_costureira());
                    transaction.commit();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity(), "Erro ao cadastrar endereço: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Error", t.getMessage());
            }
        });
    }
}