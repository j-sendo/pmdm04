package com.example.pmdm04;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;

import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioGroup;

import android.widget.Toast;

import com.example.pmdm04.persistencia.BdInteraccion;
import com.example.pmdm04.persistencia.Usuario;
import com.example.pmdm04.tareas.CopiarImaxe;

import java.io.File;
import java.io.FileNotFoundException;


public class ModificarUsuario extends AppCompatActivity {
    Usuario usuarioLogueado;
    Bitmap foto;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("foto",foto);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK && requestCode==1){
            try {
                foto=BitmapFactory.decodeStream(this.getContentResolver().openInputStream(data.getData()));
                imaxeUsuario.setImageBitmap(foto);
                Toast.makeText(this.getBaseContext(),"Nova imaxe de usuario seleccionada, deberá gardar os cambios.", Toast.LENGTH_LONG);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else if (resultCode==RESULT_OK && requestCode==2){
            foto=(Bitmap) data.getExtras().get("data");
            imaxeUsuario.setImageBitmap(foto);
        }

    }
    ImageView imaxeUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_usuario);
        Intent r=this.getIntent();
        usuarioLogueado=(Usuario)r.getSerializableExtra("usuario");

        final BdInteraccion bd=BdInteraccion.get(this);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        ActionBar ab=getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Perfil: "+usuarioLogueado.usuario);

        final EditText introNome=this.findViewById(R.id.introNome);
        final EditText introApel=this.findViewById(R.id.introApel);
        final EditText introEmail=this.findViewById(R.id.introEmail);
        final RadioGroup adminSel=this.findViewById(R.id.radioGroupTipoUser);

        imaxeUsuario=this.findViewById(R.id.imageUsuario);

        File arquivUserImage=new File(getFilesDir(),usuarioLogueado.usuario);

        if (savedInstanceState!=null) foto=savedInstanceState.getParcelable("foto");
        if (foto!=null) imaxeUsuario.setImageBitmap(foto);
        else if (arquivUserImage.exists())imaxeUsuario.setImageBitmap(BitmapFactory.decodeFile(arquivUserImage.getPath()));

        introNome.setText(usuarioLogueado.nome);
        introApel.setText(usuarioLogueado.apelidos);
        introEmail.setText(usuarioLogueado.email);

        Button cambiarContra=findViewById(R.id.botonCambioContra);
        Button botfotoPerfil=findViewById(R.id.butSelImaxe);
        Button gardarCambios=findViewById(R.id.botonGardarCambios);

        botfotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarFotoPerfilUsuario();
            }
        });
        cambiarContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarContraseña();
            }
        });

        gardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome,apelidos,email;
                nome=introNome.getText().toString();
                apelidos=introApel.getText().toString();
                email=introEmail.getText().toString();

                if (nome.length()==0||apelidos.length()==0||email.length()==0) {
                    Toast.makeText(ModificarUsuario.this,"Debe cubrir todos os campos.", Toast.LENGTH_LONG).show();
                    return;
                }
                usuarioLogueado.setNome(nome);
                usuarioLogueado.setApelidos(apelidos);
                usuarioLogueado.setEmail(email);
                bd.actualizarUsuario(usuarioLogueado);
                if (foto!=null) {
                    new CopiarImaxe(ModificarUsuario.this,foto,usuarioLogueado).execute(new Object());
                }
                Toast.makeText(ModificarUsuario.this,"Usuario actualizado. Debe volver loguearse.", Toast.LENGTH_LONG).show();
                setResult(-1);
                finish();
            }
        });

    }

    private void cambiarFotoPerfilUsuario() {
        PopupMenu pUM=new PopupMenu(ModificarUsuario.this,findViewById(R.id.butSelImaxe));
        MenuInflater mI=pUM.getMenuInflater(); //!!!!!
        mI.inflate(R.menu.menu_foto,pUM.getMenu());
        pUM.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.fotodendecamara:
                        Intent sacarFoto=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(sacarFoto,2);
                        break;
                    case R.id.fotodendearquivo:
                        Intent obterArquivoImaxe=new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        obterArquivoImaxe.setType("image/*");
                        startActivityForResult(obterArquivoImaxe,1);
                        break;
                }
                return true;
            }
        });
        pUM.show();
    }

    private void cambiarContraseña(){
        AlertDialog.Builder dialogoContraseña=new AlertDialog.Builder(this);
        LayoutInflater inflador= this.getLayoutInflater();
        final View vistaDialogContra=inflador.inflate(R.layout.vista_cambio_contra,null);
        dialogoContraseña.setView(vistaDialogContra);
        dialogoContraseña.setNegativeButton("Cancelar",null);
        dialogoContraseña.setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                EditText contra1=vistaDialogContra.findViewById(R.id.editContra1);
                EditText contra2=vistaDialogContra.findViewById(R.id.editContra2);
                String scontra1=contra1.getText().toString();
                String scontra2=contra2.getText().toString();
                if (scontra1.length()==0) {
                    Toast.makeText(ModificarUsuario.this,"Erro, non introduciu ningún contrasinal.",Toast.LENGTH_LONG).show();
                }else if (!scontra1.equals(scontra2)){
                    Toast.makeText(ModificarUsuario.this,"Erro. Os contrasinais non coinciden.",Toast.LENGTH_LONG).show();
                }else {
                    usuarioLogueado.setPassword(scontra2);
                    Toast.makeText(ModificarUsuario.this,"Debe gardar os cambios para aplicar o novo contrasinal.", Toast.LENGTH_LONG).show();
                }
            }
        });
        dialogoContraseña.show();
    }
}
