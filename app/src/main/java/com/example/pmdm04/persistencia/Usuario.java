package com.example.pmdm04.persistencia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Usuario")
public class Usuario implements Serializable {

    @PrimaryKey @NonNull
    public String usuario;

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getApelidos() {
        return apelidos;
    }

    public void setApelidos(String apelidos) {
        this.apelidos = apelidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String password;

    public String nome;

    public String apelidos;

    public boolean admin;

    public String email;


    public Usuario(@NonNull String usuario, String password, String nome, String apelidos, String email, boolean admin) {
        this.usuario=usuario;
        this.password=password;
        this.nome=nome;
        this.apelidos =apelidos;
        this.admin=admin;
        this.email=email;
    }

    @Ignore
    public Usuario(String usuario,String password) {
        this.usuario = usuario;
        this.password = password;
        this.admin = admin;
    }



    @Override
    public boolean equals(@Nullable Object obj) {

        if (this.usuario.equals(((Usuario)obj).usuario)&&this.password.equals((((Usuario)obj).password))) return true;
        else return false;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "password='" + password + '\'' +
                ", Usuario='" + usuario + '\'' +
                ", admin=" + admin +
                '}';
    }
}
