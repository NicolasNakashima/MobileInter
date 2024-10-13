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
    int addressId;
    private Retrofit retrofit;
    String atualDestinatario, atualRua, atualComplemento, atualRotulo;
    int atualNumero;
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

        Bundle bundle = getArguments();
        if(bundle != null) {
            enderecoId = bundle.getInt("enderecoId", -1);
            if (enderecoId != -1) {
                pegarEnderecoDoUsuario(enderecoId);
            }
        }

        //Atualizar Endereco
        btn_atualizar_endereco = view.findViewById(R.id.btn_atualizar_endereco);
        btn_atualizar_endereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String atualizarDestinatario = ((EditText) view.findViewById(R.id.atualizarDestinatario)).getText().toString();
                String atualizarRua = ((EditText) view.findViewById(R.id.atualizarRua)).getText().toString();
                String atualizarComplemento = ((EditText) view.findViewById(R.id.atualizarComplemento)).getText().toString();
                String atualizarRotulo = ((EditText) view.findViewById(R.id.atualizarRotulo)).getText().toString();
                String numeroTexto = ((EditText) view.findViewById(R.id.atualizarNumero)).getText().toString();

                // Verificar se os campos estão vazios ou não
                boolean houveAtualizacao = false;
                if (!atualizarDestinatario.isEmpty()) {
                    atualDestinatario = atualizarDestinatario; // Atualizar destinatário
                    houveAtualizacao = true;
                }
                if (!atualizarRua.isEmpty()) {
                    atualRua = atualizarRua; // Atualizar rua
                    houveAtualizacao = true;
                }
                if (!atualizarComplemento.isEmpty()) {
                    atualComplemento = atualizarComplemento; // Atualizar complemento
                    houveAtualizacao = true;
                }
                if (!atualizarRotulo.isEmpty()) {
                    atualRotulo = atualizarRotulo; // Atualizar rótulo
                    houveAtualizacao = true;
                }
                int atualizarNumero = 0;
                if (!numeroTexto.isEmpty()) {
                    try {
                        atualizarNumero = Integer.parseInt(numeroTexto);
                        atualNumero = atualizarNumero; // Atualizar número
                        houveAtualizacao = true;
                    } catch (NumberFormatException e) {
                        Log.e("Error", "Número inválido: " + e.getMessage());
                    }
                }

                // Verificar se alguma atualização foi feita
                if (houveAtualizacao) {
                    // Criar o objeto Address atualizado
                    Address enderecoAtualizado = new Address(enderecoId, atualDestinatario, atualRua, atualNumero, atualComplemento, atualRotulo);
                    Log.e("Endereço", enderecoAtualizado.toString());
                    Log.d("User", new Gson().toJson(enderecoAtualizado));
                    atualizarEnderecoUsuario(enderecoId, enderecoAtualizado); // Enviar a atualização para a API
                } else {
                    // Exibir mensagem informando que não houve atualização
                    Toast.makeText(getActivity(), "Nenhuma alteração feita para atualizar o endereço.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }


    //Método para atualizar um endereço do usário
    private void atualizarEnderecoUsuario(int addressId, Address atualizacao) {
        Log.e("atualizacao", atualizacao.toString());
        Log.d("User", new Gson().toJson(atualizacao));
        Log.e("addressId", String.valueOf(addressId));
        String API_BASE_URL = "https://apikhiata.onrender.com/";
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        AddressApi addressApi = retrofit.create(AddressApi.class);
        Call<String> call = addressApi.atualizarEndereco(addressId, atualizacao);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Pegue o corpo da resposta como String
                    String mensagemResposta = response.body();
                    Log.d("Response Body", mensagemResposta);
                    // Continue com a lógica de sucesso
                    Toast.makeText(getActivity(), mensagemResposta, Toast.LENGTH_SHORT).show();
                } else {
                    // Tratamento de erro com mensagem do erro
                    String errorMessage = "Erro: " + response.code(); // Exibe o código do erro
                    if (response.errorBody() != null) {
                        try {
                            errorMessage += " - " + response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                    Log.e("Error", errorMessage);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity(), "Erro ao atualizar endereço: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Error", t.getMessage());
            }
        });
    }


    //Método para buscar endereço por Id
    private void pegarEnderecoDoUsuario(int addressId){
        String API_BASE_URL = "https://apikhiata.onrender.com/";
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AddressApi addressApi = retrofit.create(AddressApi.class);
        Call<Address> call = addressApi.selecionarEnderecoPorId(addressId);
        call.enqueue(new Callback<Address>() {
            @Override
            public void onResponse(Call<Address> call, Response<Address> response) {
                if (response.isSuccessful()) {
                    Address address = response.body();
                    if (address != null) {
                        atualDestinatario = address.getRecipient();
                        atualRua = address.getStreet();
                        atualNumero = address.getNumber();
                        atualComplemento = address.getComplement();
                        atualRotulo = address.getLabel();

                    } else {
                        Toast.makeText(getActivity(), "Endereço não encontrado", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getActivity(), "Falha ao buscar endereço", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Address> call, Throwable throwable) {
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}