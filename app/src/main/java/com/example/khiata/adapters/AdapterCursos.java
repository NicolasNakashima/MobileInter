package com.example.khiata.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.khiata.R;
import com.example.khiata.apis.UserApi;
import com.example.khiata.classes.tela_login;
import com.example.khiata.fragments.fragment_tela_curso;
import com.example.khiata.models.Course;
import com.example.khiata.models.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdapterCursos extends RecyclerView.Adapter<AdapterCursos.MeuViewHolder> {
    private List<Course> cursos = new ArrayList();
    private Context context;
    private String email_usuario;
    private int premiumStatus;

    public AdapterCursos(Context context, List<Course> cursos, String email_usuario) {
        this.context = context;
        this.cursos = cursos;
        this.email_usuario = email_usuario;
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
        LayerDrawable stars = (LayerDrawable) avaliacao_curso.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#FAC552"), PorterDuff.Mode.SRC_ATOP);//Define a cor da estrela preenchida

        Course curso = cursos.get(position);

        Glide.with(context).load(curso.getThumbnailUrl()).into(img_curso);
        titulo_curso.setText(curso.getTitle());
        tempo_curso.setText(curso.getDuration());
        avaliacao_curso.setRating((float) curso.getAvaliation());

        buscarStatusPremiumDoUsuario(email_usuario);

        //Ir para a tela do curso ao clicar num card
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(premiumStatus == 1) {
                    fragment_tela_curso telaCursoFragment = new fragment_tela_curso();

                    Bundle bundle = new Bundle();
                    bundle.putString("titulo_curso", curso.getTitle());
                    bundle.putString("video_url", curso.getVideoUrl());
                    bundle.putString("categoria_curso", curso.getCategory());
                    bundle.putString("duracao_curso", curso.getDuration());
                    bundle.putFloat("avaliacao_curso", (float) curso.getAvaliation());
                    telaCursoFragment.setArguments(bundle);

                    FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_conteudo, telaCursoFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Dialog dialog = new Dialog(context);
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Sua conta não é premium. Torne-se premum para ter acesso aos cursos.");
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
            }
        });
    }

    //Método para buscar as o status premium do usuario
    private void buscarStatusPremiumDoUsuario(String email_usuario) {
        String API_BASE_URL = "https://apikhiata.onrender.com/";
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi userApi = retrofit.create(UserApi.class);
        Call<User> call = userApi.buscarUsuarioPorEmail(email_usuario);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User userResponse = response.body();
                    premiumStatus = userResponse.getPremiumStatus();
                    Log.d("premiumStatus", String.valueOf(premiumStatus));
                } else {
                    Dialog dialog = new Dialog(context);

                    LayoutInflater inflater = LayoutInflater.from(context);
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Erro:"+response.errorBody());
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
                msgPopup.setText("Erro:"+throwable.getMessage());
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
