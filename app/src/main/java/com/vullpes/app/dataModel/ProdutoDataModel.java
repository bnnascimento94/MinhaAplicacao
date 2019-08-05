package com.vullpes.app.dataModel;

public class ProdutoDataModel {
    private final static String  tabela = "tb_produto";
    private final static String idProduto = "id";
    private final static String nomeProduto = "nome_produto";
    private final static String custoProduto = "custo_produto";
    private final static String valorUnitario = "valor_unitario";
    private final static String diretorioFoto = "foto_dir";
    private final static String nomeFoto = "nome_foto";



    private static String queryCriarTabela = "";


    // criar dinamicamente uma query sql para criar a tabela media escolar no banco de dados
    public static String criarTabela(){
        queryCriarTabela = "create table "+ tabela;
        queryCriarTabela +=" (";
        queryCriarTabela +=idProduto+" INTEGER PRIMARY KEY AUTOINCREMENT, ";
        queryCriarTabela +=nomeProduto+" TEXT, ";
        queryCriarTabela +=custoProduto+" REAL, ";
        queryCriarTabela +=valorUnitario+" REAL, ";
        queryCriarTabela +=diretorioFoto+" TEXT, ";
        queryCriarTabela +=nomeFoto+" TEXT ";
        queryCriarTabela +=" )";
        return  queryCriarTabela;
    }

    public static String getTabela() {
        return tabela;
    }

    public static String getid(){
        return idProduto;
    }
    public static String getNomeProduto(){return nomeProduto;}
    public static String getCustoProduto(){return custoProduto;}
    public static String getValorUnitario(){return valorUnitario;}



    public static String getQueryCriarTabela() {
        return queryCriarTabela;
    }

    public static void setQueryCriarTabela(String queryCriarTabela) {
        ProdutoDataModel.queryCriarTabela = queryCriarTabela;
    }


    public static String getDiretorioFoto() {
        return diretorioFoto;
    }

    public static String getNomeFoto() {
        return nomeFoto;
    }
}
