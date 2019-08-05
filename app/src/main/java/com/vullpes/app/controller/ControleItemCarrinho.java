package com.vullpes.app.controller;

import android.content.ContentValues;
import android.content.Context;
import com.vullpes.app.dataModel.ItemCarrinhoDataModel;
import com.vullpes.app.dataSource.DataSource;
import com.vullpes.app.model.ItemCarrinho;

import android.util.Log;
import java.util.List;

public class ControleItemCarrinho extends DataSource {
    ContentValues dados;
    public ControleItemCarrinho(Context context) {
        super(context);
    }

    public boolean salvar(ItemCarrinho obj){
        boolean sucesso;
        dados = new ContentValues();
        dados.put(ItemCarrinhoDataModel.getProduto(),obj.getProduto().getIdProduto());
        dados.put(ItemCarrinhoDataModel.getQuantidade(),obj.getQtde());
        dados.put(ItemCarrinhoDataModel.getValorVenda(),obj.getItemValorVenda());
        sucesso= insert(ItemCarrinhoDataModel.getTabela(),dados);
        return sucesso;
    }
    public boolean deletarItemCarinho(ItemCarrinho obj){
        boolean sucesso;
        try {
                sucesso = deletar(ItemCarrinhoDataModel.getTabela(), obj.getIdItemCarrinho());
        }catch(Exception e){
            Log.i("ERRO DELETAR PEDIDO ->"," "+e);
            sucesso = false;
        }
        return sucesso;
    }
    public boolean deletarAllItemCarinho(){
        boolean sucesso = false;
        try {
            sucesso = deletarTodos(ItemCarrinhoDataModel.getTabela());
        }catch(Exception e){
            Log.i("ERRO DELETAR PEDIDO ->"," "+e);
            sucesso = false;
        }
        return sucesso;
    }
    public boolean alterar(ItemCarrinho obj){
        boolean sucesso;
        dados = new ContentValues();
            dados.put(ItemCarrinhoDataModel.getIdItemCarrinho(),obj.getIdItemCarrinho());
            dados.put(ItemCarrinhoDataModel.getProduto(),obj.getProduto().getIdProduto());
            dados.put(ItemCarrinhoDataModel.getQuantidade(),obj.getQtde());
            dados.put(ItemCarrinhoDataModel.getValorVenda(),obj.getItemValorVenda());
            sucesso = alterar(ItemCarrinhoDataModel.getTabela(),dados);
        return sucesso;
    }
    public List<ItemCarrinho> getAllItens(){
        return getAllItensCarrinhos();

    }
    public boolean temRegistro(){
        boolean registro = false;
        try{

            if(getLastItemCarrinho().getIdItemCarrinho()>0){
                registro = true;
            }
        }
        catch(Exception e){
            registro = false;
            Log.i("Erro ->"," "+e);
        }
        return registro;
    }
    public List<ItemCarrinho> getItemCarrinhoByNome(int idProduto){
        return getAllItemCarrinhoByName(idProduto);
    }

    public Double setTotalCarrinho(List<ItemCarrinho> itemCarrinhos){
        double total =0.00;
        for(ItemCarrinho item : itemCarrinhos){
            total += item.getItemValorVenda();
        }
        return total;
    }













}
