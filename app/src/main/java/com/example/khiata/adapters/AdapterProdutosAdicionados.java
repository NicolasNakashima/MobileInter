package com.example.khiata.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.khiata.R;
import com.example.khiata.apis.AddressApi;
import com.example.khiata.apis.ProductApi;
import com.example.khiata.classes.tela_inicial;
import com.example.khiata.models.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdapterProdutosAdicionados extends RecyclerView.Adapter<AdapterProdutosAdicionados.MeuViewHolder> {

    private List<Product> produtos = new ArrayList();
    private Context context;

    public AdapterProdutosAdicionados(Context context, List<Product> produtos) {
        this.context = context;
        this.produtos = produtos;
    }
    private Retrofit retrofit;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    @NonNull
    @Override
    public AdapterProdutosAdicionados.MeuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Carregar o tempplate de visualização
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_endereco, parent, false);

        //Criar o ViewHolder para carregar os dados
        return new AdapterProdutosAdicionados.MeuViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProdutosAdicionados.MeuViewHolder holder, int position) {
        ImageView img_produto = holder.img_produto;
        ImageView btn_excluir_produto = holder.btn_excluir_produto;
        TextView titulo_produto = holder.titulo_produto;
        TextView preco_produto = holder.preco_produto;

        // Defina os dados do produto para exibir no item
        Product produto = produtos.get(position);
        titulo_produto.setText(produto.getName());
        preco_produto.setText("R$ " + String.valueOf(produto.getPrice()));

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

        // Listener para excluir o produto
        btn_excluir_produto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View popup_opcao = inflater.inflate(R.layout.popup_opcao, null);

                TextView msgPopup = popup_opcao.findViewById(R.id.msg_popup);
                msgPopup.setText("Você está prestes a excluir o produto \n" + produto.getName() + "\n. Deseja prosseguir?");
                ImageView imgPopup = popup_opcao.findViewById(R.id.img_popup);
                imgPopup.setImageResource(R.drawable.icon_pop_delete);
                Button btn_seguir = popup_opcao.findViewById(R.id.btn_seguir);
                btn_seguir.setText("Excluir");
                btn_seguir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int adapterPosition = holder.getAdapterPosition();
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            deletarProduto(produtos.get(adapterPosition).getId(), adapterPosition);
                        }
                        dialog.dismiss();
                    }
                });
                Button btn_cancelar = popup_opcao.findViewById(R.id.btn_cancelar);
                btn_cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.setContentView(popup_opcao);
                dialog.setCancelable(true);
                dialog.show();
            }
        });
    }

    //Método responsável por deletar um produto
    private void deletarProduto(int productId, int position) {
        String API_BASE_URL = "https://interdisciplinarr.onrender.com/";
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ProductApi productApi = retrofit.create(ProductApi.class);
        Call<String> call = productApi.deleteProduct(productId);
        Log.e("produtoId", String.valueOf(productId));
        Log.e("position", String.valueOf(position));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    produtos.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, produtos.size());

                    Toast.makeText(context, "Produto excluído com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("Erro", response.message());
                    Toast.makeText(context, "Falha ao excluir o produto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {return produtos.size();}

    public class MeuViewHolder extends RecyclerView.ViewHolder {
        ImageView img_produto, btn_excluir_produto;
        TextView titulo_produto, preco_produto;
        public MeuViewHolder(@NonNull View itemView) {
            super(itemView);
            img_produto = itemView.findViewById(R.id.img_produto);
            btn_excluir_produto = itemView.findViewById(R.id.btn_excluir_produto);
            titulo_produto = itemView.findViewById(R.id.titulo_produto);
            preco_produto = itemView.findViewById(R.id.preco_produto);
        }
    }
}
