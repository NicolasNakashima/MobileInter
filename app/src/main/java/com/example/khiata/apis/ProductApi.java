package com.example.khiata.apis;

import com.example.khiata.models.Product;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ProductApi {

    @POST("/insert/product?id={id}&name={name}&price={price}&imageurl={imageurl}&typeId={typeid}&dressmarker={dressmarker}&avaliation={avaliation}")
    Call<String> insertProduct(String id, String name, String price, String imageurl, String typeId, String dressmarker, String avaliation);

    @DELETE("/delete?id={id}")
    Call<String> deleteProduct(String id);

    @GET("/get/name?name={name}")
    Call<Product> getByName(String name);

    @GET("/get/dressmarker?dressmarker={dressmarker}")
    Call<ArrayList<Product>> getProductsByDressmarker(String dressmarker);
}
