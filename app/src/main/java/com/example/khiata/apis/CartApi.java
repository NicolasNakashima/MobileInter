package com.example.khiata.apis;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface CartApi {
    //Rota para pegar os itens do carrinho de um usuário
    @GET("/redisCart")
    Call<List<List<String>>> getCartItens(@Query("cpf") String cpf);

    //Rota para inserir um novo item no carrinho
    @PUT("/updateRedisCart")
    Call<Void> updateCart(@Query("cpf") String cpf, @Query("quantidade") int quantidade, @Query("produto") String nomeProduto);

    //Rota para lançar o carrinho no MongoDB
    @PUT("/updateMongoCart")
    Call<Void> updateMongoCart(@Query("cpf") String cpf);

}
