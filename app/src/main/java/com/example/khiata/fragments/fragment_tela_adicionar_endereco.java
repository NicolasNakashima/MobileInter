package com.example.khiata.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khiata.R;
import com.example.khiata.apis.AddressApi;
import com.example.khiata.apis.UserApi;
import com.example.khiata.classes.MainActivity;
import com.example.khiata.classes.tela_cadastro;
import com.example.khiata.models.Address;
import com.example.khiata.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_tela_adicionar_endereco#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_tela_adicionar_endereco extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_tela_adicionar_endereco() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_tela_adicionar_endereco.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_tela_adicionar_endereco newInstance(String param1, String param2) {
        fragment_tela_adicionar_endereco fragment = new fragment_tela_adicionar_endereco();
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

    ImageView voltar_enderecos;
    Button btn_cancelar_novo_endereco, btn_adicionar_endereco;
//    private fragment_tela_enderecos fragment_tela_enderecos= new fragment_tela_enderecos();
    int userId;
    private Retrofit retrofit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tela_adicionar_endereco, container, false);

        //Ir para tela de endereços
        voltar_enderecos = view.findViewById(R.id.voltar_enderecos);
        voltar_enderecos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_enderecos());
                transaction.commit();
            }
        });

        //Cancelar criação de novo endereço
        btn_cancelar_novo_endereco = view.findViewById(R.id.btn_cancelar_novo_endereco);
        btn_cancelar_novo_endereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_enderecos());
                transaction.commit();
            }
        });

        //Adicionar novo endereço do usuário
        btn_adicionar_endereco = view.findViewById(R.id.btn_adicionar_endereco);
        btn_adicionar_endereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String novoDestinatario = ((EditText) view.findViewById(R.id.editDestinatario)).getText().toString();
                String novaRua = ((EditText) view.findViewById(R.id.editRua)).getText().toString();
                int novoNumero = Integer.parseInt(((EditText) view.findViewById(R.id.editNumero)).getText().toString());
                String novoComplemento = ((EditText) view.findViewById(R.id.editComplemento)).getText().toString();
                String novoRotulo = ((EditText) view.findViewById(R.id.editRotulo)).getText().toString();
                String novoCEP = ((EditText) view.findViewById(R.id.editCEP)).getText().toString();

                if(novoDestinatario.isEmpty() || novaRua.isEmpty() || novoNumero == 0 || novoRotulo.isEmpty() || novoCEP.isEmpty()) {
                    Dialog dialog = new Dialog(getActivity());
                    LayoutInflater inflater = getLayoutInflater();
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);
                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Por favor, preencha os campos para cadastrar um novo endereço.");
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
                    if(novoComplemento.isEmpty()){
                        novoComplemento = null;
                    }
                    Address novoEndereco = new Address(novoDestinatario, novaRua, novoNumero, novoComplemento, novoRotulo, novoCEP);
                    Log.d("Address", new Gson().toJson(novoEndereco));
                    buscarIdDoUsuario(FirebaseAuth.getInstance().getCurrentUser().getEmail(), novoEndereco);
                }
            }
        });

        return view;
    }

    //Método para buscar o ID do usuário
    private void buscarIdDoUsuario(String userEmail, Address address){
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
                cadastrarEnderecoUsuario(userId, address) ;
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Método para cadastrar um novo endereço do usuário
    private void cadastrarEnderecoUsuario(int userId, Address address){
        String API_BASE_URL = "https://apikhiata.onrender.com/";
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AddressApi addressApi = retrofit.create(AddressApi.class);
        Call<Address> call = addressApi.inserirEnderecoUsuario(userId, address);
        call.enqueue(new Callback<Address>() {
            @Override
            public void onResponse(Call<Address> call, Response<Address> response) {
                if (!response.isSuccessful()) {
                    try {
                        String errorBody = response.errorBody().string();
                        Toast.makeText(getActivity(), "Erro: " + errorBody, Toast.LENGTH_LONG).show();
                        Log.e("Error", errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "Endereço cadastrado com sucesso", Toast.LENGTH_LONG).show();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_conteudo, new fragment_tela_enderecos());
                    transaction.commit();
                }
            }

            @Override
            public void onFailure(Call<Address> call, Throwable t) {
                Toast.makeText(getActivity(), "Erro ao cadastrar endereço: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Error", t.getMessage());
            }
        });
    }
}