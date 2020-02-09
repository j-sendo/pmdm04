package com.example.pmdm04.adaptadores;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pmdm04.*;
import com.example.pmdm04.persistencia.*;



import java.util.ArrayList;

public class AdaptadorListaPedidos extends RecyclerView.Adapter<AdaptadorListaPedidos.ContenedorPedido> {
    ArrayList<Pedido> listaPedidos;
    BdInteraccion bd;

    public AdaptadorListaPedidos(ArrayList<Pedido> listaPedidos,BdInteraccion bd) {
        super();
        this.listaPedidos = listaPedidos;
        this.bd=bd;
    }

    public class ContenedorPedido extends RecyclerView.ViewHolder{
        View vista;
        Layout a;
        TextView vhdirec,vhdescr,vhcp,vhcida,vhcant;
        public ContenedorPedido(@NonNull View itemView) {
            super(itemView);
            vista=itemView;
            vhdirec=vista.findViewById(R.id.vhdirec);
            vhdescr=vista.findViewById(R.id.vhdescr);
            vhcp=vista.findViewById(R.id.vhcp);
            vhcida=vista.findViewById(R.id.vhcida);
            vhcant=vista.findViewById(R.id.vhcant);

        }
    }


    @NonNull
    @Override
    public ContenedorPedido onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View a=LayoutInflater.from(parent.getContext()).inflate(R.layout.contedor_item_pedido,parent,false);
        return new ContenedorPedido(a);
    }

    @Override
    public void onBindViewHolder(@NonNull ContenedorPedido holder, int position) {

        Producto producto=bd.getProductoFromPedido(listaPedidos.get(position).id);
        holder.vhdescr.setText("Producto:  "+ producto);
        holder.vhcant.setText("  Cantidade:  "+listaPedidos.get(position).cantidade);
        holder.vhdirec.setText("Dirección: "+listaPedidos.get(position).direccion);
        holder.vhcp.setText("C.Postal: "+listaPedidos.get(position).codPost);
        holder.vhcida.setText(" Cidade: "+listaPedidos.get(position).cidade);

    }

    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }
}
