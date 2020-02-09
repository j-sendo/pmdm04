package com.example.pmdm04;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pmdm04.persistencia.BdInteraccion;
import com.example.pmdm04.persistencia.Pedido;
import com.example.pmdm04.persistencia.Producto;
import com.example.pmdm04.persistencia.Usuario;

public class DireccionPedido extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case (android.R.id.home):
                finish();
                break;
        }
        return true;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_direccion_pedido);

        final BdInteraccion bd=BdInteraccion.get(this.getApplicationContext());

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        ActionBar ab=getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        final Usuario usuarioLogueado=(Usuario)this.getIntent().getSerializableExtra("usuario");
        final Intent voltaMenuUsuario=new Intent(); //Actividad_usuario_menu.class
        voltaMenuUsuario.putExtra("usuario",usuarioLogueado);

        final StringBuilder resultado=new StringBuilder();
        final Intent productoSel=this.getIntent();

        final EditText enderezo=findViewById(R.id.editTextDireccion);
        final EditText cidade=findViewById(R.id.editTextCidade);
        final EditText cp=findViewById(R.id.editTextCP);

        Button finalizar=findViewById(R.id.botonFinalizar);

        final Builder creadorDialogo=new Builder(DireccionPedido.this); //con this.getApplicationContext() da erro.
        creadorDialogo.setTitle("Resumo pedido");
        creadorDialogo.setPositiveButton("Tramitar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                resultado.insert(0,"Pedido realizado. Detalles: \n");
                voltaMenuUsuario.putExtra("resultado",resultado.toString());

                setResult(RESULT_OK,voltaMenuUsuario);
                finish();
            }
        });
        creadorDialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                resultado.delete(0,resultado.length());
            }
        });

        finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enderezo.getText().length()!=0&&cidade.getText().length()!=0&&cp.getText().length()!=0) {
                    resultado.append(productoSel.getSerializableExtra("producto") + " - Cantidade: " + productoSel.getIntExtra("cantidade", 0) + "\n");
                    resultado.append("Enderezo: " + enderezo.getText() + "\n");
                    resultado.append("Cidade: " + cidade.getText() + " - ");
                    resultado.append("CP.: " + cp.getText());

                    Producto producto=(Producto)productoSel.getSerializableExtra("producto");
                    Pedido pedidoReal=new Pedido(usuarioLogueado.usuario, producto.id,productoSel.getIntExtra("cantidade",0), enderezo.getText().toString(), cidade.getText().toString(), cp.getText().toString());

                    bd.insertarPedido(pedidoReal);
                    creadorDialogo.setMessage(resultado);
                    creadorDialogo.show();

                }
                else {
                    Toast erro = Toast.makeText(view.getContext(), "Debe cubrir todos os campos", Toast.LENGTH_LONG);
                    erro.show();
                }
            }
        });
    }
}
