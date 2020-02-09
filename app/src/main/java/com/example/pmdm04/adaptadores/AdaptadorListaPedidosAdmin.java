package com.example.pmdm04.adaptadores;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pmdm04.R;
import com.example.pmdm04.persistencia.BdInteraccion;
import com.example.pmdm04.persistencia.Pedido;
import com.example.pmdm04.persistencia.Producto;

import java.util.ArrayList;

public class AdaptadorListaPedidosAdmin extends RecyclerView.Adapter<AdaptadorListaPedidosAdmin.ContenedorPedido> {
    ArrayList<Pedido> listaPedidos;

    BdInteraccion bdI;

    public AdaptadorListaPedidosAdmin(ArrayList<Pedido> listaPedidos, BdInteraccion bdI) {
        super();
        this.listaPedidos = listaPedidos;
        this.bdI=bdI;
    }

    public class ContenedorPedido extends RecyclerView.ViewHolder{
        View vista;
        TextView vhdirec,vhdescr,vhcp,vhcida,vhuser,vhcant;
        Button bAceptar,bRexeitar;

        public ContenedorPedido(@NonNull View itemView) {
            super(itemView);
            vista=itemView;
            vhdirec=vista.findViewById(R.id.vhdirec);
            vhdescr=vista.findViewById(R.id.vhdescr);
            vhcp=vista.findViewById(R.id.vhcp);
            vhcida=vista.findViewById(R.id.vhcida);
            vhuser=vista.findViewById(R.id.vhuser);
            bAceptar=vista.findViewById(R.id.botonAceptarPedAdmin);
            bRexeitar=vista.findViewById(R.id.botonRexeitarPedAdmin);
            vhcant=vista.findViewById(R.id.vhcant);

        }
    }


    @NonNull
    @Override
    public ContenedorPedido onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View novoItem=LayoutInflater.from(parent.getContext()).inflate(R.layout.contedor_item_pedido_admin,parent,false);
        return new ContenedorPedido(novoItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ContenedorPedido holder, final int position) {
        final Pedido tmp=listaPedidos.get(position);
        Producto producto=bdI.getProductoFromPedido(tmp.id);
        holder.vhdescr.setText("Producto:  "+ producto);
        holder.vhcant.setText("  Cantidade:  "+listaPedidos.get(position).cantidade);
        holder.vhdirec.setText("Dirección: "+tmp.direccion);
        holder.vhcp.setText("C.Postal: "+tmp.codPost);
        holder.vhcida.setText("   Cidade: "+tmp.cidade);
        holder.vhuser.setText("Usuario: "+tmp.usuario);

        if (tmp.estado.equals("creado")) {
            holder.bAceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bdI.modificarEstado(tmp.id, "aceptado");
                    listaPedidos.remove(position);
                    AdaptadorListaPedidosAdmin.this.notifyItemRemoved(position); //
                }
            });
            holder.bRexeitar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bdI.modificarEstado(tmp.id, "rexeitado");
                    listaPedidos.remove(position);
                    AdaptadorListaPedidosAdmin.this.notifyItemRemoved(position); //
                }
            });
        }
        else {
            holder.bAceptar.setEnabled(false);
            holder.bAceptar.setVisibility(View.GONE);
            holder.bRexeitar.setEnabled(false);
            holder.bRexeitar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }
}
