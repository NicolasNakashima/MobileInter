package com.example.khiata.apis;

import com.example.khiata.models.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Cart {
    @GET("/redisCart")
    Call<List<List<String>>> getCartItens(@Query("cpf") String cpf);

    @POST("/updateRedisCart")
    Call<Void> updateCart(@Query("cpf") String cpf, @Query("quantidade") int quantidade, @Query("produto") String nomeProduto);

    @POST("/updateMongoCart")
    Call<Void> updateMongoCart(@Query("cpf") String cpf);

}
