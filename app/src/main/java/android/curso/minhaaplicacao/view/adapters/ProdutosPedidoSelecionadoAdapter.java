package android.curso.minhaaplicacao.view.adapters;

import android.curso.minhaaplicacao.R;
import android.curso.minhaaplicacao.classes.ImageSaver;
import android.curso.minhaaplicacao.model.ItemCarrinho;
import android.curso.minhaaplicacao.model.ItemPedido;
import android.curso.minhaaplicacao.model.Pedidos;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import android.widget.TextView;

import java.util.ArrayList;
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
        produtoViewHolder.nomeProduto.setText(produtos.get(i).getProduto().getNomeProduto());
        produtoViewHolder.valorUnitario.setText("R$ "+String.valueOf(produtos.get(i).getProduto().getValorUnitario()));
        produtoViewHolder.valorTotal.setText("R$ "+String.valueOf(produtos.get(i).getItemValorVenda()));
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
