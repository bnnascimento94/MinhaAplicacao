package com.vullpes.app.controller;

import android.content.ContentValues;
import android.content.Context;
import com.vullpes.app.dataModel.CondicoesPagamentoDataModel;
import com.vullpes.app.dataSource.DataSource;
import com.vullpes.app.model.CondicoesPagamento;
import android.util.Log;

import java.util.List;

public class ControleCondicaoPagamento extends DataSource {
    ContentValues dados;

    public ControleCondicaoPagamento(Context context) {
        super(context);
    }
    public boolean salvar(CondicoesPagamento obj){
        boolean sucesso= false;
        try{
            if(!(this.getAllCondicaoPagamentoByName(obj.getNomeCondiçãoPagamento()).size()>0)){
                dados = new ContentValues();
                dados.put(CondicoesPagamentoDataModel.getNomeCondicao(),obj.getNomeCondiçãoPagamento());
                sucesso= insert(CondicoesPagamentoDataModel.getTabela(),dados);
            }
        }catch (Exception e){
            Log.i("ERRO SALVAR PEDIDO ->"," "+e);
            sucesso = false;
        }

        return sucesso;
    }
    public boolean deletar(CondicoesPagamento obj){
        boolean sucesso = false;
        try {
            if(!(getPedidoByIdCondicao(obj.getIdCondicaoPagamento()).size()>0)){
                sucesso = deletar(CondicoesPagamentoDataModel.getTabela(), obj.getIdCondicaoPagamento());
            }
        }catch(Exception e){
            Log.i("ERRO DELETAR PEDIDO ->"," "+e);
            sucesso = false;
        }
        return sucesso;
    }
    public boolean alterar(CondicoesPagamento obj){
        boolean sucesso;
        try{
            dados = new ContentValues();
            dados.put(CondicoesPagamentoDataModel.getIdCondicao(),obj.getIdCondicaoPagamento());
            dados.put(CondicoesPagamentoDataModel.getNomeCondicao(),obj.getNomeCondiçãoPagamento());
            sucesso = alterar(CondicoesPagamentoDataModel.getTabela(),dados);

        }catch (Exception e){
            Log.i("ERRO ALTERAR PEDIDO ->"," "+e);
            sucesso = false;
        }

        return sucesso;
    }

    public List<CondicoesPagamento> getAllCondicaoPagamento(){
        return getAllCondicoesPagamento();
    }
    public List<CondicoesPagamento> getAllCondicaoPagamentoByName(String nome){
        return getCondicoesPagamentoByName(nome);
    }
    public List<CondicoesPagamento> getAllCondicaoPagamentoById(int id){
        return getCondicoesPagamentoById(id);
    }









}
