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
import com.example.khiata.models.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class AdapterProdutosFavoritos extends RecyclerView.Adapter<AdapterProdutosFavoritos.MeuViewHolder> {
    private List<Product> produtos = new ArrayList();
    private Context context;

    public AdapterProdutosFavoritos(Context context, List<Product> produtos) {
        this.context = context;
        this.produtos = produtos;
    }
    private Retrofit retrofit;

    @NonNull
    @Override
    public AdapterProdutosFavoritos.MeuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Carregar o tempplate de visualização
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produto_favoritado, parent, false);

        //Criar o ViewHolder para carregar os dados
        return new AdapterProdutosFavoritos.MeuViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProdutosFavoritos.MeuViewHolder holder, int position) {
        ImageView img_produto = holder.img_produto;
        ImageView btn_favorito = holder.btn_favorito;
        TextView preco_produto = holder.preco_produto;
        TextView titulo_produto = holder.titulo_produto;
        TextView vendedor_produto = holder.vendedor_produto;
    }

    @Override
    public int getItemCount() {return produtos.size();}

    public class MeuViewHolder extends RecyclerView.ViewHolder {
        ImageView img_produto, btn_favorito;
        TextView preco_produto, titulo_produto, vendedor_produto;

        public MeuViewHolder(@NonNull View itemView) {
            super(itemView);
            img_produto = itemView.findViewById(R.id.img_produto);
            btn_favorito = itemView.findViewById(R.id.btn_favorito);
            preco_produto = itemView.findViewById(R.id.preco_produto);
            titulo_produto = itemView.findViewById(R.id.titulo_produto);
            vendedor_produto = itemView.findViewById(R.id.vendedor_produto);
        }
    }
}
