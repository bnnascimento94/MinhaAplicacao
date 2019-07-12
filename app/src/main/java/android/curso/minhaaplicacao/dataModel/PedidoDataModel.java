package android.curso.minhaaplicacao.dataModel;

import android.curso.minhaaplicacao.model.Cliente;

public class PedidoDataModel {
    private static String queryCriarTabela = "";

    private final static String  tabela = "tb_pedidos";
    private final static String idPedido = "id";
    private final static String idCliente = "id_cliente";
    private final static String valorTotal = "valor_total";
    private final static String data = "data";


    public static String criarTabela(){
        queryCriarTabela = "create table "+ tabela;
        queryCriarTabela +=" (";
        queryCriarTabela +=idPedido+" INTEGER PRIMARY KEY AUTOINCREMENT, ";
        queryCriarTabela +=idCliente+" INTEGER, ";
        queryCriarTabela +=valorTotal+" REAL, ";
        queryCriarTabela +=data+" TEXT, ";
        queryCriarTabela +=" FOREIGN KEY("+idCliente+") REFERENCES "+ ClienteDataModel.getTabela()+"("+ClienteDataModel.getid()+") ";
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




}
