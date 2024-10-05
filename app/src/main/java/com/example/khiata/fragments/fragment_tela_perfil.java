package com.example.khiata.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.khiata.apis.UserApi;
import com.example.khiata.classes.CameraPerfil;
import com.example.khiata.fragments.fragment_tela_home;
import com.example.khiata.R;
import com.example.khiata.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_tela_perfil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_tela_perfil extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public fragment_tela_perfil() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_tela_perfil.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_tela_perfil newInstance(String param1, String param2) {
        fragment_tela_perfil fragment = new fragment_tela_perfil();
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


    ImageView voltar_home, btn_tirar_foto, foto_perfil;
    TextView nome_user, email_user, cpf_user, idade_user, phone_user;
    Button btn_tela_editar_perfil, btn_tela_enderecos, btn_virar_costureira, btn_plan_premium, btn_logout;
    private fragment_tela_home fragment_tela_home = new fragment_tela_home();
    private fragment_tela_enderecos fragment_tela_enderecos = new fragment_tela_enderecos();
    private fragment_tela_editar_perfil fragment_tela_editar_perfil = new fragment_tela_editar_perfil();
    private fragment_tela_plan_premium fragment_tela_plan_premium = new fragment_tela_plan_premium();

    // Obtém a referência do Firebase Storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private Retrofit retrofit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tela_perfil, container, false);

        String userEmail = auth.getCurrentUser().getEmail();


        voltar_home = view.findViewById(R.id.voltar_home);
        voltar_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, fragment_tela_home);
                transaction.commit();
            }
        });

        btn_tirar_foto = view.findViewById(R.id.btn_tirar_foto);
        btn_tirar_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CameraPerfil.class);
                startActivity(intent);
            }
        });


        foto_perfil = view.findViewById(R.id.foto_perfil);
        StorageReference profileRef = storageRef.child("khiata_perfis/foto_"+userEmail+".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext()).load(uri).into(foto_perfil);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Falha ao obter URL da imagem"+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        nome_user = view.findViewById(R.id.nome_user);
        email_user = view.findViewById(R.id.email_user);
        cpf_user = view.findViewById(R.id.cpf_user);
        idade_user = view.findViewById(R.id.idade_user);
        phone_user = view.findViewById(R.id.phone_user);
        buscarInformacoesDoUsuario(userEmail);

        btn_tela_enderecos = view.findViewById(R.id.btn_tela_enderecos);
        btn_tela_enderecos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, fragment_tela_enderecos);
                transaction.commit();
            }
        });

        btn_tela_editar_perfil = view.findViewById(R.id.btn_tela_editar_perfil);
        btn_tela_editar_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, fragment_tela_editar_perfil);
                transaction.commit();
            }
        });

        btn_plan_premium = view.findViewById(R.id.btn_plan_premium);
        btn_plan_premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, fragment_tela_plan_premium);
                transaction.commit();
            }
        });


        return view;
    }


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
                User userResponse = response.body();
                nome_user.setText(userResponse.getName());
                email_user.setText(userResponse.getEmail());
                cpf_user.setText(userResponse.getCpf());
                idade_user.setText(userResponse.getAge());
                phone_user.setText(String.valueOf(userResponse.getPhone()));
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}