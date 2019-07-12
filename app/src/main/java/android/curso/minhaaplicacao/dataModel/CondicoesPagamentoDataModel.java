package android.curso.minhaaplicacao.dataModel;

public class CondicoesPagamentoDataModel {

    private final static String  tabela = "tb_condicao_pagamento";
    private final static String idCondicao = "id";
    private final static String nomeCondicao = "nome_condicao";

    private static String queryCriarTabela = "";


    public static String criarTabela(){
        setQueryCriarTabela("create table "+ getTabela());
        setQueryCriarTabela(getQueryCriarTabela() + " (");
        setQueryCriarTabela(getQueryCriarTabela() + getIdCondicao() +" INTEGER PRIMARY KEY AUTOINCREMENT, ");
        setQueryCriarTabela(getQueryCriarTabela() + getNomeCondicao() +" TEXT ");
        setQueryCriarTabela(getQueryCriarTabela() + " )");
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
