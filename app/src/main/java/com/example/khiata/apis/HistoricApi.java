package com.example.khiata.apis;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HistoricApi {
    @GET("/historyOrder")
    Call<List<String>> getHistoric(@Query("cpf") String cpf);
}
