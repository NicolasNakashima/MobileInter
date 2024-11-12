package com.example.khiata.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khiata.R;
import com.example.khiata.fragments.fragment_tela_resumo_compra;
import com.example.khiata.models.Order;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class AdapterProdutosComprados extends RecyclerView.Adapter<AdapterProdutosComprados.MeuViewHolder> {
    private List<Order> pedidos = new ArrayList();
    private Context context;

    public AdapterProdutosComprados(Context context, List<Order> pedidos) {
        this.context = context;
        this.pedidos = pedidos;
    }
    private Retrofit retrofit;
    @NonNull
    @Override
    public AdapterProdutosComprados.MeuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Carregar o tempplate de visualização
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produto_comprado, parent, false);

        //Criar o ViewHolder para carregar os dados
        return new AdapterProdutosComprados.MeuViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProdutosComprados.MeuViewHolder holder, int position) {
        TextView id_pedido = holder.id_pedido;
        TextView status_pedido = holder.status_pedido;
        TextView forma_pagamento = holder.forma_pagamento;
        TextView data_pedido = holder.data_pedido;

        Order pedido = pedidos.get(position);
        id_pedido.setText("Cart ID: " + pedido.getCart_id());
        status_pedido.setText(pedido.getStatus());
        forma_pagamento.setText("Forma de pagamento: " + pedido.getPaymentmethod());
        data_pedido.setText("Data: " + pedido.getOrderDate());

        //Botão para ir para tela de resumo de compra ao clicar em card
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_tela_resumo_compra telaResumoCompraFragment = new fragment_tela_resumo_compra();
                //Enviando os dados para o fragment
                Bundle bundle = new Bundle();
                bundle.putString("cart_id", String.valueOf(pedido.getCart_id()));
                bundle.putString("forma_pagamento", pedido.getPaymentmethod());
                bundle.putString("data_pedido", pedido.getOrderDate());
                bundle.putString("status", pedido.getStatus());
                telaResumoCompraFragment.setArguments(bundle);

                FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, telaResumoCompraFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {return pedidos.size();}

    public class MeuViewHolder extends RecyclerView.ViewHolder {
        TextView id_pedido, status_pedido, forma_pagamento, data_pedido;
        public MeuViewHolder(@NonNull View itemView) {
            super(itemView);
            id_pedido = itemView.findViewById(R.id.id_pedido);
            status_pedido = itemView.findViewById(R.id.status_pedido);
            forma_pagamento = itemView.findViewById(R.id.forma_pagamento);
            data_pedido = itemView.findViewById(R.id.data_pedido);
        }
    }
}
