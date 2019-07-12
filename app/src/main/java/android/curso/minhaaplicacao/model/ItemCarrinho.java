package android.curso.minhaaplicacao.model;

import java.io.Serializable;

public class ItemCarrinho implements Serializable {
    private int idItemCarrinho;
    private int qtde;
    private double itemValorVenda;
    private Produto produto;

    public int getIdItemCarrinho() {
        return idItemCarrinho;
    }

    public void setIdItemCarrinho(int idItemCarrinho) {
        this.idItemCarrinho = idItemCarrinho;
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
}
