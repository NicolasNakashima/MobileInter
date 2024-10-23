package com.example.khiata.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.khiata.R;
import com.example.khiata.adapters.AdapterProdutosAdicionados;
import com.example.khiata.apis.ProductApi;
import com.example.khiata.apis.UserApi;
import com.example.khiata.models.Product;
import com.example.khiata.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_tela_perfil_costureira#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_tela_perfil_costureira extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_tela_perfil_costureira() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_tela_perfil_costureira.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_tela_perfil_costureira newInstance(String param1, String param2) {
        fragment_tela_perfil_costureira fragment = new fragment_tela_perfil_costureira();
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

    ImageView voltar_home, img_costureira, btn_sms;
    String email_costureira;
    TextView nome_costureira;
    String nome_costureira_txt;
    String phone_costureira, phone_user;
    String msg_sms;
    private Retrofit retrofit;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    RecyclerView lista_produtos_costureira;
    List<Product> produtos = new ArrayList();
    private static final String[] REQUIRED_PERMISSIONS;
    static {
        List<String> requiredPermissions = new ArrayList<>();
        requiredPermissions.add(android.Manifest.permission.SEND_SMS);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            requiredPermissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        REQUIRED_PERMISSIONS = requiredPermissions.toArray(new String[0]);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tela_perfil_costureira, container, false);

        //Ir para tela de home
        voltar_home = view.findViewById(R.id.voltar_home);
        voltar_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_conteudo, new fragment_tela_home());
                transaction.commit();
            }
        });

        Bundle bundle = getArguments();
        if(bundle != null) {
            email_costureira = bundle.getString("email_costureira", null);
            if (email_costureira != null) {
                nome_costureira = view.findViewById(R.id.nome_costureira);
                buscarInformacoesDaCostureira(email_costureira);
                img_costureira = view.findViewById(R.id.img_costureira);
                StorageReference profileRef = storageRef.child("khiata_perfis/foto_"+email_costureira+".jpg");
                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getContext()).load(uri).circleCrop().into(img_costureira);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "Falha ao obter URL da imagem"+ e.getMessage());
                        img_costureira.setImageResource(R.drawable.empty_img);
                    }
                });
            }
        }

        //Listar os produtos
        lista_produtos_costureira = view.findViewById(R.id.lista_produtos_costureira);
        lista_produtos_costureira.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        if(nome_costureira_txt != null){
            pegarProdutosDaCostureira(nome_costureira_txt);
        } else {
            Log.e("Error", "nome_costureira_txt is null");
        }


        btn_sms = view.findViewById(R.id.btn_sms);
        btn_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                View pop_sms = inflater.inflate(R.layout.pop_sms, null);

                TextView titulo = pop_sms.findViewById(R.id.titulo);
                titulo.setText("Entre em contato direto com "+nome_costureira_txt+" (SMS)");
                EditText msgSmsEditText = pop_sms.findViewById(R.id.msgSMS);
                Button btn_cancelar = pop_sms.findViewById(R.id.btn_cancelar);
                btn_cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                Button btn_enviar = pop_sms.findViewById(R.id.btn_enviar);
                btn_enviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        msg_sms = msgSmsEditText.getText().toString();
                        Log.e("msg_sms", msg_sms);
                        if(msg_sms.isEmpty()) {
                            Toast.makeText(getActivity(), "Não é possível enviar uma mensagem vazia", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            buscarTelefoneUsuario(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                            if(allPermissionsGranted()){
                                enviarSms(phone_costureira, msg_sms, phone_user);
                            } else {
                                requestPermissions();
                            }
                            dialog.cancel();
                        }
                    }
                });

                dialog.setContentView(pop_sms);
                dialog.setCancelable(true);
                dialog.show();
            }
        });

        return view;
    }

    //Método para buscar as informações do perfil da costureira
    private void buscarInformacoesDaCostureira(String userEmail) {
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
                    nome_costureira.setText(userResponse.getName());
                    phone_costureira = userResponse.getPhone();
                    nome_costureira_txt = userResponse.getName();
                } else {
                    Toast.makeText(getContext(), "Usuário não encontrado ou resposta inválida", Toast.LENGTH_SHORT).show();
                    Log.e("API Error", "Response code: " + response.code() + " | Error body: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Método para buscar o telefone do usuário atual
    private void buscarTelefoneUsuario(String userEmail) {
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
                    phone_user = userResponse.getPhone();
                } else {
                    Toast.makeText(getContext(), "Usuário não encontrado ou resposta inválida", Toast.LENGTH_SHORT).show();
                    Log.e("API Error", "Response code: " + response.code() + " | Error body: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Método para enviar SMS
    private void enviarSms(String phone_costureira, String msg_sms, String phone_user) {
        Intent intent = new Intent(String.valueOf(getActivity()));
        PendingIntent pi = PendingIntent.getActivity(getActivity(), RESULT_OK, intent, PendingIntent.FLAG_IMMUTABLE);

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(String.valueOf(phone_costureira), String.valueOf(phone_user), msg_sms, pi, null);
        if (smsManager != null) {
            Toast.makeText(getActivity(), "Mensagem enviada!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Erro ao enviar mensagem!", Toast.LENGTH_SHORT).show();
        }
    }

    //Método para buscar os produtos da costureira
    private void pegarProdutosDaCostureira(String userName) {
        if (userName == null) {
            Log.e("Error", "userName is null");
            return;
        }
        Log.e("userName", userName);
        String API_BASE_URL = "https://interdisciplinarr.onrender.com/";
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductApi productApi = retrofit.create(ProductApi.class);
        Call<List<Product>> call = productApi.getProductsByDressmarker(userName);

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> listaDeProdutos = response.body();

                    if (listaDeProdutos != null && !listaDeProdutos.isEmpty()) {
                        produtos.clear();
                        produtos.addAll(listaDeProdutos);

                        // Atualiza o adapter com a lista de produtos
                        AdapterProdutosAdicionados adapter = new AdapterProdutosAdicionados(getActivity(), produtos);
                        lista_produtos_costureira.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "Nenhum produto cadastrado.", Toast.LENGTH_SHORT).show();
                        Log.e("Error", "Nenhum produto encontrado.");
                    }
                } else {
                    Toast.makeText(getActivity(), "Falha ao carregar produtos", Toast.LENGTH_SHORT).show();
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable throwable) {
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Error", throwable.getMessage());
            }
        });
    }

    //Bloco de funções para gerenciar permissões
    private boolean allPermissionsGranted(){
        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(getActivity(), permission)
                    != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    private void requestPermissions(){
        activityResultLauncher.launch(REQUIRED_PERMISSIONS);
    }
    private ActivityResultLauncher<String[]> activityResultLauncher = registerForActivityResult(
        new ActivityResultContracts.RequestMultiplePermissions(),
        permissions -> {
            // Handle Permission granted/rejected
            boolean permissionGranted = true;
            for (Map.Entry<String, Boolean> entry : permissions.entrySet()) {
                if (Arrays.asList(REQUIRED_PERMISSIONS).contains(entry.getKey()) && !entry.getValue()) {
                    permissionGranted = false;
                    break;
                }
            }
            if (!permissionGranted) {
                Toast.makeText(getActivity(),"Permissão NEGADA. Tente novamente.",Toast.LENGTH_SHORT).show();
            } else {
                enviarSms(phone_costureira, msg_sms, phone_user);
            }
        }
    );
}