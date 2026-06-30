package com.example.eform_mobile.api;

import com.example.eform_mobile.models.AuthResponse;
import com.example.eform_mobile.models.LoginRequest;
import com.example.eform_mobile.models.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @POST("auth/login")
    Call<AuthResponse> login(@Body LoginRequest loginRequest);

    @GET("products")
    Call<List<Product>> getProducts();

    @POST("products")
    Call<Product> createProduct(@Body Product product);
}
