package com.example.khiata.apis;

import com.example.khiata.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserApi {

    @POST("api/users/inserir")
    Call<Void> inserirUsuario(@Body User user);
}
