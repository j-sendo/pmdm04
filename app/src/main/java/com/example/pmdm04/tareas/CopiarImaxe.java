package com.example.pmdm04.tareas;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.pmdm04.persistencia.Usuario;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CopiarImaxe extends AsyncTask {
    Bitmap foto;
    Usuario usuario;
    Context contexto;
    public CopiarImaxe(Context contexto, Bitmap foto, Usuario usuario) {
        this.contexto=contexto;
        this.foto=foto;
        this.usuario=usuario;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        try {
            File ficheiro=new File(contexto.getFilesDir(),usuario.usuario);
            FileOutputStream fOS=new FileOutputStream(ficheiro);
            foto.compress(Bitmap.CompressFormat.JPEG,80,fOS); //!!!!!!!!!!!!!!!!!!!!!!!!!!!
            fOS.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
