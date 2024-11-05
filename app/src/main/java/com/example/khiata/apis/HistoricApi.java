package com.example.khiata.apis;

import com.example.khiata.models.Historic;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HistoricApi {
    //Rota para buscar o historico de compras de um usuario
    @GET("/historyOrder")
    Call<List<Historic>> getHistoric(@Query("cpf") String cpf);
}
