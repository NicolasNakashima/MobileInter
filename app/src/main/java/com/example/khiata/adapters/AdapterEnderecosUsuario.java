package com.example.khiata.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.khiata.classes.TratamentoErros;
import com.example.khiata.classes.tela_login;
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
    TratamentoErros tratamentoErros = new TratamentoErros();

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

        Address endereco = enderecos.get(position);

        rotulo_endereco.setText(endereco.getLabel());
        street_endereco.setText(endereco.getStreet() + " - " + endereco.getNumber() + " - " + endereco.getCep());
        complement_endereco.setText(endereco.getComplement());

        //Buscando e definindo o status do endereço
        boolean statusEndereco = endereco.isDeactivate();
        if(statusEndereco){
            status_endereco.setText("Ativo");
        } else{
            status_endereco.setText("Inativo");
        }

        int enderecoId = endereco.getId();

        //Acionar o botão de excluir
        btn_excluir_endereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int adapterPosition = holder.getAdapterPosition();
//                if (adapterPosition != RecyclerView.NO_POSITION) {
//                    deletarEnderecoDoUsuario(enderecoId, adapterPosition);
//                }
                Dialog dialog = new Dialog(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                msgPopup.setText("Está funcionalidade estará disponível no futuro.");
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

        //Acionar o botão para ir para a tela de edição
        btn_editar_endereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                // Cria um novo fragmento de edição
//                fragment_tela_editar_endereco editarEnderecoFragment = new fragment_tela_editar_endereco();
//
//                // Cria um Bundle para passar o enderecoId e ir para a tela de edição
//                Bundle bundle = new Bundle();
//                bundle.putInt("enderecoId", enderecoId);
//                Log.d("TAG", "enderecoId: "+enderecoId);
//                editarEnderecoFragment.setArguments(bundle);
//                FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.frame_conteudo, editarEnderecoFragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
                Dialog dialog = new Dialog(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                msgPopup.setText("Está funcionalidade estará disponível no futuro.");
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
                    Toast.makeText(context, "Não foi possível encontrar o destinatário.", Toast.LENGTH_SHORT).show();
                    Log.e("Error", "Erro na resposta da API: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Dialog dialog = new Dialog(context);

                LayoutInflater inflater = LayoutInflater.from(context);
                View popupView = inflater.inflate(R.layout.popup_mensagem, null);
                TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                msgPopup.setText("Erro: "+tratamentoErros.tratandoErroThrowable(throwable));
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

    //Método responsável por deletar o endereço do usuaário
    private void deletarEnderecoDoUsuario(int addressId, int position) {
        Log.d("position", String.valueOf(position));
        Log.d("addressId", String.valueOf(addressId));
        String API_BASE_URL = "https://apikhiata.onrender.com/";
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AddressApi addressApi = retrofit.create(AddressApi.class);
        Call<String> call = addressApi.deletarEndereco(addressId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    enderecos.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, enderecos.size());

                    //Pop-up de sucesso
                    Dialog dialog = new Dialog(context);
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);
                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Endereço excluído com sucesso!");
                    ImageView imgPopup = popupView.findViewById(R.id.img_popup);
                    imgPopup.setImageResource(R.drawable.icon_pop_sucesso);
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
                } else {
                    Log.e("Erro", response.message());
                    //Pop-up de falha
                    Dialog dialog = new Dialog(context);
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View popupView = inflater.inflate(R.layout.popup_mensagem, null);
                    TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                    msgPopup.setText("Falha ao excluir o endereço!");
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

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Dialog dialog = new Dialog(context);

                LayoutInflater inflater = LayoutInflater.from(context);
                View popupView = inflater.inflate(R.layout.popup_mensagem, null);

                TextView msgPopup = popupView.findViewById(R.id.msg_popup);
                msgPopup.setText("Erro: "+tratamentoErros.tratandoErroThrowable(throwable));
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
