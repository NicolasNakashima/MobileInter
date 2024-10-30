package com.example.khiata.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.khiata.R;
import com.example.khiata.adapters.AdapterAvaliacoesCurso;
import com.example.khiata.adapters.AdapterAvaliacoesUsuario;
import com.example.khiata.adapters.AdapterCursos;
import com.example.khiata.models.Avaliation;
import com.example.khiata.models.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_tela_cursos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_tela_cursos extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_tela_cursos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_tela_cursos.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_tela_cursos newInstance(String param1, String param2) {
        fragment_tela_cursos fragment = new fragment_tela_cursos();
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

    ImageView voltar_home;
    Button btn_adquirir_premium;
    RecyclerView lista_cursos;
    List<Course> cursos = new ArrayList();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tela_cursos, container, false);

        //Botão para voltar para o home
        voltar_home = view.findViewById(R.id.voltar_home);
        voltar_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_home());
                transaction.commit();
            }
        });

        //Botão para adquirir premium
        btn_adquirir_premium = view.findViewById(R.id.btn_adquirir_premium);
        btn_adquirir_premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_plan_premium());
                transaction.commit();
            }
        });

        //Definindo os cursos
        lista_cursos = view.findViewById(R.id.lista_cursos);
        cursos.add(new Course("Teste", "Categoria", "2h 10min", 4.5, "https://blog.maximustecidos.com.br/wp-content/uploads/2020/11/4-dicas-para-conservar-a-maquina-de-costura-1.jpg", "https://www.youtube.com/watch?v=6kZMl7A7XWQ&pp=ygUHY29zdHVyYQ%3D%3D"));
        AdapterCursos adapter = new AdapterCursos(getActivity(), cursos);
        lista_cursos.setAdapter(adapter);
        lista_cursos.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        return view;
    }
}