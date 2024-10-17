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

public class tela_nova_senha extends AppCompatActivity {

    ImageView voltar_login;
    Button btn_atualizar_senha, btn_cancelar_atualizar_senha;

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


        //Botão para atualizar a senha
        btn_atualizar_senha= findViewById(R.id.btn_atualizar_senha);
        btn_atualizar_senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pegando os valores digitados pelo usuário
                String userEmail = ((EditText) findViewById(R.id.editEmail)).getText().toString();
                String novaSenha = ((EditText) findViewById(R.id.editNovaSenha)).getText().toString();
                String confirmarSenha = ((EditText) findViewById(R.id.editConfirmarSenha)).getText().toString();

                //Verificando se os campos foram preenchidos
                if(userEmail.isEmpty() || novaSenha.isEmpty() || confirmarSenha.isEmpty()){
                    //Cria um dialog
                    Dialog dialog = new Dialog(tela_nova_senha.this);

                    //Infla o layout do pop-up
                    LayoutInflater inflater = getLayoutInflater();
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                    //Captura os elementos do pop-up
                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Por favor, preencha todos os campos para realizar o login.");
                    ImageView imgPopup = popupView.findViewById(R.id.img_popup);
                    imgPopup.setImageResource(R.drawable.icon_pop_alert);
                    Button btnPopup = popupView.findViewById(R.id.btn_popup);
                    btnPopup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });

                    //Define o layout inflado como conteúdo do Dialog
                    dialog.setContentView(popupView);
                    dialog.setCancelable(true); //Permite fechar ao clicar fora do pop-up

                    //Exibe o dialog
                    dialog.show();
                } else{
                    //Verificando se as senhas conferem
                    if(!novaSenha.equals(confirmarSenha)) {
                        //Cria um dialog
                        Dialog dialog = new Dialog(tela_nova_senha.this);

                        //Infla o layout do pop-up
                        LayoutInflater inflater = getLayoutInflater();
                        View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                        //Captura os elementos do pop-up
                        TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                        msgPopup.setText("As senhas digitadas não conferem. Digite novamente.");
                        ImageView imgPopup = popupView.findViewById(R.id.img_popup);
                        imgPopup.setImageResource(R.drawable.icon_pop_alert);
                        Button btnPopup = popupView.findViewById(R.id.btn_popup);
                        btnPopup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });

                        //Define o layout inflado como conteúdo do Dialog
                        dialog.setContentView(popupView);
                        dialog.setCancelable(true); //Permite fechar ao clicar fora do pop-up

                        //Exibe o dialog
                        dialog.show();
                    }
                    else{

                    }
                }
            }
        });
    }
}