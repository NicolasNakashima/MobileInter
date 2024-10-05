package com.example.khiata.apis;

import com.example.khiata.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {

    @POST("api/users/inserir")
    Call<Void> inserirUsuario(@Body User user);

    @GET("api/users/selecionar/email/{email}")
    Call<User> buscarUsuarioPorEmail(@Path("email") String email);
}
