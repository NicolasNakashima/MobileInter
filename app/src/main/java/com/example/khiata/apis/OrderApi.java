package com.example.khiata.apis;

import com.example.khiata.models.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface OrderApi {
    //Rota para buscar o historico de compras de um usuario
    @GET("/historyOrder")
    Call<List<Order>> getHistoric(@Query("cpf") String cpf);

    //Rota para alterar o status de um pedido
    @PUT("/alterStatus")
    Call<Void> alterStatus(@Query("cpf") String cpf, @Query("status") String status);

    //Rota para criar um pedido
    @POST("/createOrder")
    Call<String> createOrder(@Body Order order);
}
