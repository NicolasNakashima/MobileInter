package com.example.khiata.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khiata.R;
import com.example.khiata.adapters.AdapterEnderecosUsuario;
import com.example.khiata.adapters.AdapterSelecaoEnderecosPagamento;
import com.example.khiata.apis.AddressApi;
import com.example.khiata.apis.UserApi;
import com.example.khiata.classes.MainActivity;
import com.example.khiata.classes.TratamentoErros;
import com.example.khiata.classes.tela_carrinho;
import com.example.khiata.classes.tela_inicial;
import com.example.khiata.models.Address;
import com.example.khiata.models.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_tela_selecao_endereco_pagamento#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_tela_selecao_endereco_pagamento extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_tela_selecao_endereco_pagamento() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_tela_selecao_endereco_pagamento.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_tela_selecao_endereco_pagamento newInstance(String param1, String param2) {
        fragment_tela_selecao_endereco_pagamento fragment = new fragment_tela_selecao_endereco_pagamento();
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

    ImageView voltar_carrinho;
    RecyclerView lista_enderecos_pagamento;
    List<Address> enderecos = new ArrayList();
    private Retrofit retrofit;
    int userId;
    TratamentoErros tratamentoErros = new TratamentoErros();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tela_selecao_endereco_pagamento, container, false);

        //Botao para voltar para tela do carrinho
        voltar_carrinho = view.findViewById(R.id.voltar_carrinho);
        voltar_carrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), tela_carrinho.class);
                startActivity(intent);
            }
        });

        // Definir lista de endereços
        lista_enderecos_pagamento = view.findViewById(R.id.lista_enderecos_pagamento);
        lista_enderecos_pagamento.setLayoutManager(new LinearLayoutManager(getContext()));
        //Carregar endereços do usuário
        buscarIdDoUsuario(FirebaseAuth.getInstance().getCurrentUser().getEmail());


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
                pegarEnderecosDoUsuario(userId);
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Dialog dialog = new Dialog(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                msgPopup.setText("Erro: " + tratamentoErros.tratandoErroThrowable(throwable));
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

    //Método para buscar os endereços do usuário
    private void pegarEnderecosDoUsuario(int userId){
        String API_BASE_URL = "https://apikhiata.onrender.com/";
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AddressApi addressApi = retrofit.create(AddressApi.class);
        Call<ArrayList<Address>> call = addressApi.buscarEnderecosUsuario(userId);
        call.enqueue(new Callback<ArrayList<Address>>() {
            @Override
            public void onResponse(Call<ArrayList<Address>> call, Response<ArrayList<Address>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Address> listaDeEnderecos = response.body();
                    if (listaDeEnderecos != null && !listaDeEnderecos.isEmpty()) {
                        enderecos.clear();

                        //Filtrar apenas endereços ativos
                        for(Address address : listaDeEnderecos){
                            if(address.isDeactivate()){
                                enderecos.add(address);
                            }
                        }
                        if(!enderecos.isEmpty()){
                            AdapterSelecaoEnderecosPagamento adapter = new AdapterSelecaoEnderecosPagamento(getActivity(), enderecos);
                            lista_enderecos_pagamento.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } else{
                            Dialog dialog = new Dialog(getActivity());
                            LayoutInflater inflater = getLayoutInflater();
                            View popup_opcao = inflater.inflate(R.layout.popup_opcao, null);

                            TextView msgPopup = popup_opcao.findViewById(R.id.msg_popup);
                            msgPopup.setText("Você não possuí endereços ativos.");
                            ImageView imgPopup = popup_opcao.findViewById(R.id.img_popup);
                            imgPopup.setImageResource(R.drawable.icon_pop_alert);
                            Button btn_seguir = popup_opcao.findViewById(R.id.btn_seguir);
                            btn_seguir.setText("Endereços");
                            btn_seguir.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.frame_conteudo, new fragment_tela_enderecos());
                                    transaction.commit();
                                    dialog.cancel();
                                }
                            });
                            Button btn_cancelar = popup_opcao.findViewById(R.id.btn_cancelar);
                            btn_cancelar.setText("Cancelar");
                            btn_cancelar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), tela_carrinho.class);
                                    startActivity(intent);
                                    dialog.cancel();
                                }
                            });

                            dialog.setContentView(popup_opcao);
                            dialog.setCancelable(true);
                            dialog.show();
                        }
                    } else {
                        Dialog dialog = new Dialog(getActivity());
                        LayoutInflater inflater = getLayoutInflater();
                        View popup_opcao = inflater.inflate(R.layout.popup_opcao, null);

                        TextView msgPopup = popup_opcao.findViewById(R.id.msg_popup);
                        msgPopup.setText("Endereço não encontrado. Cadastre um endereço para seguir com a compra.");
                        ImageView imgPopup = popup_opcao.findViewById(R.id.img_popup);
                        imgPopup.setImageResource(R.drawable.icon_pop_alert);
                        Button btn_seguir = popup_opcao.findViewById(R.id.btn_seguir);
                        btn_seguir.setText("Cadastrar");
                        btn_seguir.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.frame_conteudo, new fragment_tela_adicionar_endereco());
                                transaction.commit();
                                dialog.cancel();
                            }
                        });
                        Button btn_cancelar = popup_opcao.findViewById(R.id.btn_cancelar);
                        btn_cancelar.setText("Cancelar");
                        btn_cancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), tela_carrinho.class);
                                startActivity(intent);
                                dialog.cancel();
                            }
                        });

                        dialog.setContentView(popup_opcao);
                        dialog.setCancelable(true);
                        dialog.show();
                    }
                } else {
                    Dialog dialog = new Dialog(getActivity());
                    LayoutInflater inflater = getLayoutInflater();
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Falha ao carregar endereços, tente novamente mais tarde.");
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
            public void onFailure(Call<ArrayList<Address>> call, Throwable throwable) {
                Dialog dialog = new Dialog(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                msgPopup.setText("Erro: " + tratamentoErros.tratandoErroThrowable(throwable));
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