package com.vullpes.app.dataModel;

public class PrazosPagamentoDataModel {
    private final static String  tabela = "tb_prazos_pagamento";
    private final static String idPrazo = "id";
    private final static String nomePrazo = "nome_prazo";

    private static String queryCriarTabela = "";


    public static String criarTabela(){
        setQueryCriarTabela("create table "+ getTabela());
        setQueryCriarTabela(getQueryCriarTabela() + " (");
        setQueryCriarTabela(getQueryCriarTabela() + getIdPrazo() +" INTEGER PRIMARY KEY AUTOINCREMENT, ");
        setQueryCriarTabela(getQueryCriarTabela() + getNomePrazo() +" TEXT ");
        setQueryCriarTabela(getQueryCriarTabela() + " )");
        return getQueryCriarTabela();
    }


    public static String getTabela() {
        return tabela;
    }



    public static String getQueryCriarTabela() {
        return queryCriarTabela;
    }

    public static void setQueryCriarTabela(String queryCriarTabela) {
        PrazosPagamentoDataModel.queryCriarTabela = queryCriarTabela;
    }


    public static String getIdPrazo() {
        return idPrazo;
    }

    public static String getNomePrazo() {
        return nomePrazo;
    }
}
