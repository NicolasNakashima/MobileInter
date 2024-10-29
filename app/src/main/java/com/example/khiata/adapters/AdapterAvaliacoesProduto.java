package com.example.khiata.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khiata.R;
import com.example.khiata.models.Avaliation;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class AdapterAvaliacoesProduto extends RecyclerView.Adapter<AdapterAvaliacoesProduto.MeuViewHolder> {
    private List<Avaliation> avaliacoes = new ArrayList();
    private Context context;

    public AdapterAvaliacoesProduto(Context context, List<Avaliation> avaliacoes) {
        this.context = context;
        this.avaliacoes = avaliacoes;
    }
    private Retrofit retrofit;
    @NonNull
    @Override
    public AdapterAvaliacoesProduto.MeuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Carregar o tempplate de visualização
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_avaliacao_produto, parent, false);

        //Criar o ViewHolder para carregar os dados
        return new AdapterAvaliacoesProduto.MeuViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAvaliacoesProduto.MeuViewHolder holder, int position) {
        TextView nome_usuario = holder.nome_usuario;
        RatingBar avaliacao_usuario = holder.avaliacao_usuario;
        TextView comentario_usuario = holder.comentario_usuario;
    }

    @Override
    public int getItemCount() {return avaliacoes.size();}

    public class MeuViewHolder extends RecyclerView.ViewHolder {
        TextView nome_usuario, comentario_usuario;
        RatingBar avaliacao_usuario;

        public MeuViewHolder(@NonNull View itemView) {
            super(itemView);
            nome_usuario = itemView.findViewById(R.id.nome_usuario);
            avaliacao_usuario = itemView.findViewById(R.id.avaliacao_usuario);
            comentario_usuario = itemView.findViewById(R.id.comentario_usuario);
        }
    }
}
