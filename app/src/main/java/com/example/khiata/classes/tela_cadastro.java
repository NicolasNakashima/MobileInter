package com.example.khiata.classes;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.khiata.R;
import com.example.khiata.apis.UserApi;
import com.example.khiata.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class tela_cadastro extends AppCompatActivity {

    ImageView cadastro_voltar_inicio;

    Button btn_cadastrar, btn_ir_para_login;

    private Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_cadastro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //voltar para tela inicial
        cadastro_voltar_inicio = findViewById(R.id.cadastro_voltar_inicio);
        cadastro_voltar_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tela_cadastro.this, tela_inicial.class);
                startActivity(intent);
            }
        });

        //ir para tela de login
        btn_ir_para_login = findViewById(R.id.btn_ir_para_login);
        btn_ir_para_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Cadastrar conta
        btn_cadastrar = findViewById(R.id.btn_cadastrar);
        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String regexTelefone = "\\d{2}\\d{5}\\d{4}"; //Expressão regular para o formato DDXXXXXXXXX
                String novoNome = ((EditText) findViewById(R.id.novoNome)).getText().toString();
                String novoEmail = ((EditText) findViewById(R.id.novoEmail)).getText().toString();
                String novoCPF = ((EditText) findViewById(R.id.novoCPF)).getText().toString();
                String novoPhone = ((EditText) findViewById(R.id.novoPhone)).getText().toString();
                int novaIdade = Integer.parseInt(((EditText) findViewById(R.id.novaIdade)).getText().toString());
                String novaSenha = ((EditText) findViewById(R.id.novaSenha)).getText().toString();
                String novaConfirmaSenha = ((EditText) findViewById(R.id.confirmNovaSenha)).getText().toString();
                boolean confirmCostureira = ((CheckBox) findViewById(R.id.checkConfirmCostureira)).isChecked();

                RadioGroup opcoesGenero = findViewById(R.id.opcoesGenero);
                int selectedId = opcoesGenero.getCheckedRadioButtonId();

                if(novoNome.isEmpty() || novoEmail.isEmpty() || novoCPF.isEmpty() || novoPhone.isEmpty() || novaIdade == 0 || novaSenha.isEmpty() || novaConfirmaSenha.isEmpty() || selectedId == -1) {
                    Dialog dialog = new Dialog(tela_cadastro.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);
                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Por favor, preencha todos os campos para realizar um cadastro.");
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
                    if(novaSenha.equals(novaConfirmaSenha)){
                        if(novoPhone.matches(regexTelefone)){
                            int novoGenero = 0;
                            if(selectedId == R.id.opcaoHomem){
                                novoGenero = 2;
                            } else if(selectedId == R.id.opcaoMulher){
                                novoGenero = 1;
                            }

                            User novoUser = new User( novoNome, novoCPF, novoGenero,novaIdade,confirmCostureira,0, novoPhone, null,novaSenha, novoEmail, null);
                            Log.e("novoUser", novoUser.toString());
                            Log.d("User", new Gson().toJson(novoUser));
                            cadastrarUsuarioAPI(novoUser);
                        } else{
                            Dialog dialog = new Dialog(tela_cadastro.this);
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
                    } else{
                        Dialog dialog = new Dialog(tela_cadastro.this);
                        LayoutInflater inflater = getLayoutInflater();
                        View popupView = inflater.inflate(R.layout.popup_mensagem, null);
                        TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                        msgPopup.setText("Por favor, confirme a senha corretamente.");
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
            }
        });
    }

    // Método para cadastrar um novo usuário na API
    private void cadastrarUsuarioAPI(User user) {
        String API_BASE_URL = "https://apikhiata.onrender.com/";
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi userApi = retrofit.create(UserApi.class);
        Call<ResponseBody> call = userApi.inserirUsuario(user);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    try {
                        String errorBody = response.errorBody().string();
                        Toast.makeText(tela_cadastro.this, "Erro: " + errorBody, Toast.LENGTH_LONG).show();
                        Log.e("Error", errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    ResponseBody successMessage = response.body(); // Captura a mensagem de sucesso
                    Toast.makeText(tela_cadastro.this, successMessage.toString(), Toast.LENGTH_LONG).show();
                    cadastrarUsuarioFirebase(user.getEmail(), user.getPassword());
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Intent intent = new Intent(tela_cadastro.this, MainActivity.class);
                                    startActivity(intent);
                                } else{
                                    Toast.makeText(tela_cadastro.this, "Erro ao realizar o login: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(tela_cadastro.this, "Erro ao cadastrar usuário: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Error", t.getMessage());
            }
        });
    }


    // Método para cadastrar um novo usuário no Firebase
    private void cadastrarUsuarioFirebase(String novoEmail, String novaSenha){
        FirebaseAuth autenticar = FirebaseAuth.getInstance();

        autenticar.createUserWithEmailAndPassword(novoEmail, novaSenha)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(tela_cadastro.this, "Cadastro realizado com sucesso no Firebase", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(tela_cadastro.this, "Erro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}