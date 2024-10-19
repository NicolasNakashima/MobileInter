package com.example.khiata.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khiata.R;
import com.example.khiata.models.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class AdapterProdutosCostureira extends RecyclerView.Adapter<AdapterProdutosCostureira.MeuViewHolder> {
    private List<Product> produtos = new ArrayList();
    private Context context;

    public AdapterProdutosCostureira(Context context, List<Product> produtos) {
        this.context = context;
        this.produtos = produtos;
    }
    private Retrofit retrofit;

    @NonNull
    @Override
    public AdapterProdutosCostureira.MeuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Carregar o tempplate de visualização
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_costureira, parent, false);

        //Criar o ViewHolder para carregar os dados
        return new AdapterProdutosCostureira.MeuViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProdutosCostureira.MeuViewHolder holder, int position) {
        ImageView img_produto = holder.img_produto;
        RatingBar avaliacao_produto_costureira = holder.avaliacao_produto_costureira;
        TextView titulo_produto = holder.titulo_produto;
        TextView preco_produto = holder.preco_produto;
    }

    @Override
    public int getItemCount() {return produtos.size();}

    public class MeuViewHolder extends RecyclerView.ViewHolder {
        ImageView img_produto;
        RatingBar avaliacao_produto_costureira;
        TextView titulo_produto, preco_produto;

        public MeuViewHolder(@NonNull View itemView) {
            super(itemView);
            img_produto = itemView.findViewById(R.id.img_produto);
            avaliacao_produto_costureira = itemView.findViewById(R.id.avaliacao_produto_costureira);
            titulo_produto = itemView.findViewById(R.id.titulo_produto);
            preco_produto = itemView.findViewById(R.id.preco_produto);
        }
    }
}
