package com.example.khiata.apis;

import com.example.khiata.models.User;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {

    @POST("api/users/inserir")
    Call<String> inserirUsuario(@Body User user);

    @GET("api/users/selecionar/email/{email}")
    Call<User> buscarUsuarioPorEmail(@Path("email") String email);

    @PATCH("api/users/atualizar/email/{email}")
    Call<Void> atualizarUsuario(@Path("email") String email, @Body Map<String, Object> atualizacoes);

}
