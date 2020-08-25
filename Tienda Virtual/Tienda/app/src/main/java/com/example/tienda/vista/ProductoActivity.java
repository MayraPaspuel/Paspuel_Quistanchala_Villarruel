package com.example.tienda.vista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tienda.R;
import com.example.tienda.modelo.Modelo;
import com.example.tienda.modelo.Producto;

import java.util.ArrayList;

public class ProductoActivity extends AppCompatActivity {

    ImageView imagen;
    TextView nombreProducto, descripcion, precio, vendedor;
    Button comprar;
    Modelo modelo = new Modelo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Producto");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        final String productoId = intent.getStringExtra("id");

        imagen = findViewById(R.id.imgImagen);
        nombreProducto = findViewById(R.id.txtNombreProducto);
        descripcion = findViewById(R.id.txtDescripcion);
        precio = findViewById(R.id.txtPrecio);
        vendedor = findViewById(R.id.txtVendedor);

        comprar = findViewById(R.id.btnComprar);

        comprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelo.getVendedorId(ProductoActivity.this, productoId);
            }
        });



        modelo.buscarProducto(ProductoActivity.this, productoId, imagen, nombreProducto, descripcion,precio,vendedor);

    }
}