package com.vullpes.app.dataModel;

public class ClienteDataModel {
    private final static String  tabela = "tb_cliente";
    private final static String idCliente = "id";
    private final static String nomeCliente = "nome_completo";
    private final static String emailCliente = "email";
    private final static String telefoneCliente = "telefone";
    private final static String enderecoCliente = "endereco";

    private final static String diretorioFoto = "foto_dir";
    private final static String nomeFoto = "nome_foto";


    private static String queryCriarTabela = "";


    // criar dinamicamente uma query sql para criar a tabela media escolar no banco de dados
    public static String criarTabela(){
        queryCriarTabela = "create table "+ tabela;
        queryCriarTabela +=" (";
        queryCriarTabela +=idCliente+" INTEGER PRIMARY KEY AUTOINCREMENT, ";
        queryCriarTabela +=nomeCliente+" TEXT, ";
        queryCriarTabela +=emailCliente+" TEXT, ";
        queryCriarTabela +=telefoneCliente+" TEXT, ";
        queryCriarTabela +=enderecoCliente+" TEXT, ";
        queryCriarTabela +=diretorioFoto+" TEXT, ";
        queryCriarTabela +=nomeFoto+" TEXT ";
        queryCriarTabela +=" )";
        return  queryCriarTabela;
    }

    public static String getTabela() {
        return tabela;
    }

    public static String getid(){
        return idCliente;
    }
    public static String getNomeCliente(){return nomeCliente;}
    public static String getEmailCliente(){return emailCliente;}
    public static String getTelefoneCliente(){return telefoneCliente;}
    public static String getEnderecoCliente(){return enderecoCliente;}


    public static String getQueryCriarTabela() {
        return queryCriarTabela;
    }

    public static void setQueryCriarTabela(String queryCriarTabela) {
        ClienteDataModel.queryCriarTabela = queryCriarTabela;
    }


    public static String getDiretorioFoto() {
        return diretorioFoto;
    }

    public static String getNomeFoto() {
        return nomeFoto;
    }
}
