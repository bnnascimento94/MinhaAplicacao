package android.curso.minhaaplicacao.dataModel;

public class PrazoDiasPagamentoDataModel {
    private final static String  tabela = "tb_prazo_dias_pagamento";
    private final static String idPrazoDias = "id";
    private final static String idPrazo = "id_prazo";
    private final static String numeroDias = "numero_dias";
    private final static String porcentagem = "porcentagem";

    private static String queryCriarTabela = "";

    public static String criarTabela(){
        queryCriarTabela = "create table "+ tabela;
        queryCriarTabela +=" (";
        queryCriarTabela +=idPrazoDias+" INTEGER PRIMARY KEY AUTOINCREMENT, ";
        queryCriarTabela +=idPrazo+" INTEGER, ";
        queryCriarTabela +=numeroDias+" INTEGER, ";
        queryCriarTabela += porcentagem +" REAL, ";
        queryCriarTabela +=" FOREIGN KEY("+idPrazo+") REFERENCES "+ PrazosPagamentoDataModel.getTabela() +"( "+ PrazosPagamentoDataModel.getIdPrazo()+ " ) ";
        queryCriarTabela +=" )";
        return  queryCriarTabela;
    }

    public static String getTabela() {
        return tabela;
    }

    public static String getQueryCriarTabela() {
        return queryCriarTabela;
    }

    public static void setQueryCriarTabela(String queryCriarTabela) {
        PrazoDiasPagamentoDataModel.queryCriarTabela = queryCriarTabela;
    }

    public static String getIdPrazo() {
        return idPrazo;
    }

    public static String getIdPrazoDias() {
        return idPrazoDias;
    }

    public static String getNumeroDias() {
        return numeroDias;
    }

    public static String getPorcentagem() {
        return porcentagem;
    }
}
