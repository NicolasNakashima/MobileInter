package com.example.khiata.classes;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.khiata.R;
import com.google.firebase.auth.FirebaseAuth;

public class tela_inicial extends AppCompatActivity {

    Button btn_login_inicio, btn_cadastrar_inicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_inicial);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Chamando o pop-up para o forms
        popupForms();

        //Botão para ir para tela de login
        btn_login_inicio = findViewById(R.id.btn_login_inicio);
        btn_login_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tela_inicial.this, tela_login.class);
                startActivity(intent);
            }
        });

        //Botão para ir para tela de cadastro
        btn_cadastrar_inicio = findViewById(R.id.btn_cadastrar_inicio);
        btn_cadastrar_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tela_inicial.this, tela_cadastro.class);
                startActivity(intent);
            }
        });
    }

    private void popupForms(){
        Dialog dialog = new Dialog(tela_inicial.this);
        LayoutInflater inflater = getLayoutInflater();
        View popup_opcao = inflater.inflate(R.layout.popup_opcao, null);

        TextView msgPopup = popup_opcao.findViewById(R.id.msg_popup);
        msgPopup.setText("Gostaria de relizar um pesquisa forms do aplicativo?");
        ImageView imgPopup = popup_opcao.findViewById(R.id.img_popup);
        imgPopup.setImageResource(R.drawable.icon_pop_alert);
        Button btn_seguir = popup_opcao.findViewById(R.id.btn_seguir);
        btn_seguir.setText("Sim");
        btn_seguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), tela_forms.class);
                startActivity(intent);
                finish();
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
}