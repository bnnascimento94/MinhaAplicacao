package com.vullpes.app.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.curso.minhaaplicacao.R;
import com.vullpes.app.controller.ControlePedidos;
import com.vullpes.app.model.Pedidos;
import com.vullpes.app.view.fragments.ProdutosPedidoSelecionado;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.PedidoViewHolder> implements Filterable {
    List<Pedidos> pedidos;
    List<Pedidos> pedidosFiltrados;
    private AlertDialog alerta;
    private int posicao; //variavel global que seta a posição do elemento para excluir
    Context context;
    private ActionMode mActionMode;
    public PedidosAdapter(List<Pedidos> pedidos, Context context){
        this.pedidos = pedidos;
        this.pedidosFiltrados = pedidos;
        this.context = context;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    pedidosFiltrados = pedidos;
                } else {
                    List<Pedidos> filteredList = new ArrayList<>();
                    for (Pedidos row : pedidos) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCliente().getNomeCliente().toLowerCase().contains(charString.toLowerCase()) ||String.valueOf(row.getCliente().getNomeCliente()).contains(constraint)) {
                            filteredList.add(row);
                        }
                    }

                    pedidosFiltrados = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = pedidosFiltrados;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                pedidosFiltrados = (ArrayList<Pedidos>) results.values;
                notifyDataSetChanged();

            }
        };

    }

    public static class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView nomeCliente;
        TextView valorTotal;
        TextView data;
        View itemview;

        PedidoViewHolder(final View itemView) {
            super(itemView);
            this.itemview = itemView;
            nomeCliente = itemView.findViewById(R.id.txtCondicaoPagamento);
            valorTotal = itemView.findViewById(R.id.txtValorPedido);
            data = itemView.findViewById(R.id.txtDataPedido);
        }
    }
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pedido_listagem, viewGroup, false);
        PedidoViewHolder pvh = new PedidoViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final PedidoViewHolder produtoViewHolder,final int i) {
        NumberFormat z = NumberFormat.getCurrencyInstance();
        produtoViewHolder.nomeCliente.setText(pedidosFiltrados.get(i).getCliente().getNomeCliente());
        produtoViewHolder.valorTotal.setText(z.format(pedidosFiltrados.get(i).getValorTotal()));
        SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");
        produtoViewHolder.data.setText(dataFormatada.format(pedidosFiltrados.get(i).getData()));

        produtoViewHolder.itemview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mActionMode != null){

                }
                //clienteViewHolder.view.setOnClickListener(null);
                Activity activity = (Activity) context;
                posicao = i; //setando numa variavel global o item da listview
                mActionMode =  activity.startActionMode(mActionModeCallBack);
            }
        });
    }

    private ActionMode.Callback mActionModeCallBack = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_pedido_listagem, menu);
            mode.setTitle("Selecione a Ação");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.menu_delete:
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Atenção");
                    builder.setMessage("Deseja Realmente Excluir este Registro?");
                    builder.setPositiveButton("Positivo", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                            ControlePedidos cp = new ControlePedidos(context);
                            if(cp.deletar(pedidosFiltrados.get(posicao))){
                                pedidosFiltrados.remove(pedidosFiltrados.get(posicao));
                                notifyItemRemoved(posicao); //seta o elemento que foi excluido
                                notifyItemRangeChanged(posicao, pedidosFiltrados.size()); //muda em tela a quantidade de elementos exibidos
                                pedidos = pedidosFiltrados; // seta os dados para não aparecer os elementos já excluidos
                                Toast.makeText(context,"Deletado com Êxito",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    builder.setNegativeButton("Negativo", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });
                    alerta = builder.create();
                    alerta.show();
                    break;

                case R.id.produtos_pedido:
                    ProdutosPedidoSelecionado pps = new ProdutosPedidoSelecionado();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("itensPedido",pedidosFiltrados.get(posicao).getItensPedido());
                    pps.setArguments(bundle);

                    AppCompatActivity activity = (AppCompatActivity) context;
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, pps).addToBackStack(null).commit();

                    break;
            }


            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }

    };

    @Override
    public int getItemCount() {
        return pedidosFiltrados.size();
    }

}

