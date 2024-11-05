package com.example.khiata.apis;

import com.example.khiata.models.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryApi {
    //Rota para buscar todas as categorias
    @GET("/category")
    Call<List<Category>> getCategories();
}
