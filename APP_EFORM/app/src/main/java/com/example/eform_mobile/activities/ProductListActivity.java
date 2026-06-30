package com.example.eform_mobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eform_mobile.R;
import com.example.eform_mobile.adapters.ProductAdapter;
import com.example.eform_mobile.api.ApiClient;
import com.example.eform_mobile.api.ApiService;
import com.example.eform_mobile.models.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListActivity extends AppCompatActivity {

    private ProductAdapter productAdapter;
    private ProgressBar progressBar;
    private TextView emptyTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewProducts);
        progressBar = findViewById(R.id.progressBarProducts);
        emptyTextView = findViewById(R.id.textViewEmpty);
        FloatingActionButton addProductButton = findViewById(R.id.fabAddProduct);

        productAdapter = new ProductAdapter(new ArrayList<Product>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(productAdapter);

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductListActivity.this, ProductFormActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchProductList();
    }

    private void fetchProductList() {
        progressBar.setVisibility(View.VISIBLE);
        emptyTextView.setVisibility(View.GONE);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Product>> call = apiService.getProducts();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> products = response.body();
                    productAdapter.updateProducts(products);
                    if (products.isEmpty()) {
                        emptyTextView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(ProductListActivity.this, "No se pudo cargar la lista de productos", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ProductListActivity.this, "Error de conexion: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
