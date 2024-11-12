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
        btn_enviar_email = findViewById(R.id.btn_enviar_email);
        btn_enviar_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pegando o valor digitado pelo usuário
                String userEmail = ((EditText) findViewById(R.id.editEmail)).getText().toString();

                // Verificando se o campo foi preenchido
                if (userEmail.isEmpty()) {
                    showDialog("Por favor, preencha o campo de email.", R.drawable.icon_pop_alert);
                } else {
                    auth.fetchSignInMethodsForEmail(userEmail).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            boolean isEmailRegistered = task.getResult() != null && !task.getResult().getSignInMethods().isEmpty();

                            if (isEmailRegistered) {
                                // Se o e-mail existe, envia o e-mail de redefinição de senha
                                auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(sendTask -> {
                                    if (sendTask.isSuccessful()) {
                                        showDialog("Um e-mail de redefinição de senha foi enviado. Cheque sua caixa de entrada.", R.drawable.icon_pop_sucesso, true);
                                    } else {
                                        showDialog("Não foi possível enviar um email de redefinição de senha. Tente novamente mais tarde.", R.drawable.icon_pop_alert);
                                    }
                                });
                            } else {
                                // Mostra um pop-up indicando que o e-mail não está cadastrado
                                showDialog("Este e-mail não está cadastrado. Verifique e tente novamente.", R.drawable.icon_pop_alert);
                            }
                        } else {
                            // Caso ocorra um erro na verificação do e-mail
                            showDialog("Erro ao verificar o e-mail. Tente novamente mais tarde.", R.drawable.icon_pop_alert);
                        }
                    });
                }
            }

            private void showDialog(String message, int imageResId) {
                showDialog(message, imageResId, false);
            }

            private void showDialog(String message, int imageResId, boolean redirectToLogin) {
                Dialog dialog = new Dialog(tela_nova_senha.this);
                LayoutInflater inflater = getLayoutInflater();
                View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                msgPopup.setText(message);
                ImageView imgPopup = popupView.findViewById(R.id.img_popup);
                imgPopup.setImageResource(imageResId);
                Button btnPopup = popupView.findViewById(R.id.btn_popup);
                btnPopup.setText(redirectToLogin ? "Login" : "Ok");

                btnPopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (redirectToLogin) {
                            Intent intent = new Intent(tela_nova_senha.this, tela_login.class);
                            startActivity(intent);
                            finish();
                        }
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