package com.example.khiata.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
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

public class AdapterAvaliacoesCostureira extends RecyclerView.Adapter<AdapterAvaliacoesCostureira.MeuViewHolder> {
    private List<Avaliation> avaliacoes = new ArrayList();
    private Context context;

    public AdapterAvaliacoesCostureira(Context context, List<Avaliation> avaliacoes) {
        this.context = context;
        this.avaliacoes = avaliacoes;
    }
    private Retrofit retrofit;
    @NonNull
    @Override
    public AdapterAvaliacoesCostureira.MeuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Carregar o tempplate de visualização
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_avaliacao_costureira, parent, false);

        //Criar o ViewHolder para carregar os dados
        return new AdapterAvaliacoesCostureira.MeuViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAvaliacoesCostureira.MeuViewHolder holder, int position) {
        TextView nome_usuario = holder.nome_usuario;
        RatingBar avaliacao_usuario = holder.avaliacao_usuario;
        LayerDrawable stars = (LayerDrawable) avaliacao_usuario.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#FAC552"), PorterDuff.Mode.SRC_ATOP);//Define cor da estrela preenchida
        TextView comentario_usuario = holder.comentario_usuario;

        Avaliation avaliacao = avaliacoes.get(position);

        nome_usuario.setText(avaliacao.getUserName());
        avaliacao_usuario.setRating((float) avaliacao.getRating());
        comentario_usuario.setText(avaliacao.getComment());
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
