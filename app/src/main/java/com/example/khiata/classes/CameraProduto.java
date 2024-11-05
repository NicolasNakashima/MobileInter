package com.example.khiata.classes;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.khiata.databases_classes.DatabaseCamera;
import com.example.khiata.fragments.fragment_tela_cadastrar_produto;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraProduto extends AppCompatActivity {

    private fragment_tela_cadastrar_produto fragment_tela_cadastrar_produto = new fragment_tela_cadastrar_produto();
    private ExecutorService cameraExecutor;
    private android.widget.ImageView foto;
    private androidx.camera.view.PreviewView viewFinder;

    private Map<String, String> docData = new HashMap<>();
    private DatabaseCamera database = new DatabaseCamera();

    //Config camera
    private ImageCapture imageCapture;
    private CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;//Qual camera utilizar
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    //Log
    private static final String TAG = "CameraXGaleria";

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
        setContentView(R.layout.activity_camera_produto);
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
            String imgName = UUID.randomUUID().toString();
            takePhoto(imgName);
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
    }

    private ActivityResultLauncher<Intent> resultLauncherGaleria =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                Uri imageUri = result.getData().getData();
                if (imageUri != null) {
                    foto.setImageURI(imageUri);
                    foto.setVisibility(View.VISIBLE);
                }
            });

    private void takePhoto(String imgName) {

        if (imageCapture == null) {
            return;
        }

        // Definir nome e caminho da imagem
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, imgName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/CameraXGaleria");

        // Configurar opções de saída da imagem
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(
                getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
        ).build();

        // Configurar a orientação da imagem
        OrientationEventListener orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                int rotation;
                if (orientation >= 45 && orientation < 135) {
                    rotation = Surface.ROTATION_270;
                } else if (orientation >= 135 && orientation <= 224) {
                    rotation = Surface.ROTATION_180;
                } else if (orientation >= 225 && orientation <= 314) {
                    rotation = Surface.ROTATION_90;
                } else {
                    rotation = Surface.ROTATION_0;
                }
                imageCapture.setTargetRotation(rotation);
            }
        };
        orientationEventListener.enable();

        // Listener para capturar a imagem
        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        foto.setImageURI(outputFileResults.getSavedUri());
                        foto.setVisibility(View.VISIBLE);
                        Toast.makeText(getBaseContext(), "Imagem salva", Toast.LENGTH_SHORT).show();

                        // Fazer o upload da foto para o Firebase Storage
                        database.uploadFotoProduto(getBaseContext(), foto, docData, imgName);
                        Log.d("TAG", "ImgName: " + imgName);

                        // Implementar um mecanismo de espera antes de buscar a URL
                        Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            // Tentar obter a URL da imagem após um atraso
                            StorageReference profileRef = storageRef.child("khiata_produtos/" + imgName + ".jpg");
                            profileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                // Quando a URL da imagem for carregada com sucesso, iniciar a MainActivity
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("fragment", "cadastrar_produto");
                                intent.putExtra("imgName", imgName);
                                startActivity(intent);
                            }).addOnFailureListener(e -> {
                                Log.d("TAG", "Falha ao obter URL da imagem: " + e.getMessage());
                                Toast.makeText(getBaseContext(), "Erro ao carregar a imagem", Toast.LENGTH_SHORT).show();
                            });
                        }, 3000); // Atraso de 3 segundos para esperar que a imagem esteja disponível
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