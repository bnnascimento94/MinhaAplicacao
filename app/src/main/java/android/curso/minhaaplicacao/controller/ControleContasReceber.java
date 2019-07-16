package android.curso.minhaaplicacao.controller;

import android.content.ContentValues;
import android.content.Context;

import android.curso.minhaaplicacao.dataModel.ContaAReceberDataModel;
import android.curso.minhaaplicacao.dataSource.DataSource;

import android.curso.minhaaplicacao.model.ContasReceber;
import android.provider.ContactsContract;

import java.util.List;

public class ControleContasReceber extends DataSource {
    ContentValues dados;
    public ControleContasReceber(Context context) {
        super(context);
    }


    public boolean salvar(ContasReceber obj){
        boolean sucesso = false;
            dados = new ContentValues();
            dados.put(ContaAReceberDataModel.getIdPedido(),obj.getPedido().getIdPedido());
            dados.put(ContaAReceberDataModel.getDataContaReceber(),obj.getData().toString());
            dados.put(ContaAReceberDataModel.getValor(),obj.getValor());
            dados.put(ContaAReceberDataModel.getValorLiquidado(),obj.getValorLiquidado());
            sucesso= insert(ContaAReceberDataModel.getTabela(),dados);
            return sucesso;
    }
    public boolean deletar(ContasReceber obj){
        boolean sucesso = true;
        sucesso = deletar(ContaAReceberDataModel.getTabela(),obj.getIdContaReceber());
        return sucesso;
    }


    public boolean alterar(ContasReceber obj){
            boolean sucesso = true;
            dados = new ContentValues();
            dados.put(ContaAReceberDataModel.getIdContaReceber(),obj.getIdContaReceber());
            dados.put(ContaAReceberDataModel.getIdPedido(),obj.getPedido().getIdPedido());
            dados.put(ContaAReceberDataModel.getDataContaReceber(),obj.getData().toString());
            dados.put(ContaAReceberDataModel.getValor(),obj.getValor());
            dados.put(ContaAReceberDataModel.getValorLiquidado(),obj.getValorLiquidado());
            sucesso = alterar(ContaAReceberDataModel.getTabela(),dados);
            return true;
    }



}
