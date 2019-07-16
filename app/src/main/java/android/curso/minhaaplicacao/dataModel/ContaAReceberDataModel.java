package android.curso.minhaaplicacao.dataModel;

public class ContaAReceberDataModel {
    private final static String  tabela = "tb_conta_receber";
    private final static String idContaReceber = "id";
    private final static String idPedido = "id_pedido";
    private final static String dataContaReceber = "data_conta";
    private final static String valor = "valor";
    private final static String valorLiquidado = "valor_liquidado";
    private static String queryCriarTabela = "";

    public static String criarTabela(){
        queryCriarTabela = "create table "+ tabela;
        queryCriarTabela +=" (";
        queryCriarTabela +=idContaReceber+" INTEGER PRIMARY KEY AUTOINCREMENT, ";
        queryCriarTabela +=idPedido+" INTEGER, ";
        queryCriarTabela +=dataContaReceber+" TEXT, ";
        queryCriarTabela +=valor+" INTEGER, ";
        queryCriarTabela +=valorLiquidado+" TEXT, ";
        queryCriarTabela +=" FOREIGN KEY("+idPedido+") REFERENCES "+ PedidoDataModel.getTabela()+"(" +PedidoDataModel.getid()+ ") ";
        queryCriarTabela +=" )";
        return  queryCriarTabela;
    }


    public static String getTabela() {
        return tabela;
    }

    public static String getIdContaReceber() {
        return idContaReceber;
    }

    public static String getIdPedido() {
        return idPedido;
    }

    public static String getDataContaReceber() {
        return dataContaReceber;
    }

    public static String getValor() {
        return valor;
    }

    public static String getValorLiquidado() {
        return valorLiquidado;
    }

    public static String getQueryCriarTabela() {
        return queryCriarTabela;
    }

    public static void setQueryCriarTabela(String queryCriarTabela) {
        ContaAReceberDataModel.queryCriarTabela = queryCriarTabela;
    }


}
