package com.example.khiata.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khiata.R;
import com.example.khiata.apis.UserApi;
import com.example.khiata.classes.tela_cadastro;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_tela_editar_perfil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_tela_editar_perfil extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_tela_editar_perfil() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_tela_editar_perfil.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_tela_editar_perfil newInstance(String param1, String param2) {
        fragment_tela_editar_perfil fragment = new fragment_tela_editar_perfil();
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

    ImageView voltar_perfil;
    Button btn_cancelar_alteracoes, btn_salvar_alteracoes;
//    private fragment_tela_perfil fragment_tela_perfil = new fragment_tela_perfil();
    private Retrofit retrofit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tela_editar_perfil, container, false);

        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

//        Ir para tela de perfil
        voltar_perfil = view.findViewById(R.id.voltar_perfil);
        voltar_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_perfil());
                transaction.commit();
            }
        });

        //Cancelar alterações
        btn_cancelar_alteracoes = view.findViewById(R.id.btn_cancelar_alteracoes);
        btn_cancelar_alteracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_perfil());
                transaction.commit();
            }
        });

        //Salvar alterações
        btn_salvar_alteracoes = view.findViewById(R.id.btn_salvar_alteracoes);
        btn_salvar_alteracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String regexTelefone = "\\d{2}\\d{5}\\d{4}"; //Expressão regular para o formato DDXXXXXXXXX
                String atualizarNome = ((EditText) view.findViewById(R.id.atualizarNome)).getText().toString();
                int atualizarIdade = 0;
                String atualizarPhone = ((EditText) view.findViewById(R.id.atualizarPhone)).getText().toString();
                String idadeTexto = ((EditText) view.findViewById(R.id.atualizarIdade)).getText().toString();
                if (!idadeTexto.isEmpty()) {
                    try {
                        atualizarIdade = Integer.parseInt(idadeTexto);
                    } catch (NumberFormatException e) {
                        Dialog dialog = new Dialog(getActivity());
                        LayoutInflater inflater = getLayoutInflater();
                        View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                        TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                        msgPopup.setText("Idade inválida.");
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
                RadioGroup opcoesGenero = view.findViewById(R.id.opcoesGenero);
                int selectedId = opcoesGenero.getCheckedRadioButtonId();

                Map<String, Object> atualizacoes = new HashMap<>();
                if(!atualizarNome.isEmpty()){
                    atualizacoes.put("name", atualizarNome);
                }
                if(atualizarIdade > 0){
                    atualizacoes.put("age", atualizarIdade);
                }
                if(!atualizarPhone.isEmpty()){
                    if (atualizarPhone.matches(regexTelefone)) {
                        atualizacoes.put("phones", atualizarPhone);
                    } else {
                        Dialog dialog = new Dialog(getActivity());
                        LayoutInflater inflater = getLayoutInflater();
                        View popupView = inflater.inflate(R.layout.popup_mensagem, null);
                        TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                        msgPopup.setText("Por favor, digite um telefone valido no formato DDXXXXXXXXX.");
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
                if (selectedId != -1) {
                    int novoGenero = 0;
                    if(selectedId == R.id.opcaoHomem){
                        novoGenero = 2;
                    } else if(selectedId == R.id.opcaoMulher){
                        novoGenero = 1;
                    }
                    atualizacoes.put("genderId", novoGenero);
                }
                if (atualizacoes.isEmpty()) {
                    // Nenhuma alteração feita
                    Dialog dialog = new Dialog(getActivity());

                    LayoutInflater inflater = getLayoutInflater();
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Nenhuma alteração foi feita.");
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
                } else {
                    // Chamando a API para atualizar o perfil
                    atualizarUsuarioAPI(userEmail, atualizacoes);
                }
            }
        });

        return view;
    }

    //Método para atualizar o perfil do usuário
    private void atualizarUsuarioAPI(String email, Map<String, Object> atualizacoes) {
        String API_BASE_URL = "https://apikhiata.onrender.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi userApi = retrofit.create(UserApi.class);
        Call<Void> call = userApi.atualizarUsuario(email, atualizacoes);

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
                    msgPopup.setText("Erro: " + errorMessage);
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
                    msgPopup.setText("Perfil atualizado com sucesso.");
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
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_conteudo, new fragment_tela_perfil());
                    transaction.commit();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Dialog dialog = new Dialog(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                msgPopup.setText("Erro: " + t.getMessage());
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