package android.curso.minhaaplicacao.model;

import java.util.Date;

public class ContasReceber {
    private int idContaReceber;
    private Pedidos pedido;
    private Date data;
    private Double valor;
    private Double valorLiquidado;


    public int getIdContaReceber() {
        return idContaReceber;
    }

    public void setIdContaReceber(int idContaReceber) {
        this.idContaReceber = idContaReceber;
    }

    public Pedidos getPedido() {
        return pedido;
    }

    public void setPedido(Pedidos pedido) {
        this.pedido = pedido;
    }





    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getValorLiquidado() {
        return valorLiquidado;
    }

    public void setValorLiquidado(Double valorLiquidado) {
        this.valorLiquidado = valorLiquidado;
    }
}
