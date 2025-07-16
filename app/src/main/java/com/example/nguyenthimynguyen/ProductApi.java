package com.example.nguyenthimynguyen;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface ProductApi {
    @GET("products")
    Call<List<Product>> getAllProducts();

    @GET("products/{id}")
    Call<Product> getProductById(@Path("id") String id);
}