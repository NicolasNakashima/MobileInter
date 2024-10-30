package com.example.khiata.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khiata.R;
import com.example.khiata.models.Address;
import com.example.khiata.models.Avaliation;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class AdapterSelecaoEnderecosPagamento extends RecyclerView.Adapter<AdapterSelecaoEnderecosPagamento.MeuViewHolder> {
    private List<Address> enderecos = new ArrayList();
    private Context context;

    public AdapterSelecaoEnderecosPagamento(Context context, List<Address> enderecos) {
        this.context = context;
        this.enderecos = enderecos;
    }
    private Retrofit retrofit;
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

        rotulo_endereco.setText(enderecos.get(position).getLabel());
        street_endereco.setText(enderecos.get(position).getStreet() + " - " + enderecos.get(position).getNumber() + " - " + enderecos.get(position).getCep());
        complement_endereco.setText(enderecos.get(position).getComplement());
        destinatario_endereco.setText(enderecos.get(position).getRecipient());
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
