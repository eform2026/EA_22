package com.example.eform_mobile.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eform_mobile.R;
import com.example.eform_mobile.api.ApiClient;
import com.example.eform_mobile.api.ApiService;
import com.example.eform_mobile.models.Product;
import com.example.eform_mobile.utils.InputValidator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFormActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText priceEditText;
    private EditText stockEditText;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);

        nameEditText = findViewById(R.id.editTextProductName);
        descriptionEditText = findViewById(R.id.editTextProductDescription);
        priceEditText = findViewById(R.id.editTextProductPrice);
        stockEditText = findViewById(R.id.editTextProductStock);
        Button saveButton = findViewById(R.id.buttonSaveProduct);
        progressBar = findViewById(R.id.progressBarForm);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSaveProduct();
            }
        });
    }

    private void handleSaveProduct() {
        String name = nameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String priceText = priceEditText.getText().toString().trim();
        String stockText = stockEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Ingrese el nombre del producto");
            nameEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(description)) {
            descriptionEditText.setError("Ingrese la descripcion");
            descriptionEditText.requestFocus();
            return;
        }

        if (!InputValidator.isValidDecimal(priceText)) {
            priceEditText.setError("Ingrese un precio valido");
            priceEditText.requestFocus();
            return;
        }

        if (!InputValidator.isValidInteger(stockText)) {
            stockEditText.setError("Ingrese una cantidad de stock valida");
            stockEditText.requestFocus();
            return;
        }

        double price = Double.parseDouble(priceText);
        int stock = Integer.parseInt(stockText);

        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);

        Call<Product> call = apiService.createProduct(product);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ProductFormActivity.this, "Producto guardado correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ProductFormActivity.this, "No se pudo guardar el producto", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ProductFormActivity.this, "Error de conexion: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
