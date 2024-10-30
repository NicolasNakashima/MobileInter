package com.example.khiata.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.khiata.R;
import com.example.khiata.adapters.AdapterAvaliacoesCostureira;
import com.example.khiata.classes.tela_carrinho;
import com.example.khiata.models.Avaliation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_tela_produto#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_tela_produto extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_tela_produto() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_tela_produto.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_tela_produto newInstance(String param1, String param2) {
        fragment_tela_produto fragment = new fragment_tela_produto();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    ImageView voltar_home, btn_carrinho, img_produto;
    TextView titulo_produto, vendedor_produto, preco_produto, tamanho_produto, descricao_produto;
    RatingBar avaliacao_produto;
    Button btn_adicionar_produto_carrinho, btn_adicionar_avaliacao_produto;
    RecyclerView lista_avaliacoes_produto;
    List<Avaliation> avaliacoes = new ArrayList();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tela_produto, container, false);

        //Botão para voltar para a tela home
        voltar_home = view.findViewById(R.id.voltar_home);
        voltar_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_home());
                transaction.commit();
            }
        });

        //Botão para ir para o carrinho
        btn_carrinho = view.findViewById(R.id.btn_carrinho);
        btn_carrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), tela_carrinho.class);
                startActivity(intent);
            }
        });

        //Pegando informações do produto
        Bundle bundle = getArguments();
        if(bundle != null){
            String titulo_produto_txt = bundle.getString("titulo_produto");
            String vendedor_produto_txt = bundle.getString("vendedor_produto");
            String preco_produto_txt = String.valueOf(bundle.getDouble("preco_produto"));
            String imagem_produto_txt = bundle.getString("imagem_produto");
            String descricao_produto_txt = bundle.getString("descricao_produto");
            String tamanho_produto_txt = bundle.getString("tamanho_produto");
            float avaliacao_produto_txt = bundle.getFloat("avaliacao_produto");
            if (titulo_produto_txt != null && vendedor_produto_txt != null && preco_produto_txt != null && imagem_produto_txt != null && descricao_produto_txt != null && tamanho_produto_txt != null) {
                titulo_produto = view.findViewById(R.id.titulo_produto);
                titulo_produto.setText(titulo_produto_txt);
                vendedor_produto = view.findViewById(R.id.vendedor_produto);
                vendedor_produto.setText("Vendedor: " + vendedor_produto_txt);
                preco_produto = view.findViewById(R.id.preco_produto);
                preco_produto.setText("R$ " + preco_produto_txt);
                tamanho_produto = view.findViewById(R.id.tamanho_produto);
                tamanho_produto.setText("Tamanho: " + tamanho_produto_txt);
                descricao_produto = view.findViewById(R.id.descricao_produto);
                descricao_produto.setText(descricao_produto_txt);
                avaliacao_produto = view.findViewById(R.id.avaliacao_produto);
                Drawable stars = avaliacao_produto.getProgressDrawable();
                stars.setColorFilter(Color.parseColor("#FAC552"), PorterDuff.Mode.SRC_ATOP);
                avaliacao_produto.setRating(avaliacao_produto_txt);
                img_produto = view.findViewById(R.id.img_produto);
                StorageReference profileRef = storageRef.child("khiata_produtos/"+imagem_produto_txt+".jpg");
                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getContext()).load(uri).into(img_produto);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "Falha ao obter URL da imagem"+ e.getMessage());
                        img_produto.setImageResource(R.drawable.add_img);
                    }
                });
            }
        }

        //Botão para adicionar o produto ao carrinho
        btn_adicionar_produto_carrinho = view.findViewById(R.id.btn_adicionar_produto_carrinho);
        btn_adicionar_produto_carrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Botão para adicionar avaliação ao produto
        btn_adicionar_avaliacao_produto = view.findViewById(R.id.btn_adicionar_avaliacao_produto);
        btn_adicionar_avaliacao_produto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                View pop_avaliacao = inflater.inflate(R.layout.pop_avaliacao, null);

                RatingBar avaliation_bar = pop_avaliacao.findViewById(R.id.avaliation_bar);
                EditText edit_comment = pop_avaliacao.findViewById(R.id.edit_comment);
                Button btn_enviar = pop_avaliacao.findViewById(R.id.btn_enviar);
                Button btn_cancelar = pop_avaliacao.findViewById(R.id.btn_cancelar);
                btn_cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                dialog.setContentView(pop_avaliacao);
                dialog.setCancelable(true);
                dialog.show();
            }
        });

        //Definindo as avaliações do produto
        lista_avaliacoes_produto = view.findViewById(R.id.lista_avaliacoes_produto);
        avaliacoes.add(new Avaliation("Joaquim", "Serviço muito bom, comprarei novamente", 4));
        AdapterAvaliacoesCostureira adapter = new AdapterAvaliacoesCostureira(getActivity(), avaliacoes);
        lista_avaliacoes_produto.setAdapter(adapter);
        lista_avaliacoes_produto.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        return view;
    }
}