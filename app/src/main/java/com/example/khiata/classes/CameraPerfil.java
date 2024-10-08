package com.example.khiata.classes;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.khiata.R;
import com.example.khiata.databases_classes.Database;
import com.example.khiata.fragments.fragment_tela_home;
import com.example.khiata.fragments.fragment_tela_perfil;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.example.khiata.fragments.fragment_tela_perfil;

public class CameraPerfil extends AppCompatActivity {

    private fragment_tela_perfil fragment_tela_perfil = new fragment_tela_perfil();
    private ExecutorService cameraExecutor;
    private android.widget.ImageView foto;
    private androidx.camera.view.PreviewView viewFinder;

    private Map<String, String> docData = new HashMap<>();
    private Database database = new Database();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    //Config camera
    private ImageCapture imageCapture;
    private CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;//Qual camera utilizar

    //Log
    private static final String TAG = "CameraXGaleria";
    private static final String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";

    //Lista de permissões
    private static final String[] REQUIRED_PERMISSIONS;
    static {
        List<String> requiredPermissions = new ArrayList<>();
        requiredPermissions.add(android.Manifest.permission.CAMERA);
        requiredPermissions.add(android.Manifest.permission.RECORD_AUDIO);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            requiredPermissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        REQUIRED_PERMISSIONS = requiredPermissions.toArray(new String[0]);
    }
    //Fim da lista

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_camera_perfil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Obter objetos
        cameraExecutor = Executors.newSingleThreadExecutor();
        viewFinder = findViewById(R.id.viewFinder);
        foto = findViewById(R.id.foto);

        //Request de permissão
        if (allPermissionsGranted()){
            startCamera();
        }else{
            requestPermissions();
        }

        //Comportamento de botões
        //Capturar imagem
        Button button = findViewById(R.id.btn_capture_img);
        button.setOnClickListener(view -> {
            takePhoto();
        });

        //Alterar camera
        ImageButton bt3 = findViewById(R.id.mudar_lente);
        bt3.setOnClickListener(v -> {
            if(cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA){
                cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;
            }
            else{
                cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
            }
            startCamera();
        });

        //Inativando o imageview
        foto.setOnClickListener(v -> {
            foto.setVisibility(View.INVISIBLE);
        });

        //Selecionar imagem da galeria
//        ImageButton btn4 = findViewById(R.id.salvar_img);
//        btn4.setOnClickListener(v -> {
//            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            resultLauncherGaleria.launch(intent);
//        });

        //Baixar imagem do firebase
        ImageButton btn2 = findViewById(R.id.salvar_img);
        btn2.setOnClickListener(v -> {
            foto.setVisibility(View.VISIBLE);
            database.downloadFotoPerfil(foto, Uri.parse(docData.get("url")));
        });
    }

    private ActivityResultLauncher<Intent> resultLauncherGaleria =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                Uri imageUri = result.getData().getData();
                if (imageUri != null) {
                    foto.setImageURI(imageUri);
                    foto.setVisibility(View.VISIBLE);
                }
            });

    private void takePhoto() {

        if(imageCapture == null){
            return;
        }

        //Definir nome e caminho da imagem
//        String name = new SimpleDateFormat(userName, Locale.US).format(System.currentTimeMillis());
        String userEmail = auth.getCurrentUser().getEmail(); // Nome de Usuário atualmente logado
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, userEmail);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/CameraXGaleria");

        //Carregando imagem com as configurações
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(
                getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
        ).build();

        //Orientação da Imagem
        OrientationEventListener orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                int rotation;
                if(orientation >=45 && orientation < 135){
                    rotation = Surface.ROTATION_270;
                }
                else if(orientation >= 135 && orientation <= 224){
                    rotation = Surface.ROTATION_180;
                }
                else if(orientation >= 225 && orientation <= 314){
                    rotation = Surface.ROTATION_90;
                }
                else{
                    rotation = Surface.ROTATION_0;
                }
                imageCapture.setTargetRotation(rotation);
            }
        };
        orientationEventListener.enable();

        //Listener para gerar a Imagem
        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(this),
            new ImageCapture.OnImageSavedCallback() {
                @Override
                public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                    foto.setImageURI(outputFileResults.getSavedUri());
                    foto.setVisibility(View.VISIBLE);
                    Toast.makeText(getBaseContext(), "Imagem salva", Toast.LENGTH_SHORT).show();
                    database.uploadFotoPerfil(getBaseContext(), foto, docData, userEmail);

//                    // Iniciar a MainActivity e carregar o fragmento de perfil
//                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
//                    intent.putExtra("fragment_tela_perfil", "fragment_tela_perfil");
//                    startActivity(intent);
                }

                @Override
                public void onError(@NonNull ImageCaptureException exception) {
                    Log.e(TAG, "Photo capture failed: " + exception.getMessage());
                }
            });
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                // Used to bind the lifecycle of cameras to the lifecycle owner
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // Preview
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(viewFinder.getSurfaceProvider());

                // ImageCapture
                imageCapture = new ImageCapture.Builder().build();

                try {
                    // Unbind use cases before rebinding
                    cameraProvider.unbindAll();

                    // Bind use cases to camera
                    cameraProvider.bindToLifecycle(
                            this,
                            cameraSelector,
                            preview,
                            imageCapture
                    );
                } catch (Exception exc) {
                    Log.e(TAG, "Camera binding failed", exc);
                }

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    //Bloco de funções para gerenciar permissões
    private boolean allPermissionsGranted(){
        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this, permission)
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
                Toast.makeText(getApplicationContext(),"Permissão NEGADA. Tente novamente.",Toast.LENGTH_SHORT).show();
            } else {
                startCamera();
            }
        });
}