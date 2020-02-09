package com.example.pmdm04.persistencia;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.room.Room;

import java.util.List;


public class BdInteraccion {
    @SuppressLint("StaticFieldLeak")
    private static BdInteraccion bdInteraccion; //
    private UsuarioDao usuarioDao;
    private PedidoDao pedidoDao;
    private ProductoDao productoDao;

    public BdInteraccion(Context contexto) {
        Context appContext=contexto.getApplicationContext();
        BaseDatos bd= Room.databaseBuilder(appContext,BaseDatos.class,"bdPmdm03.db").createFromAsset("bdInicial.db").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        usuarioDao=bd.usuarioDao();
        pedidoDao=bd.pedidoDao();
        productoDao=bd.productoDao();

    }

    public static BdInteraccion get(Context contexto){
        if (bdInteraccion==null){
            bdInteraccion=new BdInteraccion(contexto);
        }
        return bdInteraccion;
    }

    public Usuario getUsuario(String user){
        return usuarioDao.buscarPorId(user);
    }

    public void insertarUsuario(Usuario a){
        usuarioDao.engadirUsuario(a);
    }

    public List<Pedido> getPedidos() {return pedidoDao.getPedidos();};

    public void insertarPedido(Pedido pedido) {pedidoDao.insertarPedido(pedido);}

    public List<Pedido> getPedidosUsuario(String user,String estado){return pedidoDao.getPedidosUsuario(user,estado);}

    public List<Pedido> getPedidosEstado(String estado) {return pedidoDao.getPedidosEstado(estado);}

    public void modificarEstado(int id, String estado) {pedidoDao.modificarEstado(id,estado);}

    public List<Producto> getProductosPorCat(String categoria){return productoDao.getProductosPorCat(categoria);}

    public List<String> getCategorias(){return productoDao.getCategorias();}

    public void insertarProducto(Producto product){ productoDao.insertarProducto(product);}

    public Producto getProductoFromPedido(int idped){ return pedidoDao.getProductoFromPedido(idped);}

    public void actualizarUsuario(Usuario a){usuarioDao.actualizarUsuario(a);}
}
