package com.example.pmdm04;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Spinner;

import com.example.pmdm04.persistencia.BdInteraccion;
import com.example.pmdm04.persistencia.Producto;
import com.example.pmdm04.persistencia.Usuario;

import java.io.Serializable;
import java.util.List;


public class Actividad_pedido_productos extends AppCompatActivity {


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return true;
    }


    Spinner spinnerCategoria;
    Spinner spinnerProducto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.actividad_pedido_productos);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        final Usuario usuarioLogueado=(Usuario)this.getIntent().getSerializableExtra("usuario");

        ab.setTitle(usuarioLogueado.usuario);

        spinnerCategoria = this.findViewById(R.id.spinnercategoria);
        spinnerProducto = this.findViewById(R.id.spinnerproducto);

        final BdInteraccion bd=new BdInteraccion(this);

        spinnerCategoria.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,bd.getCategorias()));

        final Spinner cantidad=findViewById(R.id.spinnerCantidade);
        Integer[] cantidadPosible={1,2,3,4,5};
        cantidad.setAdapter(new ArrayAdapter<>(this.getBaseContext(),android.R.layout.simple_spinner_dropdown_item,cantidadPosible));


        final Context contexto=this.getBaseContext();//Evitar caída al cambiar la orientación.

        spinnerCategoria.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            List<Producto> productos=bd.getProductosPorCat((String)adapterView.getSelectedItem());
                            spinnerProducto.setAdapter(new ArrayAdapter(Actividad_pedido_productos.this,android.R.layout.simple_spinner_dropdown_item,productos));

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                }
        );

        Button seguinte=findViewById(R.id.botonSeguintePed);
        final Intent lanzadorDirecPedido=new Intent(this,DireccionPedido.class);
        seguinte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lanzadorDirecPedido.putExtra("producto",(Serializable)spinnerProducto.getSelectedItem());
                lanzadorDirecPedido.putExtra("usuario",usuarioLogueado);
                lanzadorDirecPedido.putExtra("cantidade",Integer.parseInt(cantidad.getSelectedItem().toString()));

                startActivityForResult(lanzadorDirecPedido,1);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == -1) {
            setResult(resultCode, data);
            finish();
        }
    }
}
