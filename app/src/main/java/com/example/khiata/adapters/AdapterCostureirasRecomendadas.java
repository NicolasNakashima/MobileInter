package com.example.khiata.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khiata.R;
import com.example.khiata.models.Costureira;

import java.util.ArrayList;
import java.util.List;

public class AdapterCostureirasRecomendadas extends RecyclerView.Adapter<AdapterCostureirasRecomendadas.MeuViewHolder> {

    private List<Costureira> costureiras = new ArrayList();

    public AdapterCostureirasRecomendadas(List<Costureira> costureiras) {
        this.costureiras = costureiras;
    }

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
        nome_costureira.setText(costureiras.get(position).getNome());
    }

    @Override
    public int getItemCount() {return costureiras.size();}

    public class MeuViewHolder extends RecyclerView.ViewHolder{
        TextView nome_costureira;

        public MeuViewHolder(@NonNull View itemView) {
            super(itemView);
            nome_costureira = itemView.findViewById(R.id.nome_costureira);
        }
    }
}
