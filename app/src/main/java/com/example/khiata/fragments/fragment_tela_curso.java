package com.example.khiata.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.content.pm.ActivityInfo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.khiata.R;
import com.example.khiata.adapters.AdapterAvaliacoesCostureira;
import com.example.khiata.adapters.AdapterAvaliacoesCurso;
import com.example.khiata.classes.tela_login;
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
                View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                msgPopup.setText("Está funcionalidade estará disponível no futuro.");
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
                LayerDrawable stars = (LayerDrawable) avaliacao_curso.getProgressDrawable();
                stars.getDrawable(2).setColorFilter(Color.parseColor("#FAC552"), PorterDuff.Mode.SRC_ATOP);
                avaliacao_curso.setRating(avaliacao_curso_txt);

                // Configuração do WebView para vídeo do YouTube
                youtubeWebView = view.findViewById(R.id.youtubeWebView);

                // Configurar o WebChromeClient para suporte a tela cheia
                youtubeWebView.setWebChromeClient(new WebChromeClient() {
                    private View customView;
                    private WebChromeClient.CustomViewCallback customViewCallback;
                    private FrameLayout fullscreenContainer;
                    private int originalOrientation;
                    private int originalSystemUiVisibility;

                    @Override
                    public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
                        // Se já estiver no modo de tela cheia, saia primeiro
                        if (customView != null) {
                            callback.onCustomViewHidden();
                            return;
                        }

                        // Salva a configuração original
                        originalSystemUiVisibility = getActivity().getWindow().getDecorView().getSystemUiVisibility();
                        originalOrientation = getActivity().getRequestedOrientation();

                        // Configura a nova visualização de tela cheia
                        fullscreenContainer = new FrameLayout(getActivity());
                        fullscreenContainer.addView(view, new FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                        // Adiciona o contêiner de tela cheia à View principal
                        ((FrameLayout) getActivity().getWindow().getDecorView()).addView(fullscreenContainer);
                        customView = view;
                        customViewCallback = callback;
                    }

                    @Override
                    public void onHideCustomView() {
                        // Volta à visualização original
                        ((FrameLayout) getActivity().getWindow().getDecorView()).removeView(fullscreenContainer);
                        customView = null;
                        fullscreenContainer = null;
                        customViewCallback.onCustomViewHidden();
                        getActivity().getWindow().getDecorView().setSystemUiVisibility(originalSystemUiVisibility);
                        getActivity().setRequestedOrientation(originalOrientation);
                    }

                    @Override
                    public View getVideoLoadingProgressView() {
                        // Retorna uma View personalizada ou null se não houver
                        return super.getVideoLoadingProgressView();
                    }
                });

                // Configuração do WebView
                youtubeWebView.setWebViewClient(new WebViewClient());
                WebSettings webSettings = youtubeWebView.getSettings();
                webSettings.setJavaScriptEnabled(true);

                // Lógica para extrair o ID do vídeo e carregar a URL embutida do YouTube
                String videoId;
                if (video_url_txt.contains("youtu.be/")) {
                    videoId = video_url_txt.substring(video_url_txt.lastIndexOf("/") + 1);
                } else if (video_url_txt.contains("v=")) {
                    videoId = video_url_txt.substring(video_url_txt.indexOf("v=") + 2, video_url_txt.indexOf("v=") + 13);
                } else {
                    videoId = "";
                }

                if (!videoId.isEmpty()) {
                    String embedUrl = "https://www.youtube.com/embed/" + videoId + "?rel=0&showinfo=0&controls=1&modestbranding=1";
                    youtubeWebView.loadUrl(embedUrl);
                } else {
                    Toast.makeText(getActivity(), "ID de vídeo inexistente", Toast.LENGTH_SHORT).show();
                }
            }
        }

        //Definindo as avaliações do curso, por hora não será implementado
        lista_avaliacoes_curso = view.findViewById(R.id.lista_avaliacoes_curso);
        AdapterAvaliacoesCurso adapter = new AdapterAvaliacoesCurso(getActivity(), avaliacoes);
        lista_avaliacoes_curso.setAdapter(adapter);
        lista_avaliacoes_curso.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        return view;
    }
}