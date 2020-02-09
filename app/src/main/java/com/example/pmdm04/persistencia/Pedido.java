package com.example.pmdm04.persistencia;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(foreignKeys = @ForeignKey(entity=Usuario.class,parentColumns = "usuario",childColumns = "usuario"), indices=@Index(value={"usuario"}, name="indexUserPed", unique=false) )
public class Pedido {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return id == pedido.id;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @PrimaryKey (autoGenerate = true)
    public int id;

    @NonNull
    public String usuario;

    @Ignore
    public Pedido(int id, @NonNull String usuario, int idProd, int cantidade, String direccion, String cidade, String codPost, String estado) {
        this.id = id;
        this.usuario = usuario;
        this.idProd = idProd;
        this.direccion = direccion;
        this.cidade = cidade;
        this.codPost = codPost;
        this.estado=estado;
        this.cantidade=cantidade;
    }

    public Pedido(@NonNull String usuario, @NonNull int idProd, int cantidade, @NonNull String direccion, @NonNull String cidade, @NonNull String codPost) {
        this.usuario = usuario;
        this.idProd = idProd;
        this.direccion = direccion;
        this.cidade = cidade;
        this.codPost = codPost;
        this.estado="creado";
        this.cantidade=cantidade;
    }

    @ForeignKey(entity = Producto.class,parentColumns = "id",childColumns = "idProd")
    public int idProd;

    
    public int cantidade;

    @NonNull
    public String direccion;
    @NonNull
    public String cidade;
    @NonNull
    public String codPost;

    @ColumnInfo(defaultValue = "creado")
    public String estado;


}
