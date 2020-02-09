package com.example.pmdm04;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pmdm04.persistencia.BdInteraccion;
import com.example.pmdm04.persistencia.Usuario;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK){
            Toast.makeText(this.getApplicationContext(),"Usuario "+data.getStringExtra("usuarioCreado")+" creado correctamente.",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BdInteraccion bd=BdInteraccion.get(this.getApplicationContext());
        final Intent logueoUsuario=new Intent(this, Actividad_usuario_menu.class);
        final Intent logueoAdmin=new Intent(this, Actividad_admin_menu.class);
        final Intent rexistroUsuario=new Intent(this.getApplicationContext(), ActividadRexistro.class);


        Button botonLogueo=this.findViewById(R.id.botonAcceder);
        Button botonRexistro=this.findViewById(R.id.botonRexistrarse);
        botonLogueo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nomeUsuario=findViewById(R.id.editTextUsuario);
                EditText passwordUsuario=findViewById(R.id.editTextContrasinal);

                Usuario introUser=new Usuario(nomeUsuario.getText().toString(),passwordUsuario.getText().toString());
                Usuario resUser=bd.getUsuario(nomeUsuario.getText().toString());

                boolean accesoOk=false;

                if (resUser!=null){
                    if (resUser.password.equals(introUser.password)){
                        accesoOk=true;
                        if (resUser.admin) {
                            logueoAdmin.putExtra("usuario",resUser);
                            startActivity(logueoAdmin);
                        }
                        else {
                            logueoUsuario.putExtra("usuario",resUser);
                            startActivity(logueoUsuario);
                        }
                    }
                }

                if (!accesoOk) {
                    Toast.makeText(view.getContext(), "Datos de acceso incorrectos.", Toast.LENGTH_LONG).show();
                    nomeUsuario.setText("");
                    passwordUsuario.setText("");
                    nomeUsuario.requestFocus();
                }

            }
        });
        botonRexistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(rexistroUsuario,1);
            }
        });
    }
}
