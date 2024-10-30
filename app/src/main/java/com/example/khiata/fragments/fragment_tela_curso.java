package com.example.khiata.fragments;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.khiata.R;
import com.example.khiata.adapters.AdapterAvaliacoesCostureira;
import com.example.khiata.adapters.AdapterAvaliacoesCurso;
import com.example.khiata.models.Avaliation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_tela_curso#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_tela_curso extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_tela_curso() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_tela_curso.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_tela_curso newInstance(String param1, String param2) {
        fragment_tela_curso fragment = new fragment_tela_curso();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    ImageView voltar_cursos;
    TextView titulo_curso, duracao_curso, categoria_curso;
    RatingBar avaliacao_curso;
    WebView youtubeWebView;
    Button btn_adicionar_avaliacao_curso;
    RecyclerView lista_avaliacoes_curso;
    List<Avaliation> avaliacoes = new ArrayList();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tela_curso, container, false);

        //Botao para voltar para a tela de Cursos
        voltar_cursos = view.findViewById(R.id.voltar_cursos);
        voltar_cursos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_cursos());
                transaction.commit();
            }
        });


        //Botão para adicionar avaliação do curso
        btn_adicionar_avaliacao_curso = view.findViewById(R.id.btn_adicionar_avaliacao_curso);
        btn_adicionar_avaliacao_curso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                View pop_avaliacao = inflater.inflate(R.layout.pop_avaliacao, null);

                RatingBar avaliation_bar = pop_avaliacao.findViewById(R.id.avaliation_bar);
                EditText edit_comment = pop_avaliacao.findViewById(R.id.edit_comment);
                Button btn_enviar = pop_avaliacao.findViewById(R.id.btn_enviar);
                Button btn_cancelar = pop_avaliacao.findViewById(R.id.btn_cancelar);
                btn_cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                dialog.setContentView(pop_avaliacao);
                dialog.setCancelable(true);
                dialog.show();
            }
        });

        //Pegando informações do curso
        Bundle bundle = getArguments();
        if(bundle != null){
            String titulo_curso_txt = bundle.getString("titulo_curso");
            String duracao_curso_txt = bundle.getString("duracao_curso");
            String categoria_curso_txt = bundle.getString("categoria_curso");
            String video_url_txt = bundle.getString("video_url");
            float avaliacao_curso_txt = bundle.getFloat("avaliacao_curso");
            if (titulo_curso_txt != null && duracao_curso_txt != null && categoria_curso_txt != null && video_url_txt != null) {
                titulo_curso = view.findViewById(R.id.titulo_curso);
                titulo_curso.setText(titulo_curso_txt);
                duracao_curso = view.findViewById(R.id.duracao_curso);
                duracao_curso.setText("Duração: " + duracao_curso_txt);
                categoria_curso = view.findViewById(R.id.categoria_curso);
                categoria_curso.setText("Categoria: " + categoria_curso_txt);
                avaliacao_curso = view.findViewById(R.id.avaliacao_curso);
                avaliacao_curso.setRating(avaliacao_curso_txt);
                //Configurando o WebView para o video do YouTube
                youtubeWebView = view.findViewById(R.id.youtubeWebView);
                youtubeWebView.setWebViewClient(new WebViewClient());
                WebSettings webSettings = youtubeWebView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                String videoId = video_url_txt.substring(video_url_txt.indexOf("v=") + 2, video_url_txt.indexOf("v=") + 13);
                String embedUrl = "https://www.youtube.com/embed/" + videoId + "?rel=0&showinfo=0&controls=1&modestbranding=1";
                youtubeWebView.loadUrl(embedUrl);
            }
        }

        //Definindo as avaliações do curso
        lista_avaliacoes_curso = view.findViewById(R.id.lista_avaliacoes_curso);
        avaliacoes.add(new Avaliation("Joaquim", "Serviço muito bom, comprarei novamente", 4));
        AdapterAvaliacoesCurso adapter = new AdapterAvaliacoesCurso(getActivity(), avaliacoes);
        lista_avaliacoes_curso.setAdapter(adapter);
        lista_avaliacoes_curso.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        return view;
    }
}