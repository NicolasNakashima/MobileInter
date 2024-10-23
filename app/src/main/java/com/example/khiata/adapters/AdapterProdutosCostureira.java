package com.example.khiata.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.khiata.R;
import com.example.khiata.models.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    @NonNull
    @Override
    public AdapterProdutosCostureira.MeuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Carregar o tempplate de visualização
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produto_costureira, parent, false);

        //Criar o ViewHolder para carregar os dados
        return new AdapterProdutosCostureira.MeuViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProdutosCostureira.MeuViewHolder holder, int position) {
        ImageView img_produto = holder.img_produto;
        RatingBar avaliacao_produto_costureira = holder.avaliacao_produto_costureira;
        TextView titulo_produto = holder.titulo_produto;
        TextView preco_produto = holder.preco_produto;

        Product produto = produtos.get(position);
        titulo_produto.setText(produto.getName());
        preco_produto.setText("R$ " + String.valueOf(produto.getPrice()));
        avaliacao_produto_costureira.setRating((float) produto.getAvaliation());

        StorageReference profileRef = storageRef.child("khiata_produtos/"+produto.getImageUrl()+".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).circleCrop().into(img_produto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "Falha ao obter URL da imagem"+ e.getMessage());
                img_produto.setImageResource(R.drawable.add_img);
            }
        });
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
