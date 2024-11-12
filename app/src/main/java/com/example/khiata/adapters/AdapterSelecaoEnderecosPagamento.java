package com.example.khiata.adapters;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khiata.R;
import com.example.khiata.apis.UserApi;
import com.example.khiata.classes.TratamentoErros;
import com.example.khiata.classes.tela_login;
import com.example.khiata.fragments.fragment_tela_dados_compra_produto;
import com.example.khiata.models.Address;
import com.example.khiata.models.Avaliation;
import com.example.khiata.models.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdapterSelecaoEnderecosPagamento extends RecyclerView.Adapter<AdapterSelecaoEnderecosPagamento.MeuViewHolder> {
    private List<Address> enderecos = new ArrayList();
    private Context context;

    public AdapterSelecaoEnderecosPagamento(Context context, List<Address> enderecos) {
        this.context = context;
        this.enderecos = enderecos;
    }
    private Retrofit retrofit;
    TratamentoErros tratamentoErros = new TratamentoErros();

    @NonNull
    @Override
    public AdapterSelecaoEnderecosPagamento.MeuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Carregar o tempplate de visualização
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_endereco_pagamento, parent, false);

        //Criar o ViewHolder para carregar os dados
        return new AdapterSelecaoEnderecosPagamento.MeuViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSelecaoEnderecosPagamento.MeuViewHolder holder, int position) {
        TextView rotulo_endereco = holder.rotulo_endereco;
        TextView destinatario_endereco = holder.destinatario_endereco;
        TextView street_endereco = holder.street_endereco;
        TextView complement_endereco = holder.complement_endereco;

        Address endereco = enderecos.get(position);

        //Buscando e definindo nome do destinatário
        buscarNomeDoUsuario(FirebaseAuth.getInstance().getCurrentUser().getEmail(), destinatario_endereco);

        rotulo_endereco.setText(endereco.getLabel());
        street_endereco.setText(endereco.getStreet() + " - " + endereco.getNumber() + " - " + endereco.getCep());
        complement_endereco.setText(endereco.getComplement());

        //Ir para a tela de confirmação de dados
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_tela_dados_compra_produto telaDadosCompraProduto = new fragment_tela_dados_compra_produto();
                //Passando dados para o fragment
                Bundle bundle = new Bundle();
                bundle.putString("street_endereco", endereco.getStreet());
                bundle.putString("number_endereco", String.valueOf(endereco.getNumber()));
                bundle.putString("cep_endereco", endereco.getCep());
                bundle.putString("complement_endereco", endereco.getComplement());
                telaDadosCompraProduto.setArguments(bundle);

                FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, telaDadosCompraProduto);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    //Método responsável por buscar o nome do usuário
    private void buscarNomeDoUsuario(String userEmail, TextView destinatario_endereco) {
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
                if (response.isSuccessful() && response.body() != null) {
                    User userResponse = response.body();
                    destinatario_endereco.setText(userResponse.getName());
                } else {
                    Dialog dialog = new Dialog(context);

                    LayoutInflater inflater = LayoutInflater.from(context);
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Erro: " + tratamentoErros.tratandoErroApi(response));
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
                    Log.e("Error", "Erro na resposta da API: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Dialog dialog = new Dialog(context);

                LayoutInflater inflater = LayoutInflater.from(context);
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

    @Override
    public int getItemCount() {return enderecos.size();}

    public class MeuViewHolder extends RecyclerView.ViewHolder {
        TextView rotulo_endereco, destinatario_endereco, street_endereco, complement_endereco;

        public MeuViewHolder(@NonNull View itemView) {
            super(itemView);
            rotulo_endereco = itemView.findViewById(R.id.rotulo_endereco);
            destinatario_endereco = itemView.findViewById(R.id.destinatario_endereco);
            street_endereco = itemView.findViewById(R.id.street_endereco);
            complement_endereco = itemView.findViewById(R.id.complement_endereco);
        }
    }
}
