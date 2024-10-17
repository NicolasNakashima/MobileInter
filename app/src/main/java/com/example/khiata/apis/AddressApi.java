package com.example.khiata.apis;

import com.example.khiata.models.Address;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AddressApi {
    @POST("api/enderecos/user/inserir/{userid}")
    Call<Address> inserirEnderecoUsuario(@Path("userid") int userid, @Body Address address);

    @DELETE("api/enderecos/deletar/{id}")
    Call<String> deletarEndereco(@Path("id") int id);

    @GET("api/enderecos/user/{userid}")
    Call<ArrayList<Address>> buscarEnderecosUsuario(@Path("userid") int userid);

    @GET("api/enderecos/selecionar/id/{id}")
    Call<Address> selecionarEnderecoPorId(@Path("id") int id);

    @PUT("api/enderecos/atualizar/{id}")
    Call<String> atualizarEndereco(@Path("id") int id, @Body Address address);

    @PATCH("api/enderecos/atualizar/id/{id}")
    Call<Void> atualizarEnderecoPorId(@Path("id") int id, @Body Map<String, Object> atualizacoes);
}
