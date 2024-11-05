package com.example.khiata.apis;

import com.example.khiata.models.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductApi {

    @POST("/insert/product")
    Call<String> insertProduct(
            @Query("name") String name,
            @Query("price") double price,
            @Query("imageurl") String imageurl,
            @Query("typeId") int typeId,
            @Query("dressmarker") String dressmarker,
            @Query("avaliation") double avaliation,
            @Query("description") String description,
            @Query("size") String size
    );

    @GET("/get/category")
    Call<List<Product>> getProductsByCategory(@Query("category") String category);

    @DELETE("/delete")
    Call<String> deleteProduct(@Query("id") int id);

    @GET("/get/name")
    Call<List<Product>> getByName(@Query("name") String name);

    @GET("/get/dressmarker")
    Call<List<Product>> getProductsByDressmarker(@Query("dressmarker") String dressmarker);

    @PATCH("/update/id")
    Call<String> updateProduct(@Query("id") int id, @Query("dressmarker") String dressmarker);
}
