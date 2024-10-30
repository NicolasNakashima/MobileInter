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
import com.example.khiata.models.Produto;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class AdapterProdutosRecomendados extends RecyclerView.Adapter<AdapterProdutosRecomendados.MeuViewHolder> {
    private List<Produto> produtos = new ArrayList();
    private Context context;

    public AdapterProdutosRecomendados(Context context, List<Produto> produtos) {
        this.context = context;
        this.produtos = produtos;
    }
    private Retrofit retrofit;

    @NonNull
    @Override
    public AdapterProdutosRecomendados.MeuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Carregar o tempplate de visualização
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produto_recomendado, parent, false);

        //Criar o ViewHolder para carregar os dados
        return new MeuViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProdutosRecomendados.MeuViewHolder holder, int position) {
        TextView titulo_produto = holder.titulo_produto;
        TextView preco_produto = holder.preco_produto;

        titulo_produto.setText(produtos.get(position).getTitulo());
        preco_produto.setText("R$ " + String.valueOf(produtos.get(position).getPreco()));

    }

    @Override
    public int getItemCount() {return produtos.size();}

    public class MeuViewHolder extends RecyclerView.ViewHolder{
        TextView titulo_produto, preco_produto;
        ImageView img_produto;

        public MeuViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo_produto = itemView.findViewById(R.id.cart_id);
            preco_produto = itemView.findViewById(R.id.preco_produto);
            img_produto = itemView.findViewById(R.id.img_produto);
        }
    }
}
