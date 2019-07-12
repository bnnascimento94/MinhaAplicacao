package android.curso.minhaaplicacao.dataModel;

import android.curso.minhaaplicacao.model.ItemPedido;

public class ItemPedidoDataModel {
    private final static String  tabela = "tb_item_produto";
    private final static String idItemProduto = "id";
    private final static String quantidade = "qtde";
    private final static String valorVenda = "item_valor_venda";
    private final static String produto = "id_produto";
    private final static String idPedido = "id_pedido";


    public static String criarTabela(){
        queryCriarTabela = "create table "+ tabela;
        queryCriarTabela +=" (";
        queryCriarTabela +=idItemProduto+" INTEGER PRIMARY KEY AUTOINCREMENT, ";
        queryCriarTabela +=quantidade+" INTEGER, ";
        queryCriarTabela +=valorVenda+" REAL, ";
        queryCriarTabela +=produto+" INTEGER NOT NULL, ";
        queryCriarTabela +=idPedido+" INTEGER NOT NULL, ";
        queryCriarTabela +=" FOREIGN KEY("+produto+") REFERENCES "+ProdutoDataModel.getTabela()+"("+ProdutoDataModel.getid()+"), ";
        queryCriarTabela +=" FOREIGN KEY("+idPedido+") REFERENCES "+PedidoDataModel.getTabela()+"("+PedidoDataModel.getid()+") ";
        queryCriarTabela +=" )";
        return  queryCriarTabela;
    }

    public static String getTabela() {
        return tabela;
    }
    public static String getid(){
        return idItemProduto;
    }
    public static String getQuantidade(){return quantidade;}
    public static String getValorVenda(){return valorVenda;}
    public static String getIdProduto(){return produto;}
    public static String getIdPedido(){return idPedido;}
    public static String getQueryCriarTabela() {
        return queryCriarTabela;
    }
    public static void setQueryCriarTabela(String queryCriarTabela) {
        ItemPedidoDataModel.queryCriarTabela = queryCriarTabela;
    }

    private static String queryCriarTabela = "";
}
