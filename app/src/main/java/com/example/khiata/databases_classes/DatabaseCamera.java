package com.example.khiata.databases_classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class DatabaseCamera {

    //Upload da foto de perfil no Firebase
    public void uploadFotoPerfil(Context c, ImageView foto, Map<String, String> docData, String userName){
        //Conversão
        Bitmap bitmap = ((BitmapDrawable)foto.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] databyte = baos.toByteArray();

        //Abrir Database e definindo o caminho da imagem
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storage.getReference("khiata_perfis").child("foto_"+userName+".jpg").putBytes(databyte).
            addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(c, "Imagem salva", Toast.LENGTH_SHORT).show();
                    //Obter a URL da imagem
                    taskSnapshot.getMetadata().getReference().getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Toast.makeText(c, uri.toString(), Toast.LENGTH_SHORT).show();
                                docData.put("url", uri.toString());
                            }
                        });
                }
            });
    }

    //Download da foto de perfil
    public void downloadFotoPerfil(ImageView img, Uri urlFirebase){
        img.setRotation(0);
        Glide.with(img.getContext()).asBitmap().load(urlFirebase).into(img);
    }

    //Upload da foto do produto no Firebase
    public void uploadFotoProduto(Context c, ImageView foto, Map<String, String> docData, String ProductName){
        //Conversão
        Bitmap bitmap = ((BitmapDrawable)foto.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] databyte = baos.toByteArray();

        //Abrir Database e definindo o caminho da imagem
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storage.getReference("khiata_produtos").child(ProductName+".jpg").putBytes(databyte).
            addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(c, "Imagem salva", Toast.LENGTH_SHORT).show();
                    //Obter a URL da imagem
                    taskSnapshot.getMetadata().getReference().getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Toast.makeText(c, uri.toString(), Toast.LENGTH_SHORT).show();
                                docData.put("url", uri.toString());
                            }
                        });
                }
            });
    }

    //Download da foto do produto
    public void downloadFotoProduto(ImageView img, Uri urlFirebase){
        img.setRotation(0);
        Glide.with(img.getContext()).asBitmap().load(urlFirebase).into(img);
    }
}
