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
import com.example.khiata.models.Course;
import com.example.khiata.models.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class AdapterProdutosComprados extends RecyclerView.Adapter<AdapterProdutosComprados.MeuViewHolder> {
    private List<Product> produtos = new ArrayList();
    private Context context;

    public AdapterProdutosComprados(Context context, List<Product> produtos) {
        this.context = context;
        this.produtos = produtos;
    }
    private Retrofit retrofit;
    @NonNull
    @Override
    public AdapterProdutosComprados.MeuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Carregar o tempplate de visualização
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_costureira, parent, false);

        //Criar o ViewHolder para carregar os dados
        return new AdapterProdutosComprados.MeuViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProdutosComprados.MeuViewHolder holder, int position) {
        ImageView img_produto = holder.img_produto;
        TextView titulo_produto = holder.titulo_produto;
        TextView total_compra = holder.total_compra;
        TextView status_compra = holder.status_compra;
        TextView btn_adicionar_carrinho = holder.btn_adicionar_carrinho;
    }

    @Override
    public int getItemCount() {return produtos.size();}

    public class MeuViewHolder extends RecyclerView.ViewHolder {
        ImageView img_produto;
        TextView titulo_produto, total_compra, status_compra, btn_adicionar_carrinho;

        public MeuViewHolder(@NonNull View itemView) {
            super(itemView);
            img_produto = itemView.findViewById(R.id.img_produto);
            titulo_produto = itemView.findViewById(R.id.titulo_produto);
            total_compra = itemView.findViewById(R.id.total_compra);
            status_compra = itemView.findViewById(R.id.status_compra);
            btn_adicionar_carrinho = itemView.findViewById(R.id.btn_adicionar_carrinho);
        }
    }
}
