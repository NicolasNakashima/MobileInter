package com.example.khiata.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.khiata.R;
import com.example.khiata.fragments.fragment_tela_perfil_costureira;
import com.example.khiata.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class AdapterCostureirasRecomendadas extends RecyclerView.Adapter<AdapterCostureirasRecomendadas.MeuViewHolder> {

    private List<User> costureiras = new ArrayList();

    private Context context;

    public AdapterCostureirasRecomendadas(Context context, List<User> costureiras) {
        this.context = context;
        this.costureiras = costureiras;
    }

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    @NonNull
    @Override
    public AdapterCostureirasRecomendadas.MeuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Carregar o tempplate de visualização
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_costureira, parent, false);

        //Criar o ViewHolder para carregar os dados
        return new MeuViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCostureirasRecomendadas.MeuViewHolder holder, int position) {
        TextView nome_costureira = holder.nome_costureira;
        nome_costureira.setText(costureiras.get(position).getName());
        ImageView img_costureira = holder.img_costureira;
        Button btn_perfil_costureira = holder.btn_perfil_costureira;

        String email_costureira = costureiras.get(position).getEmail();

        //Carregar a imagem dos perfis de costureiras
        StorageReference profileRef = storageRef.child("khiata_perfis/foto_"+email_costureira+".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).circleCrop().into(img_costureira);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getContext(), "Falha ao obter URL da imagem"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "Falha ao obter URL da imagem"+ e.getMessage());
                img_costureira.setImageResource(R.drawable.empty_img);
            }
        });

        //Botão para acessar o perfil da costureira
        btn_perfil_costureira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cria um novo fragmento de perfil da costureira
                fragment_tela_perfil_costureira perfilCostureiraFragment = new fragment_tela_perfil_costureira();

                // Cria um Bundle para passar o email da costureira
                Bundle bundle = new Bundle();
                bundle.putString("costureira_emial", email_costureira); // Passa o email da costureira para o fragmento

                // Define o argumento no fragmento de edição
                perfilCostureiraFragment.setArguments(bundle);

                // Inicia a transação de fragmento para substituir o fragmento atual
                FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, perfilCostureiraFragment);
                transaction.addToBackStack(null); // Adiciona a transação à pilha de navegação
                transaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {return costureiras.size();}

    public class MeuViewHolder extends RecyclerView.ViewHolder{
        TextView nome_costureira;
        ImageView img_costureira;
        Button btn_perfil_costureira;

        public MeuViewHolder(@NonNull View itemView) {
            super(itemView);
            nome_costureira = itemView.findViewById(R.id.nome_costureira);
            img_costureira = itemView.findViewById(R.id.img_costureira);
            btn_perfil_costureira = itemView.findViewById(R.id.btn_perfil_costureira);
        }
    }
}
