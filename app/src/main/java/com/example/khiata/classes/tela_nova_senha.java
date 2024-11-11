package com.example.khiata.classes;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.khiata.R;
import com.google.firebase.auth.FirebaseAuth;

public class tela_nova_senha extends AppCompatActivity {

    ImageView voltar_login;
    Button btn_enviar_email, btn_cancelar_atualizar_senha;
    FirebaseAuth auth= FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_nova_senha);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Botão para voltar para tela de login
        voltar_login = findViewById(R.id.voltar_login);
        voltar_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tela_nova_senha.this, tela_login.class);
                startActivity(intent);
            }
        });


        //Botão para cancelar a atualização da senha
        btn_cancelar_atualizar_senha = findViewById(R.id.btn_cancelar_atualizar_senha);
        btn_cancelar_atualizar_senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tela_nova_senha.this, tela_login.class);
                startActivity(intent);
            }
        });


        //Botão para enviar um email de atualizar senha
        btn_enviar_email= findViewById(R.id.btn_enviar_email);
        btn_enviar_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pegando o valor digitado pelo usuário
                String userEmail = ((EditText) findViewById(R.id.editEmail)).getText().toString();
                //Verificando se o campo foi preenchido
                if(userEmail.isEmpty()){
                    Dialog dialog = new Dialog(tela_nova_senha.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                    //Captura os elementos do pop-up
                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Por favor, preencha o campo de email.");
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
                } else{
                    auth.sendPasswordResetEmail(userEmail)
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                Dialog dialog = new Dialog(tela_nova_senha.this);
                                LayoutInflater inflater = getLayoutInflater();
                                View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                                //Captura os elementos do pop-up
                                TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                                msgPopup.setText("Um e-mail de redefinição de senha foi enviado. Cheque sua caixa de entrada.");
                                ImageView imgPopup = popupView.findViewById(R.id.img_popup);
                                imgPopup.setImageResource(R.drawable.icon_pop_sucesso);
                                Button btnPopup = popupView.findViewById(R.id.btn_popup);
                                btnPopup.setText("Login");
                                btnPopup.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(tela_nova_senha.this, tela_login.class);
                                        startActivity(intent);
                                        finish();
                                        dialog.cancel();
                                    }
                                });

                                dialog.setContentView(popupView);
                                dialog.setCancelable(true);
                                dialog.show();
                            } else{
                                Dialog dialog = new Dialog(tela_nova_senha.this);
                                LayoutInflater inflater = getLayoutInflater();
                                View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                                //Captura os elementos do pop-up
                                TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                                msgPopup.setText("Não foi possível enviar um email de redefinição de senha. Tente novamente mais tarde.");
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
        });
    }
}