package com.vullpes.app.dataModel;

public class ItemCarrinhoDataModel {
    private static String queryCriarTabela = "";
    private final static String  tabela = "tb_item_carrinho";
    private final static String idItemCarrinho = "id";
    private final static String quantidade = "qtde";
    private final static String valorVenda = "item_valor_venda";
    private final static String produto = "id_produto";

    public static String criarTabela(){
        queryCriarTabela = "create table "+ tabela;
        queryCriarTabela +=" (";
        queryCriarTabela +=idItemCarrinho+" INTEGER PRIMARY KEY AUTOINCREMENT, ";
        queryCriarTabela +=quantidade+" INTEGER, ";
        queryCriarTabela +=valorVenda+" REAL, ";
        queryCriarTabela +=produto+" INTEGER NOT NULL, ";
        queryCriarTabela +=" FOREIGN KEY("+produto+") REFERENCES "+ProdutoDataModel.getTabela()+"("+ProdutoDataModel.getid()+") ";
        queryCriarTabela +=" )";
        return  queryCriarTabela;
    }


    public static String getQueryCriarTabela() {
        return queryCriarTabela;
    }

    public static void setQueryCriarTabela(String queryCriarTabela) {
        ItemCarrinhoDataModel.queryCriarTabela = queryCriarTabela;
    }

    public static String getTabela() {
        return tabela;
    }

    public static String getIdItemCarrinho() {
        return idItemCarrinho;
    }

    public static String getQuantidade() {
        return quantidade;
    }

    public static String getValorVenda() {
        return valorVenda;
    }

    public static String getProduto() {
        return produto;
    }
}
