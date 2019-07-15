package android.curso.minhaaplicacao.controller;

import android.content.ContentValues;
import android.content.Context;

import android.curso.minhaaplicacao.dataModel.ItemPedidoDataModel;
import android.curso.minhaaplicacao.dataModel.PedidoDataModel;
import android.curso.minhaaplicacao.dataSource.DataSource;

import android.curso.minhaaplicacao.model.ItemCarrinho;
import android.curso.minhaaplicacao.model.ItemPedido;
import android.curso.minhaaplicacao.model.Pedidos;
import android.util.Log;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ControlePedidos extends DataSource {
    ContentValues dados,dados1;
    Context context;
    public ControlePedidos(Context context){
        super(context);
    }

    public boolean salvar(Pedidos obj){
        boolean sucesso = true;
        dados = new ContentValues();
        dados.put(PedidoDataModel.getIdCliente(),obj.getCliente().getIdCliente());
        dados.put(PedidoDataModel.getIdCondicaoPagamento(),obj.getCondicoesPagamento().getIdCondicaoPagamento());
        dados.put(PedidoDataModel.getIdNomePrazoPagamento(),obj.getPrazosPagamento().getIdPrazoPagamento());
        dados.put(PedidoDataModel.getValorTotal(),obj.getValorTotal());
        SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");
        dados.put(PedidoDataModel.getData(),dataFormatada.format(obj.getData()));

        sucesso= insert(PedidoDataModel.getTabela(),dados);

        if(salvarItem(obj.getItensPedido())){
            sucesso=true;
        }else{
            sucesso=false;
        }


        return sucesso;
    }
    public boolean deletar(Pedidos obj){
        boolean sucesso = false;
        try {
            if (deletarItemPedido(obj.getIdPedido())) {
                sucesso = deletar(PedidoDataModel.getTabela(), obj.getIdPedido());
            }
        }catch(Exception e){
            Log.i("ERRO DELETAR PEDIDO ->"," "+e);
            sucesso = false;
        }
        return sucesso;
    }
    public boolean alterar(Pedidos obj){
        boolean sucesso = false;
        dados = new ContentValues();

        if(deletarItemPedido(obj.getIdPedido())){
            dados.put(PedidoDataModel.getid(),obj.getIdPedido());
            dados.put(PedidoDataModel.getIdCliente(),obj.getCliente().getIdCliente());
            dados.put(PedidoDataModel.getValorTotal(),obj.getValorTotal());
            dados.put(PedidoDataModel.getData(),obj.getData().toString());
            dados.put(PedidoDataModel.getIdCondicaoPagamento(),obj.getCondicoesPagamento().getIdCondicaoPagamento());
            dados.put(PedidoDataModel.getIdNomePrazoPagamento(),obj.getPrazosPagamento().getIdPrazoPagamento());
            sucesso = alterar(PedidoDataModel.getTabela(),dados);

            List<ItemPedido> itemPedidos = setarIdPedido(obj.getItensPedido()) ;
            if(salvarItem(itemPedidos)){
                sucesso = true;

            }else{
                sucesso = false;
            }

        }


        return sucesso;
    }

    public List<Pedidos> listarPedidos() {return getAllPedidos();}
    public List<ItemPedido> setarItemPedido(List<ItemCarrinho> itemCarrinhos){

        List<ItemPedido> itemPedido =new ArrayList<ItemPedido>();


        for(ItemCarrinho item : itemCarrinhos){
            ItemPedido iped = new ItemPedido();
            iped.setQtde(item.getQtde());
            iped.setProduto(item.getProduto());
            iped.setItemValorVenda(item.getItemValorVenda());
            itemPedido.add(iped);
        }
        return itemPedido;
    }

    private boolean deletarItemPedido(int idPedido){
        boolean sucesso = false;
        for(ItemPedido item:getAllItemPedidoByIdPedido(idPedido)){
            sucesso = deletar(ItemPedidoDataModel.getTabela(),item.getIdItemPedido());
        }

        return sucesso;
    }
    private boolean salvarItem(List<ItemPedido> itens){
        boolean sucesso = false;
        List<ItemPedido> itemPedidos = setarIdPedido(itens) ;

        for(ItemPedido item:itemPedidos){
            dados = new ContentValues();
            dados.put(ItemPedidoDataModel.getQuantidade(),item.getQtde());
            dados.put(ItemPedidoDataModel.getValorVenda(),item.getItemValorVenda());
            dados.put(ItemPedidoDataModel.getIdProduto(),item.getProduto().getIdProduto());
            dados.put(ItemPedidoDataModel.getIdPedido(),item.getPedido().getIdPedido());
            sucesso= insert(ItemPedidoDataModel.getTabela(),dados);
        }

        return  sucesso;
    }
    private List<ItemPedido> setarIdPedido(List<ItemPedido> itens){
        Pedidos ped = getLastPedido();
        for( ItemPedido item :itens){
            item.setPedido(ped);
        }
        return itens;
    }






}
