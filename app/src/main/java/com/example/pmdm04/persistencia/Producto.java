package com.example.pmdm04.persistencia;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Producto implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String nome;

    @NonNull
    public String categoria;

    public Producto(@NonNull String nome, @NonNull String categoria) {
        this.nome = nome;
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return nome;
    }
}
