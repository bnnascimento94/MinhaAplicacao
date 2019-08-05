package com.vullpes.app.controller;

import android.content.ContentValues;
import android.content.Context;

import com.vullpes.app.dataModel.ContaAReceberDataModel;
import com.vullpes.app.dataSource.DataSource;

import com.vullpes.app.model.ContasReceber;
import com.vullpes.app.model.PrazosPagamento;

import java.text.SimpleDateFormat;

public class ControleContasReceber extends DataSource {
    ContentValues dados;
    PrazosPagamento prazosPagamento;
    public ControleContasReceber(Context context) {
        super(context);
        this.prazosPagamento = prazosPagamento;
    }


    public boolean salvar(ContasReceber obj){
        boolean sucesso = false;

            dados = new ContentValues();
            dados.put(ContaAReceberDataModel.getIdPedido(),obj.getPedido().getIdPedido());
            SimpleDateFormat dataFormatada = new SimpleDateFormat("yyyy-MM-dd");
            dados.put(ContaAReceberDataModel.getDataContaReceber(),dataFormatada.format(obj.getData()));
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
            SimpleDateFormat dataFormatada = new SimpleDateFormat("yyyy-MM-dd");
            dados.put(ContaAReceberDataModel.getDataContaReceber(),dataFormatada.format(obj.getData()));
            dados.put(ContaAReceberDataModel.getValor(),obj.getValor());
            dados.put(ContaAReceberDataModel.getValorLiquidado(),obj.getValorLiquidado());
            sucesso = alterar(ContaAReceberDataModel.getTabela(),dados);
            return true;
    }




}
