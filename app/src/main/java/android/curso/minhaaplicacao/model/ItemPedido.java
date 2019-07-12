package android.curso.minhaaplicacao.model;

import java.io.Serializable;

public class ItemPedido implements Serializable {
    private int idItemPedido;
    private int qtde;
    private double itemValorVenda;
    private Produto produto;
    private Pedidos pedido;

    public ItemPedido(){}


    public int getIdItemPedido() {
        return idItemPedido;
    }

    public void setIdItemPedido(int idItemPedido) {
        this.idItemPedido = idItemPedido;
    }

    public int getQtde() {
        return qtde;
    }

    public void setQtde(int qtde) {
        this.qtde = qtde;
    }

    public double getItemValorVenda() {
        return itemValorVenda;
    }

    public void setItemValorVenda(double itemValorVenda) {
        this.itemValorVenda = itemValorVenda;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Pedidos getPedido() {
        return pedido;
    }

    public void setPedido(Pedidos pedido) {
        this.pedido = pedido;
    }
}
