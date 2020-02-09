package com.example.pmdm04.persistencia;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductoDao {

    @Query("SELECT * FROM Producto WHERE categoria LIKE :categoria")
    List<Producto> getProductosPorCat(String categoria);
    @Query("SELECT DISTINCT categoria FROM Producto")
    List<String> getCategorias();
    @Insert
    void insertarProducto(Producto product);


}
