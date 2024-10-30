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

public class AdapterProdutosCarrinho extends RecyclerView.Adapter<AdapterProdutosCarrinho.MeuViewHolder> {

    private List<Product> produtos = new ArrayList();

    private Context context;

    public AdapterProdutosCarrinho(Context context, List<Product> produtos) {
        this.context = context;
        this.produtos = produtos;
    }

    private Retrofit retrofit;

    @NonNull
    @Override
    public AdapterProdutosCarrinho.MeuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Carregar o tempplate de visualização
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produto_carrinho, parent, false);

        //Criar o ViewHolder para carregar os dados
        return new AdapterProdutosCarrinho.MeuViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProdutosCarrinho.MeuViewHolder holder, int position) {
        ImageView img_produto = holder.img_produto;
        TextView preco_produto = holder.preco_produto;
        TextView vendedor_produto = holder.vendedor_produto;
        TextView titulo_produto = holder.titulo_produto;
        TextView tamanho_produto = holder.tamanho_produto;
        ImageView btn_excluir_item = holder.btn_excluir_item;

        Product produto = produtos.get(position);
        preco_produto.setText("R$" + String.valueOf(produto.getPrice()));
        vendedor_produto.setText("Vendido por " + produto.getDressMarkerName());
        titulo_produto.setText(produto.getName());
        tamanho_produto.setText("Tamanho: " + produto.getSize());
    }

    @Override
    public int getItemCount() {return produtos.size();}

    public class MeuViewHolder extends RecyclerView.ViewHolder {
        ImageView img_produto, btn_excluir_item;
        TextView preco_produto, vendedor_produto, titulo_produto, tamanho_produto;

        public MeuViewHolder(View view) {
            super(view);
            img_produto = view.findViewById(R.id.img_produto);
            btn_excluir_item = view.findViewById(R.id.btn_excluir_item);
            preco_produto = view.findViewById(R.id.preco_produto);
            vendedor_produto = view.findViewById(R.id.vendedor_produto);
            titulo_produto = view.findViewById(R.id.cart_id);
            tamanho_produto = view.findViewById(R.id.tamanho_produto);
        }
    }
}
