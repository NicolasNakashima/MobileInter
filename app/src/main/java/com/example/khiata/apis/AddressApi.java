package com.example.khiata.apis;

import com.example.khiata.models.Address;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AddressApi {

    @POST("api/enderecos/inserir")
    Call<String> inserirEndereco(@Body Address address);

    @POST("api/enderecos/user/inserir/{userid}")
    Call<Address> inserirEnderecoUsuario(@Path("userid") String userid, @Body Address address);

    @DELETE("api/enderecos/deletar/{id}")
    Call<String> deletarEndereco(@Path("id") String id);

    @GET("api/enderecos/user/{userid}")
    Call<ArrayList<Address>> buscarEnderecosUsuario(@Path("userid") String userid);
}
