package com.example.khiata.apis;

import com.example.khiata.models.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductApi {

    @POST("/insert/product")
    Call<String> insertProduct(
            @Query("id") String id,
            @Query("name") String name,
            @Query("price") String price,
            @Query("imageurl") String imageurl,
            @Query("typeId") String typeId,
            @Query("dressmarker") String dressmarker,
            @Query("avaliation") String avaliation
    );

    @DELETE("/delete")
    Call<String> deleteProduct(@Query("id") int id);

    @GET("/get/name")
    Call<String> getByName(@Query("name") String name);

    @GET("/get/dressmarker")
    Call<List<Product>> getProductsByDressmarker(@Query("dressmarker") String dressmarker);
}
