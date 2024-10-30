package com.example.khiata.fragments;

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
import android.widget.Toast;

import com.example.khiata.R;
import com.example.khiata.adapters.AdapterEnderecosUsuario;
import com.example.khiata.apis.AddressApi;
import com.example.khiata.apis.UserApi;
import com.example.khiata.models.Address;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_tela_editar_endereco#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_tela_editar_endereco extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_tela_editar_endereco() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_tela_editar_endereco.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_tela_editar_endereco newInstance(String param1, String param2) {
        fragment_tela_editar_endereco fragment = new fragment_tela_editar_endereco();
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
    Button btn_cancelar_atualizar_endereco, btn_atualizar_endereco;
    int enderecoId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tela_editar_endereco, container, false);

        // Ir para tela de enderecos
        voltar_enderecos= view.findViewById(R.id.voltar_endercos);
        voltar_enderecos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_enderecos());
                transaction.commit();
            }
        });

        //Cancelar alteracoes de endereco
        btn_cancelar_atualizar_endereco= view.findViewById(R.id.btn_cancelar_atualizar_endereco);
        btn_cancelar_atualizar_endereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_enderecos());
                transaction.commit();
            }
        });

        //Atualizar Endereco
        btn_atualizar_endereco = view.findViewById(R.id.btn_atualizar_endereco);
        btn_atualizar_endereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String atualizarDestinatario = ((EditText) view.findViewById(R.id.atualizarDestinatario)).getText().toString();
                String atualizarRua = ((EditText) view.findViewById(R.id.atualizarRua)).getText().toString();
                String atualizarComplemento = ((EditText) view.findViewById(R.id.atualizarComplemento)).getText().toString();
                String atualizarRotulo = ((EditText) view.findViewById(R.id.atualizarRotulo)).getText().toString();
                String atualizarCEP = ((EditText) view.findViewById(R.id.atualizarCEP)).getText().toString();
                int atualizarNumero = 0;
                String numeroTexto = ((EditText) view.findViewById(R.id.atualizarNumero)).getText().toString();
                if (!numeroTexto.isEmpty()) {
                    try {
                        atualizarNumero = Integer.parseInt(numeroTexto);
                    } catch (NumberFormatException e) {
                        Log.e("Error", "Idade inválida: " + e.getMessage());
                    }
                }
                Map<String, Object> atualizacoes = new HashMap<>();
                if(!atualizarDestinatario.isEmpty()){
                    atualizacoes.put("recipient", atualizarDestinatario);
                }
                if(!atualizarRua.isEmpty()){
                    atualizacoes.put("street", atualizarRua);
                }
                if(!atualizarComplemento.isEmpty()){
                    atualizacoes.put("complement", atualizarComplemento);
                }
                if(!atualizarRotulo.isEmpty()){
                    atualizacoes.put("label", atualizarRotulo);
                }
                if(!atualizarCEP.isEmpty()){
                    atualizacoes.put("cep", atualizarCEP);
                }
                if(atualizarNumero != 0){
                    atualizacoes.put("number", atualizarNumero);
                }
                if (atualizacoes.isEmpty()) {
                    // Nenhuma alteração feita
                    Toast.makeText(getActivity(), "Nenhuma alteração foi feita.", Toast.LENGTH_SHORT).show();
                } else{
                    Bundle bundle = getArguments();
                    if(bundle != null) {
                        enderecoId = bundle.getInt("enderecoId", -1);
                        if (enderecoId != -1) {
                            atualizarEndercoDoUsuario(enderecoId, atualizacoes);
                        }
                    }
                }
            }
        });

        return view;
    }


    //Método para atualizar um endereço do usário
    private void atualizarEndercoDoUsuario(int addressId, Map<String, Object> atualizacoes) {
        String API_BASE_URL = "https://apikhiata.onrender.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AddressApi addressApi = retrofit.create(AddressApi.class);
        Call<Void> call = addressApi.atualizarEnderecoPorId(addressId, atualizacoes);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    // Captura a mensagem de erro diretamente
                    String errorMessage = "Erro: " + response.code(); // Exibe o código do erro
                    if (response.errorBody() != null) {
                        errorMessage += " - " + response.errorBody().toString();
                    }
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                    Log.e("Error", errorMessage);
                } else {
                    // A atualização foi bem-sucedida
                    Toast.makeText(getActivity(), "Endereço atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_conteudo, new fragment_tela_enderecos());
                    transaction.commit();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getActivity(), "Erro ao atualizar endereço: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}