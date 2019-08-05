package com.vullpes.app.view.adapters;

import android.curso.minhaaplicacao.R;
import com.vullpes.app.classes.ImageSaver;
import com.vullpes.app.model.ItemPedido;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProdutosPedidoSelecionadoAdapter extends RecyclerView.Adapter<ProdutosPedidoSelecionadoAdapter.ProdutoViewHolder>{
    List<ItemPedido> produtos;
    byte[] fotoArray;
    Bitmap raw;

    public ProdutosPedidoSelecionadoAdapter(List<ItemPedido> produtos){
        this.produtos = produtos;
    }



    public static class ProdutoViewHolder extends RecyclerView.ViewHolder {
        CircleImageView fotoID;
        TextView nomeProduto;
        TextView valorUnitario;
        TextView valorTotal;
        TextView txtQtde;
        ProdutoViewHolder(final View itemView) {
            super(itemView);
            fotoID = itemView.findViewById(R.id.imageProdutoSelecionado);
            nomeProduto = itemView.findViewById(R.id.txtNomeProdutoSelecionado);
            valorUnitario = itemView.findViewById(R.id.txtValorUnitarioProdutoSelecionado);
            valorTotal = itemView.findViewById(R.id.txtValorVendaProdutoSelecionado);
            txtQtde = itemView.findViewById(R.id.txtQtde);
        }

    }

    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pedido_produtos_selecionados, viewGroup, false);
        ProdutoViewHolder pvh = new ProdutoViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProdutoViewHolder produtoViewHolder,final int i) {
        NumberFormat z = NumberFormat.getCurrencyInstance();
        produtoViewHolder.nomeProduto.setText(produtos.get(i).getProduto().getNomeProduto());
        produtoViewHolder.valorUnitario.setText(z.format(produtos.get(i).getProduto().getValorUnitario()));
        produtoViewHolder.valorTotal.setText(z.format(produtos.get(i).getItemValorVenda()));
        produtoViewHolder.txtQtde.setText(String.valueOf(produtos.get(i).getQtde()));

        Bitmap bitmap = new ImageSaver(produtoViewHolder.itemView.getContext()).
                setFileName(produtos.get(i).getProduto().getNomeArquivo()).
                setDirectoryName(produtos.get(i).getProduto().getDiretorioFoto()).
                load();
        if(bitmap!=null){
            produtoViewHolder.fotoID.setImageBitmap(bitmap);
        }else{
            produtoViewHolder.fotoID.setImageResource(R.drawable.produto);
        }
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }
}
