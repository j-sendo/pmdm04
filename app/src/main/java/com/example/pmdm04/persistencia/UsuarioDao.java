package com.example.pmdm04.persistencia;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UsuarioDao {
    @Query("SELECT * FROM Usuario WHERE usuario LIKE :user ")
    Usuario buscarPorId(String user);

    @Insert
    void engadirUsuario(Usuario a);
    @Update
    void actualizarUsuario(Usuario a);
}
