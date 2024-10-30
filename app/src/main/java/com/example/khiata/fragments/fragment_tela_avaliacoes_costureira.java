package com.example.khiata.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.khiata.R;
import com.example.khiata.adapters.AdapterAvaliacoesCostureira;
import com.example.khiata.adapters.AdapterAvaliacoesUsuario;
import com.example.khiata.apis.UserApi;
import com.example.khiata.models.Avaliation;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_tela_avaliacoes_costureira#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_tela_avaliacoes_costureira extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_tela_avaliacoes_costureira() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_tela_avaliacoes_costureira.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_tela_avaliacoes_costureira newInstance(String param1, String param2) {
        fragment_tela_avaliacoes_costureira fragment = new fragment_tela_avaliacoes_costureira();
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

    ImageView voltar_perfil_costureira, img_costureira;
    TextView nome_costureira;
    RatingBar avaliacao_costureira;
    ImageButton btn_adicionar_avaliacao_costureira;
    RecyclerView lista_avaliacoes_costureira;
    List<Avaliation> avaliacoes = new ArrayList();
    private Retrofit retrofit;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    String email_costureira;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tela_avaliacoes_costureira, container, false);

        //Botão para voltar para a tela de perfil da costureira
        voltar_perfil_costureira = view.findViewById(R.id.voltar_perfil_costureira);
        voltar_perfil_costureira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_perfil_costureira());
                transaction.commit();
            }
        });

        //Pegando os dados da costureira
        Bundle bundle = getArguments();
        if(bundle != null) {
            email_costureira = bundle.getString("email_costureira", null);
            if (email_costureira != null) {
                nome_costureira = view.findViewById(R.id.nome_costureira);
                avaliacao_costureira = view.findViewById(R.id.avaliacao_costureira);
                Drawable stars = avaliacao_costureira.getProgressDrawable();
                stars.setColorFilter(Color.parseColor("#FAC552"), PorterDuff.Mode.SRC_ATOP);
                buscarInformacoesDaCostureira(email_costureira);

                img_costureira = view.findViewById(R.id.img_costureira);
                StorageReference profileRef = storageRef.child("khiata_perfis/foto_"+email_costureira+".jpg");
                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getContext()).load(uri).circleCrop().into(img_costureira);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "Falha ao obter URL da imagem"+ e.getMessage());
                        img_costureira.setImageResource(R.drawable.empty_img);
                    }
                });
            }
        }

        //Botão para adicionar uma avaliação
        btn_adicionar_avaliacao_costureira = view.findViewById(R.id.btn_adicionar_avaliacao_costureira);
        btn_adicionar_avaliacao_costureira.setOnClickListener(new View.OnClickListener() {
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

        //Definindo as avaliações da costureira
        lista_avaliacoes_costureira = view.findViewById(R.id.lista_avaliacoes_costureira);
        avaliacoes.add(new Avaliation("Joaquim", "Serviço muito bom, comprarei novamente", 4));
        AdapterAvaliacoesCostureira adapter = new AdapterAvaliacoesCostureira(getActivity(), avaliacoes);
        lista_avaliacoes_costureira.setAdapter(adapter);
        lista_avaliacoes_costureira.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        return view;
    }

    //Método para buscar as informações do perfil da costureira
    private void buscarInformacoesDaCostureira(String userEmail) {
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
                    nome_costureira.setText(userResponse.getName());
                    avaliacao_costureira.setRating((float) userResponse.getAvaliation());
                } else {
                    Toast.makeText(getContext(), "Usuário não encontrado ou resposta inválida", Toast.LENGTH_SHORT).show();
                    Log.e("API Error", "Response code: " + response.code() + " | Error body: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}