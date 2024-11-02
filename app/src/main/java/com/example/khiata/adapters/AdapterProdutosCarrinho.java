package com.example.khiata.adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.khiata.R;
import com.example.khiata.classes.MainActivity;
import com.example.khiata.fragments.fragment_tela_produto;
import com.example.khiata.models.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

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

        StorageReference profileRef = storageRef.child("khiata_produtos/"+produto.getImageUrl()+".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(img_produto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "Falha ao obter URL da imagem"+ e.getMessage());
                img_produto.setImageResource(R.drawable.add_img);
            }
        });

        //Ir para a tela do produto ao clicar num card
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("fragment", "tela_produto");
                intent.putExtra("titulo_produto", produto.getName());
                intent.putExtra("vendedor_produto", produto.getDressMarkerName());
                intent.putExtra("preco_produto", produto.getPrice());
                intent.putExtra("imagem_produto", produto.getImageUrl());
                intent.putExtra("descricao_produto", produto.getDescription());
                intent.putExtra("tamanho_produto", produto.getSize());
                intent.putExtra("avaliacao_produto", (float) produto.getAvaliation());

                // Adiciona a flag para iniciar a atividade corretamente
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
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
