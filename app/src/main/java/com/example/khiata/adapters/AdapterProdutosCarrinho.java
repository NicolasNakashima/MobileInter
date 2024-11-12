package com.example.khiata.adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.khiata.R;
import com.example.khiata.apis.CartApi;
import com.example.khiata.apis.ProductApi;
import com.example.khiata.apis.UserApi;
import com.example.khiata.classes.MainActivity;
import com.example.khiata.classes.TratamentoErros;
import com.example.khiata.classes.tela_carrinho;
import com.example.khiata.fragments.fragment_tela_produto;
import com.example.khiata.models.Product;
import com.example.khiata.models.User;
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

public class AdapterProdutosCarrinho extends RecyclerView.Adapter<AdapterProdutosCarrinho.MeuViewHolder> {

    private List<Product> produtos = new ArrayList();

    private Context context;
    private Activity activity;

    public AdapterProdutosCarrinho(Context context, List<Product> produtos, Activity activity) {
        this.context = context;
        this.produtos = produtos;
        this.activity = activity;
    }

    private Retrofit retrofit;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    String userCPF;
    TratamentoErros tratamentoErros = new TratamentoErros();

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
        ImageView btn_remover_carrinho = holder.btn_remover_carrinho;
        TextView preco_produto = holder.preco_produto;
        TextView vendedor_produto = holder.vendedor_produto;
        TextView titulo_produto = holder.titulo_produto;
        TextView tamanho_produto = holder.tamanho_produto;
        TextView quant_produto = holder.quant_produto;

        Product produto = produtos.get(position);

        preco_produto.setText("R$" + String.valueOf(produto.getPrice()));
        vendedor_produto.setText("Vendido por " + produto.getDressMarkerName());
        titulo_produto.setText(produto.getName());
        tamanho_produto.setText("Tamanho: " + produto.getSize());
        quant_produto.setText("Quant: " + String.valueOf(produto.getQuantity()));

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
                //Envia os dados para a tela do produto
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

        //Botão para remover o item do carrinho
        btn_remover_carrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    buscarCPFDoUsuario(FirebaseAuth.getInstance().getCurrentUser().getEmail(), produtos.get(adapterPosition).getName(), adapterPosition, userCPF);
                }
            }
        });
    }

    private void removerProdutoCarrinho(String productName, int position, String userCPf) {
        String API_BASE_URL = "https://api-khiata-feira.onrender.com/";
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CartApi cartApi = retrofit.create(CartApi.class);
        Call<String> call = cartApi.deleteCart(userCPf, productName);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) { // Verifica sucesso e código HTTP
                    // Somente remove e notifica após a confirmação de sucesso da API
                    produtos.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, produtos.size());

                    mostrarDialogo("Produto removido com sucesso", R.drawable.icon_pop_sucesso);
                    Log.d("Sucesso", "Exclusão concluída");
                } else {
                    Log.e("Erro", "Falha na exclusão: " + response.message());
                    mostrarDialogo("Falha ao remover produto", R.drawable.icon_pop_alert);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                mostrarDialogo("Erro: " + tratamentoErros.tratandoErroThrowable(throwable), R.drawable.icon_pop_alert);
            }
        });
    }

    private void buscarCPFDoUsuario(String userEmail, String productName, int position, String userCPf) {
        String API_BASE_URL = "https://apikhiata.onrender.com/";
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi userApi = retrofit.create(UserApi.class);
        Call<User> call = userApi.buscarUsuarioPorEmail(userEmail);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User userResponse = response.body();
                    userCPF= userResponse.getCpf();
                    removerProdutoCarrinho(productName, position, userCPF);
                } else {
                    Dialog dialog = new Dialog(context);

                    LayoutInflater inflater = LayoutInflater.from(context);
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Erro: "+tratamentoErros.tratandoErroApi(response));
                    ImageView imgPopup = popupView.findViewById(R.id.img_popup);
                    imgPopup.setImageResource(R.drawable.icon_pop_alert);
                    Button btnPopup = popupView.findViewById(R.id.btn_popup);
                    btnPopup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });

                    dialog.setContentView(popupView);
                    dialog.setCancelable(true);
                    dialog.show();
                    Log.e("API Error", "Response code: " + response.code() + " | Error body: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Dialog dialog = new Dialog(context);

                LayoutInflater inflater = LayoutInflater.from(context);
                View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                msgPopup.setText("Erro: " + tratamentoErros.tratandoErroThrowable(throwable));
                ImageView imgPopup = popupView.findViewById(R.id.img_popup);
                imgPopup.setImageResource(R.drawable.icon_pop_alert);
                Button btnPopup = popupView.findViewById(R.id.btn_popup);
                btnPopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                dialog.setContentView(popupView);
                dialog.setCancelable(true);
                dialog.show();
            }
        });
    }

    // Método para exibir o diálogo de mensagem
    private void mostrarDialogo(String mensagem, int icone) {
        Log.e("Mensagem Carrinho", mensagem);
    }

    @Override
    public int getItemCount() {return produtos.size();}

    public class MeuViewHolder extends RecyclerView.ViewHolder {
        ImageView img_produto, btn_remover_carrinho;
        TextView preco_produto, vendedor_produto, titulo_produto, tamanho_produto, quant_produto;

        public MeuViewHolder(View view) {
            super(view);
            img_produto = view.findViewById(R.id.img_produto);
            preco_produto = view.findViewById(R.id.preco_produto);
            vendedor_produto = view.findViewById(R.id.vendedor_produto);
            titulo_produto = view.findViewById(R.id.cart_id);
            tamanho_produto = view.findViewById(R.id.tamanho_produto);
            quant_produto = view.findViewById(R.id.quant_produto);
            btn_remover_carrinho = view.findViewById(R.id.btn_remover_carrinho);
        }
    }
}
