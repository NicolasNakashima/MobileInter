package com.example.khiata.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khiata.R;
import com.example.khiata.apis.AddressApi;
import com.example.khiata.apis.UserApi;
import com.example.khiata.fragments.fragment_tela_editar_endereco;
import com.example.khiata.fragments.fragment_tela_enderecos;
import com.example.khiata.models.Address;
import com.example.khiata.models.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdapterEnderecosUsuario extends RecyclerView.Adapter<AdapterEnderecosUsuario.MeuViewHolder> {

    private List<Address> enderecos = new ArrayList();
    private Context context;

    public AdapterEnderecosUsuario(Context context, List<Address> enderecos) {
        this.context = context;
        this.enderecos = enderecos;
    }
    private Retrofit retrofit;

    @NonNull
    @Override
    public AdapterEnderecosUsuario.MeuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Carregar o tempplate de visualização
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_endereco, parent, false);

        //Criar o ViewHolder para carregar os dados
        return new AdapterEnderecosUsuario.MeuViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterEnderecosUsuario.MeuViewHolder holder, int position) {
        TextView rotulo_endereco = holder.rotulo_endereco;
        TextView destinatario_endereco = holder.destinatario_endereco;
        TextView street_endereco = holder.street_endereco;
        TextView complement_endereco = holder.complement_endereco;
        ImageView btn_excluir_endereco = holder.btn_excluir_endereco;
        ImageView btn_editar_endereco = holder.btn_editar_endereco;
        TextView status_endereco = holder.status_endereco;

        //Buscando e definindo o nome do destinatário
        buscarNomeDoUsuario(FirebaseAuth.getInstance().getCurrentUser().getEmail(), destinatario_endereco);

        rotulo_endereco.setText(enderecos.get(position).getLabel());
        street_endereco.setText(enderecos.get(position).getStreet() + " - " + enderecos.get(position).getNumber() + " - " + enderecos.get(position).getCep());
        complement_endereco.setText(enderecos.get(position).getComplement());

        boolean statusEndereco = enderecos.get(position).isDeactivate();
        if(statusEndereco){
            status_endereco.setText("Ativo");
        } else{
            status_endereco.setText("Inativo");
        }

        int enderecoId = enderecos.get(position).getId();

        //Acionar o botão de excluir
        btn_excluir_endereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    deletarEnderecoDoUsuario(enderecoId, adapterPosition);
                }
            }
        });

        //Acionar o botão para ir para a tela de edição
        btn_editar_endereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cria um novo fragmento de edição
                fragment_tela_editar_endereco editarEnderecoFragment = new fragment_tela_editar_endereco();

                // Cria um Bundle para passar o enderecoId
                Bundle bundle = new Bundle();
                bundle.putInt("enderecoId", enderecoId); // Passa o enderecoId para o fragmento

                // Define o argumento no fragmento de edição
                editarEnderecoFragment.setArguments(bundle);

                // Inicia a transação de fragmento para substituir o fragmento atual
                FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, editarEnderecoFragment);
                transaction.addToBackStack(null); // Adiciona a transação à pilha de navegação
                transaction.commit();
            }
        });
    }

    //Método responsável por buscar o nome do usuário
    private void buscarNomeDoUsuario(String userEmail, TextView destinatario_endereco) {
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
                    destinatario_endereco.setText(userResponse.getName());
                } else {
                    // Trata caso a resposta não seja bem-sucedida
                    Log.e("Error", "Erro na resposta da API: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Método responsável por deletar o endereço do usuaário
    private void deletarEnderecoDoUsuario(int addressId, int position) {
        String API_BASE_URL = "https://apikhiata.onrender.com/";
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AddressApi addressApi = retrofit.create(AddressApi.class);
        Call<String> call = addressApi.deletarEndereco(addressId);
        Log.e("userId", String.valueOf(addressId));
        Log.e("position", String.valueOf(position));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    enderecos.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, enderecos.size());

                    Toast.makeText(context, "Endereço excluído com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("Erro", response.message());
                    Toast.makeText(context, "Falha ao excluir o endereço", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {return enderecos.size();}

    public class MeuViewHolder extends RecyclerView.ViewHolder{
        TextView rotulo_endereco, destinatario_endereco, street_endereco, complement_endereco, status_endereco;
        ImageView btn_excluir_endereco, btn_editar_endereco;

        public MeuViewHolder(@NonNull View itemView) {
            super(itemView);
            rotulo_endereco = itemView.findViewById(R.id.rotulo_endereco);
            destinatario_endereco = itemView.findViewById(R.id.destinatario_endereco);
            street_endereco = itemView.findViewById(R.id.street_endereco);
            complement_endereco = itemView.findViewById(R.id.complement_endereco);
            btn_excluir_endereco = itemView.findViewById(R.id.btn_excluir_endereco);
            btn_editar_endereco = itemView.findViewById(R.id.btn_editar_endereco);
            status_endereco = itemView.findViewById(R.id.status_endereco);
        }
    }
}
