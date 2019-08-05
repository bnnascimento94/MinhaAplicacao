package com.vullpes.app.dataModel;

public class CondicoesPagamentoDataModel {

    private final static String  tabela = "tb_condicao_pagamento";
    private final static String idCondicao = "id";
    private final static String nomeCondicao = "nome_condicao";

    private static String queryCriarTabela = "";


    public static String criarTabela(){
        setQueryCriarTabela("create table "+ getTabela());
        setQueryCriarTabela( queryCriarTabela + " (");
        setQueryCriarTabela( queryCriarTabela + idCondicao +" INTEGER PRIMARY KEY AUTOINCREMENT, ");
        setQueryCriarTabela( queryCriarTabela + nomeCondicao +" TEXT ");
        setQueryCriarTabela( queryCriarTabela + " )");
        return getQueryCriarTabela();
    }


    public static String getTabela() {
        return tabela;
    }

    public static String getIdCondicao() {
        return idCondicao;
    }

    public static String getNomeCondicao() {
        return nomeCondicao;
    }

    public static String getQueryCriarTabela() {
        return queryCriarTabela;
    }

    public static void setQueryCriarTabela(String queryCriarTabela) {
        CondicoesPagamentoDataModel.queryCriarTabela = queryCriarTabela;
    }
}
