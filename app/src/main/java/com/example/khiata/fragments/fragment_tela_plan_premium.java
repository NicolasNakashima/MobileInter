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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khiata.R;
import com.example.khiata.apis.UserApi;
import com.example.khiata.classes.tela_login;
import com.example.khiata.models.User;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_tela_plan_premium#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_tela_plan_premium extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_tela_plan_premium() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_tela_plan_premium.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_tela_plan_premium newInstance(String param1, String param2) {
        fragment_tela_plan_premium fragment = new fragment_tela_plan_premium();
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
    Button btn_adquirir_premium;
    private Retrofit retrofit;
    int statusPremium;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tela_plan_premium, container, false);

        //Botao voltar para tela home
        voltar_home = view.findViewById(R.id.voltar_home);
        voltar_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_home());
                transaction.commit();
            }
        });

        //Botao para seguir no processo de adquirir o plano premium
        btn_adquirir_premium = view.findViewById(R.id.btn_adquirir_premium);
        btn_adquirir_premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarStatusPremium(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                if(statusPremium == 0){
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_conteudo, new fragment_tela_dados_compra_premium());
                    transaction.commit();
                }
                if(statusPremium == 1){
                    Dialog dialog = new Dialog(getActivity());

                    LayoutInflater inflater = getLayoutInflater();
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Sua conta já possui o plano premium");
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
                if (statusPremium == 2) {
                    Dialog dialog = new Dialog(getActivity());

                    LayoutInflater inflater = getLayoutInflater();
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("O seu plano premium logo será liberado");
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


        return view;
    }

    //Método para buscar o status do plano premium de um determinado usuário
    private void buscarStatusPremium(String userEmail) {
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
                    statusPremium = userResponse.getPremiumStatus();

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