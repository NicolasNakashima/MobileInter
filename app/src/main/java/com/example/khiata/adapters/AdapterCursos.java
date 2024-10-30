package com.example.khiata.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.khiata.R;
import com.example.khiata.fragments.fragment_tela_produto;
import com.example.khiata.models.Course;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class AdapterCursos extends RecyclerView.Adapter<AdapterCursos.MeuViewHolder> {
    private List<Course> cursos = new ArrayList();
    private Context context;

    public AdapterCursos(Context context, List<Course> cursos) {
        this.context = context;
        this.cursos = cursos;
    }
    private Retrofit retrofit;

    @NonNull
    @Override
    public AdapterCursos.MeuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Carregar o tempplate de visualização
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_curso, parent, false);

        //Criar o ViewHolder para carregar os dados
        return new AdapterCursos.MeuViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCursos.MeuViewHolder holder, int position) {
        ImageView img_curso = holder.img_curso;
        TextView titulo_curso = holder.titulo_curso;
        TextView tempo_curso = holder.tempo_curso;
        RatingBar avaliacao_curso = holder.avaliacao_curso;
        Drawable stars = avaliacao_curso.getProgressDrawable();
        stars.setColorFilter(Color.parseColor("#FAC552"), PorterDuff.Mode.SRC_ATOP);

        Course curso = cursos.get(position);

        Glide.with(context).load(curso.getThumbnailUrl()).into(img_curso);
        titulo_curso.setText(curso.getTitle());
        tempo_curso.setText(curso.getDuration());
        avaliacao_curso.setRating((float) curso.getAvaliation());

        //Ir para a tela do curso ao clicar num card
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_tela_produto telaProdutoFragment = new fragment_tela_produto();

                Bundle bundle = new Bundle();
                bundle.putString("titulo_curso", curso.getTitle());
                bundle.putString("video_url", curso.getVideoUrl());
                bundle.putString("categoria_curso", curso.getCategory());
                bundle.putString("duracao_curso", curso.getDuration());
                bundle.putFloat("avaliacao_curso", (float) curso.getAvaliation());
                telaProdutoFragment.setArguments(bundle);

                FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, telaProdutoFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {return cursos.size();}

    public class MeuViewHolder extends RecyclerView.ViewHolder {
        ImageView img_curso;
        TextView titulo_curso, tempo_curso;
        RatingBar avaliacao_curso;

        public MeuViewHolder(@NonNull View itemView) {
            super(itemView);
            img_curso = itemView.findViewById(R.id.img_curso);
            titulo_curso = itemView.findViewById(R.id.titulo_curso);
            tempo_curso = itemView.findViewById(R.id.tempo_curso);
            avaliacao_curso = itemView.findViewById(R.id.avaliacao_curso);
        }
    }
}
