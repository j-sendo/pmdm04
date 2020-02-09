package com.example.pmdm04.persistencia;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database( entities = {Usuario.class,Pedido.class,Producto.class},version = 8,exportSchema = false)
public abstract class BaseDatos extends RoomDatabase {
    public abstract UsuarioDao usuarioDao();
    public abstract PedidoDao pedidoDao();
    public abstract ProductoDao productoDao();
}
