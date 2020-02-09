package com.example.pmdm04;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.pmdm04.persistencia.Usuario;

import java.io.File;


public class Actividad_admin_menu extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflador=getMenuInflater();
        inflador.inflate(R.menu.menu_barra_accion_admin,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.abVerPedTram:
                lanzarPedidsTramite();
                break;
            case R.id.abVerPedAcept:
                lanzarPedidsAceptados();
                break;
            case R.id.abVerPedRexeit:
                lanzarPedidsRexeitados();
                break;
            case R.id.abSair:
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==-1) {
            Toast resultadoPedido= Toast.makeText(this.getBaseContext(),data.getStringExtra("resultado"),Toast.LENGTH_LONG);
            resultadoPedido.show();
        }else if(requestCode==2&&resultCode==-1){
        finish();
    }
    }

    Intent lanzadorPedidos;
    Usuario usuarioLogueado;
    Intent lanzadorPerfilUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_admin_menu);

        usuarioLogueado =(Usuario)this.getIntent().getSerializableExtra("usuario");

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle(usuarioLogueado.usuario);

        TextView nomeUsuario=findViewById(R.id.nomeusuario);
        nomeUsuario.setText(usuarioLogueado.nome+" "+usuarioLogueado.apelidos);

//IMAXE
        ImageView fotoUser=this.findViewById(R.id.fotousuario);
        File arquivoFotoUser=new File(this.getFilesDir(),usuarioLogueado.usuario);
        if (arquivoFotoUser.exists()) {
            Bitmap imaxe = BitmapFactory.decodeFile(arquivoFotoUser.getAbsolutePath()); //!!!!!!!!!!!!!!!!!!!!!!!!
            fotoUser.setImageBitmap(imaxe);
        }
//FIN IMAXE
        Button perfilUsuario=findViewById(R.id.botonPerfilUsuario);
        Button verTramite=findViewById(R.id.botonadminverpedtramite);
        Button verAcepta=findViewById(R.id.botonadminverpedaceptad);
        Button verRexeita=findViewById(R.id.botonadminverpedrexeita);
        Button sair=findViewById(R.id.botonsair);

        lanzadorPedidos=new Intent(this,PedidosAdmin.class);
        lanzadorPedidos.putExtra("usuario",usuarioLogueado);
        lanzadorPerfilUsuario=new Intent(this,ModificarUsuario.class);

        perfilUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lanzarPerfilUsuario();
            }
        });

        verTramite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lanzarPedidsTramite();
            }
        });

        verAcepta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lanzarPedidsAceptados();
            }
        });

        verRexeita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lanzarPedidsRexeitados();
            }
        });

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void lanzarPerfilUsuario(){
        lanzadorPerfilUsuario.putExtra("usuario",usuarioLogueado);
        startActivityForResult(lanzadorPerfilUsuario,2);
    }
    private void lanzarPedidsTramite(){
        lanzadorPedidos.putExtra("estado","creado");
        startActivity(lanzadorPedidos);
    }
    private void lanzarPedidsAceptados(){
        lanzadorPedidos.putExtra("estado","aceptado");
        startActivity(lanzadorPedidos);
    }
    private void lanzarPedidsRexeitados(){
        lanzadorPedidos.putExtra("estado","rexeitado");
        startActivity(lanzadorPedidos);
    }
}
