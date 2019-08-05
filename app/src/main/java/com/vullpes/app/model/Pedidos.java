package com.vullpes.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Pedidos implements Serializable {
    private int idPedido;
    private Cliente cliente;
    private ArrayList<ItemPedido> itensPedido;
    private double valorTotal;
    private Date data;
    private CondicoesPagamento condicoesPagamento;
    private PrazosPagamento prazosPagamento;
    public Pedidos(){}


    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }



    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public ArrayList<ItemPedido> getItensPedido() {
        return itensPedido;
    }

    public void setItensPedido(ArrayList<ItemPedido> itensPedido) {
        this.itensPedido = itensPedido;
    }

    public CondicoesPagamento getCondicoesPagamento() {
        return condicoesPagamento;
    }

    public void setCondicoesPagamento(CondicoesPagamento condicoesPagamento) {
        this.condicoesPagamento = condicoesPagamento;
    }

    public PrazosPagamento getPrazosPagamento() {
        return prazosPagamento;
    }

    public void setPrazosPagamento(PrazosPagamento prazosPagamento) {
        this.prazosPagamento = prazosPagamento;
    }
}
