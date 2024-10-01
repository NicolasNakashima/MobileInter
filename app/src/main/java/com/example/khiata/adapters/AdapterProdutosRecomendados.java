package com.example.khiata.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khiata.R;
import com.example.khiata.models.Produto;

import java.util.ArrayList;
import java.util.List;

public class AdapterProdutosRecomendados extends RecyclerView.Adapter<AdapterProdutosRecomendados.MeuViewHolder> {
    private List<Produto> produtos = new ArrayList();

    public AdapterProdutosRecomendados(List<Produto> produtos) {
        this.produtos = produtos;
    }

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

    }

    @Override
    public int getItemCount() {return produtos.size();}

    public class MeuViewHolder extends RecyclerView.ViewHolder{
        TextView titulo_produto, preco_produto;

        public MeuViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo_produto = itemView.findViewById(R.id.titulo_produto);
            preco_produto = itemView.findViewById(R.id.preco_produto);
        }
    }
}
