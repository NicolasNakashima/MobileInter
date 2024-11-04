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

public class tela_forms extends AppCompatActivity {

    ImageView voltar_inicio;
    Button btn_encerrar_forms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_forms);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Botão para voltar para tela inicial
        voltar_inicio = findViewById(R.id.voltar_inicio);
        voltar_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), tela_inicial.class);
                startActivity(intent);
                finish();
            }
        });

        //Botão para seguir para a tela home ao terminar o formulário
        btn_encerrar_forms = findViewById(R.id.btn_encerrar_forms);
        btn_encerrar_forms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(tela_forms.this);
                LayoutInflater inflater = getLayoutInflater();
                View popup_opcao = inflater.inflate(R.layout.popup_opcao, null);

                TextView msgPopup = popup_opcao.findViewById(R.id.msg_popup);
                msgPopup.setText("Você pode seguir para o inicio se tiver terminado o forms.");
                ImageView imgPopup = popup_opcao.findViewById(R.id.img_popup);
                imgPopup.setImageResource(R.drawable.icon_pop_alert);
                Button btn_seguir = popup_opcao.findViewById(R.id.btn_seguir);
                btn_seguir.setText("Terminei");
                btn_seguir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getApplicationContext(), tela_inicial.class);
                        startActivity(intent);
                        finish();
                        dialog.cancel();
                    }
                });
                Button btn_cancelar = popup_opcao.findViewById(R.id.btn_cancelar);
                btn_cancelar.setText("Cancelar");
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
    }
}