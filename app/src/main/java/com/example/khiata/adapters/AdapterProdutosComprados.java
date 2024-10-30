package com.example.khiata.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khiata.R;
import com.example.khiata.models.Historic;
import com.example.khiata.models.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class AdapterProdutosComprados extends RecyclerView.Adapter<AdapterProdutosComprados.MeuViewHolder> {
    private List<Historic> pedidos = new ArrayList();
    private Context context;

    public AdapterProdutosComprados(Context context, List<Historic> pedidos) {
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
        TextView cart_id = holder.cart_id;
        TextView status_pedido = holder.status_pedido;
        TextView total_pedido = holder.total_pedido;
        TextView forma_pagamento = holder.forma_pagamento;
        TextView data_pedido = holder.data_pedido;

        Historic pedido = pedidos.get(position);
        cart_id.setText("ID: " + pedido.getCart_id());
        status_pedido.setText("Status: " + pedido.getStatus());
        total_pedido.setText("Total: R$ " + pedido.getFinalValue());
        forma_pagamento.setText("Forma de pagamento: " + pedido.getPaymentmethod());
        data_pedido.setText("Data: " + pedido.getOrderDate());
    }

    @Override
    public int getItemCount() {return pedidos.size();}

    public class MeuViewHolder extends RecyclerView.ViewHolder {
        TextView cart_id, status_pedido, total_pedido, forma_pagamento, data_pedido;
        public MeuViewHolder(@NonNull View itemView) {
            super(itemView);
            cart_id = itemView.findViewById(R.id.cart_id);
            status_pedido = itemView.findViewById(R.id.status_pedido);
            total_pedido = itemView.findViewById(R.id.total_pedido);
            forma_pagamento = itemView.findViewById(R.id.forma_pagamento);
            data_pedido = itemView.findViewById(R.id.data_pedido);
        }
    }
}
