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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tela_editar_endereco, container, false);

        // Ir para tela de endereços
        voltar_enderecos = view.findViewById(R.id.voltar_endercos);
        voltar_enderecos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_enderecos());
                transaction.commit();
            }
        });

        // Cancelar alterações de endereço
        btn_cancelar_atualizar_endereco = view.findViewById(R.id.btn_cancelar_atualizar_endereco);
        btn_cancelar_atualizar_endereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_enderecos());
                transaction.commit();
            }
        });

        // Buscar dados do endereço
        Bundle bundle = getArguments();
        if (bundle != null) {
            addressId = bundle.getInt("enderecoId", -1);
            if (addressId != -1) {
                pegarEnderecosDoUsuario(addressId); // Buscar o endereço atual e preencher os campos
            }
        }

        // Atualizar Endereço
        btn_atualizar_endereco = view.findViewById(R.id.btn_atualizar_endereco);
        btn_atualizar_endereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obter dados atualizados do formulário
                String atualizarDestinatario = ((EditText) view.findViewById(R.id.atualizarDestinatario)).getText().toString();
                String atualizarRua = ((EditText) view.findViewById(R.id.atualizarRua)).getText().toString();
                String numeroTexto = ((EditText) view.findViewById(R.id.atualizarNumero)).getText().toString();
                int atualizarNumero = !numeroTexto.isEmpty() ? Integer.parseInt(numeroTexto) : atualNumero;
                String atualizarComplemento = ((EditText) view.findViewById(R.id.atualizarComplemento)).getText().toString();
                String atualizarRotulo = ((EditText) view.findViewById(R.id.atualizarRotulo)).getText().toString();

                // Verificar se houve alterações
                boolean houveAlteracao = false;

                if (!atualizarDestinatario.isEmpty() && !atualizarDestinatario.equals(atualDestinatario)) {
                    atualDestinatario = atualizarDestinatario;
                    houveAlteracao = true;
                }
                if (!atualizarRua.isEmpty() && !atualizarRua.equals(atualRua)) {
                    atualRua = atualizarRua;
                    houveAlteracao = true;
                }
                if (!numeroTexto.isEmpty() && atualizarNumero != atualNumero) {
                    atualNumero = atualizarNumero;
                    houveAlteracao = true;
                }
                if (!atualizarComplemento.isEmpty() && !atualizarComplemento.equals(atualComplemento)) {
                    atualComplemento = atualizarComplemento;
                    houveAlteracao = true;
                }
                if (!atualizarRotulo.isEmpty() && !atualizarRotulo.equals(atualRotulo)) {
                    atualRotulo = atualizarRotulo;
                    houveAlteracao = true;
                }

                if (houveAlteracao) {
                    // Criar o objeto Address com as atualizações
                    Address addressAtualizado = new Address(atualDestinatario, atualRua, atualNumero, atualComplemento, atualRotulo);
                    atualizarUsuarioAPI(addressId, addressAtualizado);
                } else {
                    // Exibir mensagem de erro se nenhuma alteração for feita
                    Toast.makeText(getActivity(), "Nenhuma alteração foi feita.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    // Método para buscar endereço por Id
    private void pegarEnderecosDoUsuario(int addressId) {
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
                        // Preencher os campos com os dados atuais do endereço
                        atualDestinatario = address.getRecipient();
                        atualRua = address.getStreet();
                        atualNumero = address.getNumber();
                        atualComplemento = address.getComplement();
                        atualRotulo = address.getLabel();

                        ((EditText) getView().findViewById(R.id.atualizarDestinatario)).setText(atualDestinatario);
                        ((EditText) getView().findViewById(R.id.atualizarRua)).setText(atualRua);
                        ((EditText) getView().findViewById(R.id.atualizarNumero)).setText(String.valueOf(atualNumero));
                        ((EditText) getView().findViewById(R.id.atualizarComplemento)).setText(atualComplemento);
                        ((EditText) getView().findViewById(R.id.atualizarRotulo)).setText(atualRotulo);
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