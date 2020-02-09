package com.example.pmdm04.persistencia;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
import java.util.Set;

@Dao
public interface PedidoDao {
    @Query("SELECT * FROM Pedido;")
    List<Pedido> getPedidos();

    @Insert
    void insertarPedido(Pedido pedido);

    @Query("UPDATE Pedido SET estado= :estado WHERE id==:id")
    void modificarEstado(int id,String estado);

    @Query("SELECT * FROM Pedido WHERE usuario==:usuario AND estado==:estado;")
    List<Pedido> getPedidosUsuario(String usuario,String estado);

    @Query("SELECT * FROM Pedido WHERE estado==:estado ORDER BY usuario,id;")
    List<Pedido> getPedidosEstado(String estado);

    @Query("SELECT Producto.id,Producto.nome,Producto.categoria FROM Producto INNER JOIN Pedido ON Pedido.idProd==Producto.id WHERE Pedido.id==:idped")
    Producto getProductoFromPedido(int idped);
}
