package android.curso.minhaaplicacao.view.adapters;

import android.content.DialogInterface;
import android.curso.minhaaplicacao.R;
import android.curso.minhaaplicacao.classes.ImageSaver;
import android.curso.minhaaplicacao.controller.ControleItemCarrinho;
import android.curso.minhaaplicacao.controller.ControleProdutos;
import android.curso.minhaaplicacao.model.ItemCarrinho;
import android.curso.minhaaplicacao.model.ItemPedido;
import android.curso.minhaaplicacao.view.TelaPrincipalActivity;
import android.curso.minhaaplicacao.view.fragments.Graficos;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProdutosPedidoCarrinhoAdapter extends RecyclerView.Adapter<ProdutosPedidoCarrinhoAdapter.ProdutoViewHolder>{
    List<ItemCarrinho> produtos;
    byte[] fotoArray;
    Bitmap raw;
    private AlertDialog alerta;
    public String valorTotal = "";

    public ProdutosPedidoCarrinhoAdapter(List<ItemCarrinho> produtos){
        this.produtos = produtos;

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

        produtoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Atenção");
                builder.setMessage("Deseja Realmente Excluir este Produto?");
                builder.setPositiveButton("Positivo", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        if(produtos.size() == 1){
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setTitle("Atenção");
                            builder.setMessage("Este é o produto único ao deletar estará excluindo o pedido, deseja confirmar?");
                            builder.setPositiveButton("Positivo", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    ControleItemCarrinho controller = new ControleItemCarrinho(v.getContext());
                                    if(controller.deletarItemCarinho(produtos.get(i))){
                                        produtos.remove(produtos.get(i));
                                        notifyItemRemoved(i); //seta o elemento que foi excluido
                                        notifyItemRangeChanged(i, produtos.size());

                                        valorTotal =controller.setTotalCarrinho(produtos);

                                        ControleItemCarrinho controleItemCarrinho = new ControleItemCarrinho(v.getContext());
                                        controleItemCarrinho.deletarAllItemCarinho();

                                        Toast.makeText(v.getContext(),"Deletado com Êxito",Toast.LENGTH_LONG).show();
                                        AppCompatActivity activity = (AppCompatActivity) v.getContext();

                                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, new Graficos()).addToBackStack(null).commit();
                                    }
                                    else{
                                        Toast.makeText(v.getContext(),"Não foi possível deletar",Toast.LENGTH_LONG).show();
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
                            ControleItemCarrinho controller = new ControleItemCarrinho(v.getContext());
                            if(controller.deletarItemCarinho(produtos.get(i))){
                                produtos.remove(produtos.get(i));
                                notifyItemRemoved(i); //seta o elemento que foi excluido
                                notifyItemRangeChanged(i, produtos.size());
                                valorTotal =controller.setTotalCarrinho(produtos);
                                Toast.makeText(v.getContext(),"Deletado com Êxito",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(v.getContext(),"Não foi possível deletar",Toast.LENGTH_LONG).show();
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
            }
        });

        }




    @Override
    public int getItemCount() {
        return produtos.size();
    }
}
