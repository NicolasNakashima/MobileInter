package com.example.khiata.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.khiata.R;
import com.example.khiata.apis.UserApi;
import com.example.khiata.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_tela_perfil_costureira#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_tela_perfil_costureira extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_tela_perfil_costureira() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_tela_perfil_costureira.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_tela_perfil_costureira newInstance(String param1, String param2) {
        fragment_tela_perfil_costureira fragment = new fragment_tela_perfil_costureira();
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

    ImageView voltar_home;
    String email_costureira;
    ImageView img_costureira;
    TextView nome_costureira;
    int phone_costureira;
    private Retrofit retrofit;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tela_perfil_costureira, container, false);

        //Ir para tela de home
        voltar_home = view.findViewById(R.id.voltar_home);
        voltar_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_home());
                transaction.commit();
            }
        });

        Bundle bundle = getArguments();
        if(bundle != null) {
            email_costureira = bundle.getString("email_costureira", null);
            if (email_costureira != null) {
                nome_costureira = view.findViewById(R.id.nome_costureira);
                buscarInformacoesDoUsuario(email_costureira);
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
//                Toast.makeText(getContext(), "Falha ao obter URL da imagem"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "Falha ao obter URL da imagem"+ e.getMessage());
                        img_costureira.setImageResource(R.drawable.empty_img);
                    }
                });
            }
        }

        return view;
    }

    //Método para buscar as informações do perfil da costureira
    private void buscarInformacoesDoUsuario(String userEmail) {
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
                    phone_costureira = userResponse.getPhone();
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