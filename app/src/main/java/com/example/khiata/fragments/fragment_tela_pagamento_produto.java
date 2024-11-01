package com.example.khiata.fragments;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.khiata.R;
import com.example.khiata.classes.NotificationReceiver;
import com.example.khiata.classes.tela_carrinho;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_tela_pagamento_produto#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_tela_pagamento_produto extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_tela_pagamento_produto() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_tela_pagamento_produto.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_tela_pagamento_produto newInstance(String param1, String param2) {
        fragment_tela_pagamento_produto fragment = new fragment_tela_pagamento_produto();
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

    ImageView voltar_resumo_dados_pagamento;
    Button btn_pagar, btn_cancelar_pagamento;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tela_pagamento_produto, container, false);

        //Botão para voltar para tela de resumo dos dados de pagamento
        voltar_resumo_dados_pagamento = view.findViewById(R.id.voltar_resumo_dados_pagamento);
        voltar_resumo_dados_pagamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_dados_compra_produto());
                transaction.commit();
            }
        });

        //Botão para cancelar o pagamento
        btn_cancelar_pagamento = view.findViewById(R.id.btn_cancelar_pagamento);
        btn_cancelar_pagamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                View popup_opcao = inflater.inflate(R.layout.popup_opcao, null);

                TextView msgPopup = popup_opcao.findViewById(R.id.msg_popup);
                msgPopup.setText("Deseja cancelar o processo de pagamento?");
                ImageView imgPopup = popup_opcao.findViewById(R.id.img_popup);
                imgPopup.setImageResource(R.drawable.icon_pop_alert);
                Button btn_seguir = popup_opcao.findViewById(R.id.btn_seguir);
                btn_seguir.setText("Sim");
                btn_seguir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), tela_carrinho.class);
                        startActivity(intent);
                        dialog.cancel();
                    }
                });
                Button btn_cancelar = popup_opcao.findViewById(R.id.btn_cancelar);
                btn_cancelar.setText("Não");
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

        //Botão para confirmar o pagamento
        btn_pagar = view.findViewById(R.id.btn_pagar);
        btn_pagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notificar();
                    }
                }, 2000);
            }
        });


        return view;
    }

    //Método para notificação de pagamento
    private void notificar() {
        //Criar notificação
        Context context = getActivity();
        Intent intentAndroid = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentAndroid, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "channel_id")
                .setSmallIcon(R.drawable.icon_app)
                .setContentTitle("Seu pagamento foi confirmado")
                .setContentText("Pagamento recebido, logo terá informações do seu pedido.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        //Config Canal de notificação
        NotificationManager manager = getSystemService(getActivity(), NotificationManager.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "Notificacao", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 100);
                return;
            }
        }

        //Show - Apresentar notificação
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, builder.build());
    }
}