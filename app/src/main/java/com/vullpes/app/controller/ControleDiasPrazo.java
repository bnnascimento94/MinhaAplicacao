package com.vullpes.app.controller;

import android.content.ContentValues;
import android.content.Context;
import com.vullpes.app.dataModel.PrazoDiasPagamentoDataModel;
import com.vullpes.app.dataSource.DataSource;
import com.vullpes.app.model.PrazoDiasPagamento;

import java.util.List;

public class ControleDiasPrazo extends DataSource {
    ContentValues dados;
    Context context;

    public ControleDiasPrazo(Context context) {
        super(context);
        this.context = context;
    }

    public boolean salvar(PrazoDiasPagamento obj){
        boolean sucesso = false;
        double somaPorcentagem = getTotalPorcentagemPrazo(obj.getIdPrazo());
        if((somaPorcentagem+obj.getPorcentagem())<101){ //consulta se há algum cliente já cadastrado
            dados = new ContentValues();
            dados.put(PrazoDiasPagamentoDataModel.getIdPrazo(),obj.getIdPrazo());
            dados.put(PrazoDiasPagamentoDataModel.getNumeroDias(),obj.getNumeroDias());
            dados.put(PrazoDiasPagamentoDataModel.getPorcentagem(),obj.getPorcentagem());
            sucesso= insert(PrazoDiasPagamentoDataModel.getTabela(),dados);
        }
        return sucesso;
    }

    public boolean deletar(PrazoDiasPagamento obj){
        boolean sucesso = true;
        sucesso = deletar(PrazoDiasPagamentoDataModel.getTabela(),obj.getIdPrazoDias());
        return sucesso;
    }

    public boolean alterar(PrazoDiasPagamento obj){
        boolean sucesso = false;
        double somaPorcentagem = getTotalPorcentagemPrazo(obj.getIdPrazo());
        if((somaPorcentagem+ obj.getPorcentagem())<100){
            dados = new ContentValues();
            dados.put(PrazoDiasPagamentoDataModel.getIdPrazoDias(),obj.getIdPrazoDias());
            dados.put(PrazoDiasPagamentoDataModel.getIdPrazo(),obj.getIdPrazo());
            dados.put(PrazoDiasPagamentoDataModel.getNumeroDias(),obj.getNumeroDias());
            dados.put(PrazoDiasPagamentoDataModel.getPorcentagem(),obj.getPorcentagem());
            sucesso = alterar(PrazoDiasPagamentoDataModel.getTabela(),dados);
        }

        return sucesso;
    }

    public double getTotalPorcentagemPrazo(int idPrazo) {
        return totalPorcentagemDiasPrazo(idPrazo);

    }

    public List<PrazoDiasPagamento> getPrazoDiasPagamento(int idPrazo){
        return getPrazoDiaPagamentoById(idPrazo);
    }




}
