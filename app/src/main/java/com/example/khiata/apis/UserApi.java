package com.example.khiata.apis;

import com.example.khiata.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {

    @POST("api/users/inserir")
    Call<ResponseBody> inserirUsuario(@Body User user);

    @GET("api/users/selecionar/email/{email}")
    Call<User> buscarUsuarioPorEmail(@Path("email") String email);

    @PATCH("api/users/atualizar/email/{email}")
    Call<Void> atualizarUsuario(@Path("email") String email, @Body Map<String, Object> atualizacoes);

    @GET("api/users/selecionar")
    Call<ArrayList<User>> selecionarTodos();

    @PATCH("api/users/atualizar-preferencias/{userId}")
    Call<Void> atualizarPreferencias(@Path("userId") int userId, @Body Map<String, Object> atualizacoes);

}
