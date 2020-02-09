package com.example.pmdm04;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pmdm04.adaptadores.AdaptadorListaPedidosAdmin;
import com.example.pmdm04.persistencia.BdInteraccion;
import com.example.pmdm04.persistencia.Usuario;

import java.util.ArrayList;

public class PedidosAdmin extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mI=this.getMenuInflater();
        mI.inflate(R.menu.menu_barra_accion_admin,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.abVerPedTram:
                lanzadorPedidos.putExtra("estado","creado");
                startActivity(lanzadorPedidos);
                finish();
                break;
            case R.id.abVerPedAcept:
                lanzadorPedidos.putExtra("estado","aceptado");
                startActivity(lanzadorPedidos);
                finish();
                break;
            case R.id.abVerPedRexeit:
                lanzadorPedidos.putExtra("estado","rexeitado");
                startActivity(lanzadorPedidos);
                finish();
                break;
            case R.id.abSair:
                Intent salir=new Intent(this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);;
                startActivity(salir);
                finish();
                break;
            case android.R.id.home:
                finish();
        }
        return true;
    }

    Intent lanzadorPedidos;
    String estado;
    Usuario usuarioLogueado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_pedidos_admin);

        lanzadorPedidos=this.getIntent();

        estado=lanzadorPedidos.getStringExtra("estado");

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        ActionBar ab=getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        usuarioLogueado=(Usuario)lanzadorPedidos.getSerializableExtra("usuario");
        ab.setTitle(usuarioLogueado.usuario);

        BdInteraccion bdI=new BdInteraccion(this);
        RecyclerView rv=(RecyclerView) findViewById(R.id.lista_pedidos_rec_view);;
        RecyclerView.LayoutManager lLM=new LinearLayoutManager(this);
        rv.setLayoutManager(lLM);

        AdaptadorListaPedidosAdmin aLPA;

        if (estado.equals("creado")){
            ((TextView)this.findViewById(R.id.textTituloPedidAdmin)).setText(R.string.textPedidosTramite);
        }
        else {
            if (estado.equals("aceptado")) ((TextView)this.findViewById(R.id.textTituloPedidAdmin)).setText(R.string.textTitulComprAcept);
            else if (estado.equals("rexeitado")) ((TextView)this.findViewById(R.id.textTituloPedidAdmin)).setText(R.string.textTitulComprRexeit);
        }

        aLPA=new AdaptadorListaPedidosAdmin((ArrayList)bdI.getPedidosEstado(estado),bdI);
        rv.setAdapter(aLPA);

    }
}
