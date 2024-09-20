package com.example.khiata.activities_classes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.khiata.R;
import com.example.khiata.fragments.fragment_tela_home;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class tela_login extends AppCompatActivity {

    TextView esqueceu_senha;
    Button btn_login, btn_ir_para_cadastro;
    ImageView login_voltar_inicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        esqueceu_senha = findViewById(R.id.esqueceu_senha);
        esqueceu_senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tela_login.this, tela_nova_senha.class);
                startActivity(intent);
            }
        });

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pegando o email e a senha digitados
                String txtEmail = ((EditText) findViewById(R.id.editLoginEmail)).getText().toString();
                String txtSenha = ((EditText) findViewById(R.id.editLoginSenha)).getText().toString();

                FirebaseAuth autenticar = FirebaseAuth.getInstance();
                //Autenticar usuario
                autenticar.signInWithEmailAndPassword(txtEmail, txtSenha)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //Abrir tela home se login bem sucedido
                                Intent intent = new Intent(tela_login.this, MainActivity.class);
                                startActivity(intent);
                            } else{
                                //Exibir mensagem de erro
                                String msgErro="";
                                try{
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException e){
                                    msgErro = "Email inválido";
                                } catch (FirebaseAuthInvalidCredentialsException e){
                                    msgErro = "Senha inválida";
                                } catch (Exception e){
                                    msgErro = "Erro ao autenticar: " + e.getMessage();
                                }
                                Toast.makeText(tela_login.this, msgErro, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            }
        });

        btn_ir_para_cadastro = findViewById(R.id.btn_ir_para_cadastro);
        btn_ir_para_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tela_login.this, tela_cadastro.class);
                startActivity(intent);
            }
        });

        login_voltar_inicio = findViewById(R.id.login_voltar_inicio);
        login_voltar_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tela_login.this, tela_inicial.class);
                startActivity(intent);
            }
        });
    }
}