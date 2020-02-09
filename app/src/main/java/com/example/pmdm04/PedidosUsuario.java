package com.example.pmdm04;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pmdm04.adaptadores.AdaptadorListaPedidos;
import com.example.pmdm04.persistencia.BdInteraccion;
import com.example.pmdm04.persistencia.Usuario;

import java.util.ArrayList;

public class PedidosUsuario extends AppCompatActivity {

    Intent lanzPedidsUser,lanzadorActividadPedido;
    Usuario usuarioLogueado;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mI=this.getMenuInflater();
        mI.inflate(R.menu.menu_barra_accion_usuario,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.abFacerPedido:
                lanzarActividadPedido();
                break;
            case R.id.abVerPedTram:
                lanzarPedidsTramite();
                break;
            case R.id.abVerComReal:
                lanzarComprasReal();
                break;
            case R.id.abSair:
                Intent salir=new Intent(this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(salir);
                finish();
                break;
            case android.R.id.home:
                finish();
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_pedidos_usuario);

        final BdInteraccion bd=BdInteraccion.get(this.getApplicationContext());

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar ab=getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        usuarioLogueado=(Usuario)this.getIntent().getSerializableExtra("usuario");

        ab.setTitle(usuarioLogueado.usuario);
        String estado=this.getIntent().getStringExtra("estado");

        if (estado.equals("creado")) ((TextView)this.findViewById(R.id.textTituloPedidUser)).setText(R.string.textPedidosTramite);
        else if (estado.equals("aceptado")) ((TextView)this.findViewById(R.id.textTituloPedidUser)).setText(R.string.textTitulComprReal);

        RecyclerView rv=(RecyclerView) findViewById(R.id.lista_pedidos_rec_view);

        RecyclerView.LayoutManager lLM=new LinearLayoutManager(this);
        rv.setLayoutManager(lLM);

        BdInteraccion bdI=new BdInteraccion(this);
        AdaptadorListaPedidos aLP=new AdaptadorListaPedidos((ArrayList)bdI.getPedidosUsuario(usuarioLogueado.usuario,estado),bd);
        rv.setAdapter(aLP);

    }
    private void lanzarActividadPedido(){
            lanzadorActividadPedido=new Intent(this,Actividad_pedido_productos.class);
            lanzadorActividadPedido.putExtra("usuario",usuarioLogueado);
            startActivityForResult(lanzadorActividadPedido,1);

        }
    private void lanzarPedidsTramite(){
        lanzPedidsUser=new Intent(this,PedidosUsuario.class);
        lanzPedidsUser.putExtra("usuario",usuarioLogueado);
        lanzPedidsUser.putExtra("estado","creado");
        startActivity(lanzPedidsUser);
        finish();
    }
    private void lanzarComprasReal(){
        lanzPedidsUser=new Intent(this,PedidosUsuario.class);
        lanzPedidsUser.putExtra("usuario",usuarioLogueado);
        lanzPedidsUser.putExtra("estado","aceptado");
        startActivity(lanzPedidsUser);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==-1) {
            Toast resultadoPedido= Toast.makeText(this.getBaseContext(),data.getStringExtra("resultado"),Toast.LENGTH_LONG);
            resultadoPedido.show();
            finish();
        }
    }
}
