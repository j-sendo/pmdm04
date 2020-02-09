package com.example.pmdm04;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Patterns;
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


import java.io.FileNotFoundException;

public class ActividadRexistro extends AppCompatActivity {
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    Bitmap foto;
    ImageView imaxeUsuario;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK){
            Uri rutaImaxe=data.getData();
            try {
                foto=BitmapFactory.decodeStream(this.getContentResolver().openInputStream(rutaImaxe)); //!!!!!!!
                imaxeUsuario.setImageBitmap(foto);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else if (requestCode==2 && resultCode==RESULT_OK){
            foto= (Bitmap) data.getExtras().get("data");
            imaxeUsuario.setImageBitmap(foto);

        }
    }
   protected void onSaveInstanceState(Bundle outState) {
       super.onSaveInstanceState(outState);
       outState.putParcelable("foto",foto);
   }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_rexistro);


        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent actChamante=this.getIntent();
        final BdInteraccion bd=BdInteraccion.get(this.getApplicationContext());

        final EditText introUser=this.findViewById(R.id.introUser);
        final EditText introContra=this.findViewById(R.id.introContra);
        final EditText introNome=this.findViewById(R.id.introNome);
        final EditText introApel=this.findViewById(R.id.introApel);
        final EditText introEmail=this.findViewById(R.id.introEmail);
        final RadioGroup adminSel=this.findViewById(R.id.radioGroupTipoUser);

        imaxeUsuario=findViewById(R.id.imageUsuario);
        Button selImaxe=this.findViewById(R.id.butSelImaxe);
        Button botRexistrar=this.findViewById(R.id.botonAceptar);

       if (savedInstanceState!=null) foto=savedInstanceState.getParcelable("foto");
        if (foto!=null) imaxeUsuario.setImageBitmap(foto);
        //IMAXE
        selImaxe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu pUM=new PopupMenu(ActividadRexistro.this,findViewById(R.id.butSelImaxe));
                MenuInflater mI=pUM.getMenuInflater();
                mI.inflate(R.menu.menu_foto,pUM.getMenu());
                pUM.show();
                pUM.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.fotodendearquivo:
                                Intent selectorArchivos=new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                selectorArchivos.setType("image/*");
                                startActivityForResult(selectorArchivos,1);
                                break;
                            case R.id.fotodendecamara:
                                Intent sacarFoto=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(sacarFoto,2);
                                break;

                        }
                        return true;
                    }
                });


            }
        });
        //FIN IMAXE

        botRexistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stringUsuario, stringContra, stringNome, stringApel, stringEmail;

                boolean bolAdmin = false;
                stringUsuario = introUser.getText().toString();
                stringContra = introContra.getText().toString();
                stringNome = introNome.getText().toString();
                stringApel = introApel.getText().toString();
                stringEmail = introEmail.getText().toString();
                if (adminSel.getCheckedRadioButtonId() == R.id.radioButAdmin) bolAdmin = true;

                if (stringUsuario.isEmpty() || stringContra.isEmpty() || stringNome.isEmpty() || stringApel.isEmpty() || stringEmail.isEmpty())
                    Toast.makeText(view.getContext(), "ERRO. Debe cubrir todos os campos", Toast.LENGTH_LONG).show();
                else {
                    if (stringEmail.matches(Patterns.EMAIL_ADDRESS.pattern())) {
                        Usuario introUser = new Usuario(stringUsuario, stringContra, stringNome, stringApel, stringEmail, bolAdmin);
                        try {
                            bd.insertarUsuario(introUser);
                            if (foto!=null) {
                                new CopiarImaxe(ActividadRexistro.this,foto,introUser).execute(new Object());
                            }
                            actChamante.putExtra("usuarioCreado", stringUsuario);
                            setResult(RESULT_OK, actChamante);
                            finish();
                        } catch (SQLiteConstraintException e) {
                            AlertDialog.Builder alertaUserExist = new AlertDialog.Builder(ActividadRexistro.this, 0);

                            alertaUserExist.setTitle("Erro");
                            alertaUserExist.setMessage("O usuario indicado xa existe");
                            alertaUserExist.setPositiveButton("ok", null);
                            alertaUserExist.show();
                        }
                    } else Toast.makeText(view.getContext(), "ERRO. Formato de email incorrecto", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
