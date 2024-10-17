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

public class AdapterProdutosPesquisados extends RecyclerView.Adapter<AdapterProdutosPesquisados.MeuViewHolder> {

    private List<Product> produtos = new ArrayList();
    private Context context;

    public AdapterProdutosPesquisados(Context context, List<Product> produtos) {
        this.context = context;
        this.produtos = produtos;
    }
    private Retrofit retrofit;
    @NonNull
    @Override
    public AdapterProdutosPesquisados.MeuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Carregar o tempplate de visualização
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_endereco, parent, false);

        //Criar o ViewHolder para carregar os dados
        return new AdapterProdutosPesquisados.MeuViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProdutosPesquisados.MeuViewHolder holder, int position) {
        ImageView img_produto = holder.img_produto;
        TextView titulo_produto = holder.titulo_produto;
        TextView vendedor_produto = holder.vendedor_produto;
        TextView preco_produto = holder.preco_produto;

        Product product = produtos.get(position);
        titulo_produto.setText(product.getName());
        vendedor_produto.setText("Vendido por "+product.getDressMarkerName());
        preco_produto.setText("R$ "+product.getPrice());
    }

    @Override
    public int getItemCount() {return produtos.size();}

    public class MeuViewHolder extends RecyclerView.ViewHolder {
        TextView titulo_produto, vendedor_produto, preco_produto;
        ImageView img_produto;
        public MeuViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo_produto = itemView.findViewById(R.id.titulo_produto);
            vendedor_produto = itemView.findViewById(R.id.vendedor_produto);
            preco_produto = itemView.findViewById(R.id.preco_produto);
            img_produto = itemView.findViewById(R.id.img_produto);
        }
    }
}
