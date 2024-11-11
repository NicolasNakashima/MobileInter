package com.example.khiata.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
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
import com.example.khiata.classes.tela_inicial;
import com.example.khiata.R;
import com.example.khiata.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

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
    boolean isDressmaker = false;
    Button btn_tela_editar_perfil, btn_tela_enderecos, btn_virar_costureira, btn_plan_premium, btn_logout, btn_tela_editar_preferencias;
    private fragment_tela_home fragment_tela_home = new fragment_tela_home();
    private fragment_tela_enderecos fragment_tela_enderecos = new fragment_tela_enderecos();
    private fragment_tela_plan_premium fragment_tela_plan_premium = new fragment_tela_plan_premium();
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

        //Ir para tela de home
        voltar_home = view.findViewById(R.id.voltar_perfil);
        voltar_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, fragment_tela_home);
                transaction.commit();
            }
        });

        //Tirar foto de perfil
        btn_tirar_foto = view.findViewById(R.id.btn_tirar_foto);
        btn_tirar_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CameraPerfil.class);
                startActivity(intent);
            }
        });

        //Carregar foto de perfil
        foto_perfil = view.findViewById(R.id.foto_perfil);
        StorageReference profileRef = storageRef.child("khiata_perfis/foto_"+userEmail+".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext()).load(uri).circleCrop().into(foto_perfil);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getContext(), "Falha ao obter URL da imagem"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "Falha ao obter URL da imagem"+ e.getMessage());
                foto_perfil.setImageResource(R.drawable.empty_img);
            }
        });

        //Carregar informações do perfil
        nome_user = view.findViewById(R.id.nome_user);
        email_user = view.findViewById(R.id.email_user);
        cpf_user = view.findViewById(R.id.cpf_user);
        idade_user = view.findViewById(R.id.idade_user);
        phone_user = view.findViewById(R.id.phone_user);
        buscarInformacoesDoUsuario(userEmail);

        //Ir para tela de endereços
        btn_tela_enderecos = view.findViewById(R.id.btn_tela_enderecos);
        btn_tela_enderecos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, fragment_tela_enderecos);
                transaction.commit();
            }
        });

        //Ir para tela de editar perfil
        btn_tela_editar_perfil = view.findViewById(R.id.btn_tela_editar_perfil);
        btn_tela_editar_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_editar_perfil());
                transaction.commit();
            }
        });

        //Ir para tela de editar preferencias
        btn_tela_editar_preferencias = view.findViewById(R.id.btn_tela_editar_preferencias);
        btn_tela_editar_preferencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_atualizar_preferencias());
                transaction.commit();
            }
        });

        //Ir para tela de plano premium
        btn_plan_premium = view.findViewById(R.id.btn_plan_premium);
        btn_plan_premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, fragment_tela_plan_premium);
                transaction.commit();
            }
        });

        //Logout
        btn_logout = view.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                View popup_opcao = inflater.inflate(R.layout.popup_opcao, null);

                TextView msgPopup = popup_opcao.findViewById(R.id.msg_popup);
                msgPopup.setText("Você está prestes a realizar o logout.\n Deseja prosseguir?");
                ImageView imgPopup = popup_opcao.findViewById(R.id.img_popup);
                imgPopup.setImageResource(R.drawable.icon_pop_logout);
                Button btn_seguir = popup_opcao.findViewById(R.id.btn_seguir);
                btn_seguir.setText("Sair");
                btn_seguir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getActivity(), tela_inicial.class);
                        startActivity(intent);
                        getActivity().finish();
                        dialog.cancel();
                    }
                });
                Button btn_cancelar = popup_opcao.findViewById(R.id.btn_cancelar);
                btn_cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                dialog.setContentView(popup_opcao);
                dialog.setCancelable(true);
                dialog.show();
            }
        });

        //Tornar-se costureira
        btn_virar_costureira = view.findViewById(R.id.btn_virar_costureira);
        btn_virar_costureira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                View popup_opcao = inflater.inflate(R.layout.popup_opcao, null);

                TextView msgPopup = popup_opcao.findViewById(R.id.msg_popup);
                msgPopup.setText("Você está prestes a se tornar uma costureira.\n Deseja prosseguir?");
                ImageView imgPopup = popup_opcao.findViewById(R.id.img_popup);
                imgPopup.setImageResource(R.drawable.icon_pop_alert);
                Button btn_seguir = popup_opcao.findViewById(R.id.btn_seguir);
                btn_seguir.setText("Continuar");
                btn_seguir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        btn_seguir.setEnabled(false);
                        buscarSeUsuarioEhCostureira(userEmail);
                        dialog.cancel();
                    }
                });
                Button btn_cancelar = popup_opcao.findViewById(R.id.btn_cancelar);
                btn_cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                dialog.setContentView(popup_opcao);
                dialog.setCancelable(true);
                dialog.show();
            }
        });

        return view;
    }


    //Método para buscar as informações do perfil
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
                    nome_user.setText(userResponse.getName());
                    email_user.setText(userResponse.getEmail());
                    cpf_user.setText(userResponse.getCpf());

                    // Convertendo corretamente para String os valores numéricos
                    idade_user.setText(String.valueOf(userResponse.getAge()));  // Converte idade para String
                    phone_user.setText(String.valueOf(userResponse.getPhone())); // Converte telefone para String, caso seja numérico
                } else {
                    Dialog dialog = new Dialog(getActivity());
                    LayoutInflater inflater = getLayoutInflater();
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Erro:" + response.message());
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
                Dialog dialog = new Dialog(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                msgPopup.setText("Erro:" + throwable.getMessage());
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

    //Método para buscar se o usuário é costureira
    private void  buscarSeUsuarioEhCostureira(String userEmail) {
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
                    isDressmaker= userResponse.isDressmaker();

                    if (!isDressmaker) {
                        Map<String, Object> atualizacao = new HashMap<>();
                        atualizacao.put("isDressmaker", true);
                        tornarUsuarioCostureira(userEmail, atualizacao);
                    } else {
                        Dialog dialog = new Dialog(getActivity());
                        LayoutInflater inflater = getLayoutInflater();
                        View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                        TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                        msgPopup.setText("Seu perfil ja é de costureira.");
                        ImageView imgPopup = popupView.findViewById(R.id.img_popup);
                        imgPopup.setImageResource(R.drawable.icon_pop_sucesso);
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
                } else {
                    Dialog dialog = new Dialog(getActivity());
                    LayoutInflater inflater = getLayoutInflater();
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Erro:" + response.message());
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
                Dialog dialog = new Dialog(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                msgPopup.setText("Erro:" + throwable.getMessage());
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

    //Método para tornar-se costureira
    private void tornarUsuarioCostureira(String email, Map<String, Object> atualizacao) {
        String API_BASE_URL = "https://apikhiata.onrender.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi userApi = retrofit.create(UserApi.class);
        Call<Void> call = userApi.atualizarUsuario(email, atualizacao);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    // Captura a mensagem de erro diretamente
                    String errorMessage = "Erro: " + response.code(); // Exibe o código do erro
                    if (response.errorBody() != null) {
                        errorMessage += " - " + response.errorBody().toString();
                    }
                    Dialog dialog = new Dialog(getActivity());
                    LayoutInflater inflater = getLayoutInflater();
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Erro:" + errorMessage);
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
                    Log.e("Error", errorMessage);
                } else {
                    // A atualização foi bem-sucedida
                    Dialog dialog = new Dialog(getActivity());
                    LayoutInflater inflater = getLayoutInflater();
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Vocé se tornou uma costureira.");
                    ImageView imgPopup = popupView.findViewById(R.id.img_popup);
                    imgPopup.setImageResource(R.drawable.icon_pop_sucesso);
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

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Dialog dialog = new Dialog(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                msgPopup.setText("Erro:" + t.getMessage());
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
}