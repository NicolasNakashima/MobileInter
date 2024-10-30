package com.example.khiata.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khiata.R;
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

        img_curso.setImageURI(Uri.parse(cursos.get(position).getThumbnailUrl()));
        titulo_curso.setText(cursos.get(position).getTitle());
        tempo_curso.setText(cursos.get(position).getDuration());
        avaliacao_curso.setRating((float) cursos.get(position).getAvaliation());
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
