package com.vullpes.app.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.curso.minhaaplicacao.R;
import com.vullpes.app.classes.ImageSaver;
import com.vullpes.app.controller.ControleItemCarrinho;
import com.vullpes.app.model.ItemCarrinho;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProdutosPedidoCarrinhoAdapter extends RecyclerView.Adapter<ProdutosPedidoCarrinhoAdapter.ProdutoViewHolder>{
    List<ItemCarrinho> produtos;
    private AlertDialog alerta;
    Boolean eCarrinho;
    private int posicao; //variavel global que seta a posição do elemento para excluir
    Context context;
    private ActionMode mActionMode;

    public ProdutosPedidoCarrinhoAdapter(List<ItemCarrinho> produtos, Boolean eCarrinho,Context context){
        this.produtos = produtos;
        this.eCarrinho = eCarrinho;
        this.context = context;

    }

    public static class ProdutoViewHolder extends RecyclerView.ViewHolder {
        CircleImageView fotoID;
        TextView nomeProduto;
        TextView valorUnitario;
        TextView valorTotal,txtQtde;

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

        produtoViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        produtoViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(mActionMode != null){
                    return false;
                }
                Activity activity = (Activity) context;
                posicao = i; //setando numa variavel global o item da listview
                mActionMode =  activity.startActionMode(mActionModeCallBack);
                return true;
            }
        });

        }

    private ActionMode.Callback mActionModeCallBack = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.delete_menu, menu);
            mode.setTitle("Deletar Item");
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
                    builder.setMessage("Deseja Realmente Excluir este Produto?");
                    builder.setPositiveButton("Positivo", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            if(produtos.size() == 1){
                                if(!eCarrinho){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("Atenção");
                                    builder.setMessage("Este é o único item ao deletar estará excluindo o pedido, deseja confirmar?");
                                    builder.setPositiveButton("Positivo", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            ControleItemCarrinho controller = new ControleItemCarrinho(context);
                                            if(controller.deletarItemCarinho(produtos.get(posicao))){
                                                produtos.remove(produtos.get(posicao));
                                                notifyItemRemoved(posicao); //seta o elemento que foi excluido
                                                notifyItemRangeChanged(posicao, produtos.size());
                                                ControleItemCarrinho controleItemCarrinho = new ControleItemCarrinho(context);
                                                controleItemCarrinho.deletarAllItemCarinho();
                                                Toast.makeText(context,"Deletado com Êxito",Toast.LENGTH_LONG).show();
                                            }
                                            else{
                                                Toast.makeText(context,"Não foi possível deletar",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                    builder.setNegativeButton("Negativo", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface arg0, int arg1) {

                                        }
                                    });
                                    alerta = builder.create();
                                    alerta.show();
                                }else{
                                    ControleItemCarrinho controller = new ControleItemCarrinho(context);
                                    if(controller.deletarItemCarinho(produtos.get(posicao))){
                                        produtos.remove(produtos.get(posicao));
                                        notifyItemRemoved(posicao); //seta o elemento que foi excluido
                                        notifyItemRangeChanged(posicao, produtos.size());
                                        Toast.makeText(context,"Deletado com Êxito",Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(context,"Não foi possível deletar",Toast.LENGTH_LONG).show();
                                    }
                                }

                            }else{
                                ControleItemCarrinho controller = new ControleItemCarrinho(context);
                                if(controller.deletarItemCarinho(produtos.get(posicao))){
                                    produtos.remove(produtos.get(posicao));
                                    notifyItemRemoved(posicao); //seta o elemento que foi excluido
                                    notifyItemRangeChanged(posicao, produtos.size());
                                    Toast.makeText(context,"Deletado com Êxito",Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(context,"Não foi possível deletar",Toast.LENGTH_LONG).show();
                                }
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
        return produtos.size();
    }
}
