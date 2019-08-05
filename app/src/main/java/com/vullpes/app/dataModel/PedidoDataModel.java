package com.vullpes.app.dataModel;

public class PedidoDataModel {
    private static String queryCriarTabela = "";

    private final static String  tabela = "tb_pedidos";
    private final static String idPedido = "id";
    private final static String idCliente = "id_cliente";
    private final static String idCondicaoPagamento = "id_condicao_pagamento";
    private final static String idNomePrazoPagamento = "id_nome_prazo_pagamento";
    private final static String valorTotal = "valor_total";
    private final static String data = "data";


    public static String criarTabela(){
        queryCriarTabela = "create table "+ tabela;
        queryCriarTabela +=" (";
        queryCriarTabela +=idPedido+" INTEGER PRIMARY KEY AUTOINCREMENT, ";
        queryCriarTabela +=idCliente+" INTEGER, ";
        queryCriarTabela +=idCondicaoPagamento+" INTEGER, ";
        queryCriarTabela +=idNomePrazoPagamento+" INTEGER, ";
        queryCriarTabela +=valorTotal+" REAL, ";
        queryCriarTabela +=data+" TEXT, ";
        queryCriarTabela +=" FOREIGN KEY("+idCliente+") REFERENCES "+ ClienteDataModel.getTabela()+"("+ClienteDataModel.getid()+"), ";
        queryCriarTabela +=" FOREIGN KEY("+idCondicaoPagamento+") REFERENCES "+ CondicoesPagamentoDataModel.getTabela()+"("+CondicoesPagamentoDataModel.getIdCondicao()+"), ";
        queryCriarTabela +=" FOREIGN KEY("+idNomePrazoPagamento+") REFERENCES "+ PrazosPagamentoDataModel.getTabela()+"("+PrazosPagamentoDataModel.getIdPrazo()+") ";
        queryCriarTabela +=" )";
        return  queryCriarTabela;
    }

    public static String getTabela() {
        return tabela;
    }
    public static String getid(){
        return idPedido;
    }
    public static String getIdCliente(){return idCliente;}
    public static String getValorTotal(){return valorTotal;}
    public static String getData(){return data;}
    public static String getQueryCriarTabela() {
        return queryCriarTabela;
    }
    public static void setQueryCriarTabela(String queryCriarTabela) {
        PedidoDataModel.queryCriarTabela = queryCriarTabela;
    }


    public static String getIdCondicaoPagamento() {
        return idCondicaoPagamento;
    }

    public static String getIdNomePrazoPagamento() {
        return idNomePrazoPagamento;
    }
}
