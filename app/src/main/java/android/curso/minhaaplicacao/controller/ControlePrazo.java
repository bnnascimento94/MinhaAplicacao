package android.curso.minhaaplicacao.controller;

import android.content.ContentValues;
import android.content.Context;
import android.curso.minhaaplicacao.dataModel.PrazosPagamentoDataModel;
import android.curso.minhaaplicacao.dataSource.DataSource;
import android.curso.minhaaplicacao.model.PrazosPagamento;
import android.util.Log;

import java.util.List;

public class ControlePrazo extends DataSource {
    ContentValues dados;
    public ControlePrazo(Context context) {
        super(context);
    }

    public boolean salvar(PrazosPagamento obj){
        boolean sucesso= false;
        try{
            if(!(this.getPrazosPagamentoByName(obj.getNomePrazoPagamento()).size()>0)){
                dados = new ContentValues();
                dados.put(PrazosPagamentoDataModel.getNomePrazo(),obj.getNomePrazoPagamento());
                sucesso= insert(PrazosPagamentoDataModel.getTabela(),dados);
            }
        }catch (Exception e){
            Log.i("ERRO SALVAR PEDIDO ->"," "+e);
            sucesso = false;
        }

        return sucesso;
    }
    public boolean deletar(PrazosPagamento obj){
        boolean sucesso;
        try {
            sucesso = deletar(PrazosPagamentoDataModel.getTabela(), obj.getIdPrazoPagamento());
        }catch(Exception e){
            Log.i("ERRO DELETAR PEDIDO ->"," "+e);
            sucesso = false;
        }
        return sucesso;
    }
    public boolean alterar(PrazosPagamento obj){
        boolean sucesso;
        try{
            dados = new ContentValues();
            dados.put(PrazosPagamentoDataModel.getIdPrazo(),obj.getIdPrazoPagamento());
            dados.put(PrazosPagamentoDataModel.getNomePrazo(),obj.getNomePrazoPagamento());
            sucesso = alterar(PrazosPagamentoDataModel.getTabela(),dados);

        }catch (Exception e){
            Log.i("ERRO ALTERAR PEDIDO ->"," "+e);
            sucesso = false;
        }

        return sucesso;
    }


    public List<PrazosPagamento> getAllPrazosPagamento(){
        return getAllPrazosPagamento();
    }
    public List<PrazosPagamento> getPrazosPagamentoByName(String nome){
        return getPrazosPagamentoByName(nome);
    }
    public List<PrazosPagamento> getPrazosPagamentoById(int id){
        return getPrazosPagamentoById(id);
    }









}
