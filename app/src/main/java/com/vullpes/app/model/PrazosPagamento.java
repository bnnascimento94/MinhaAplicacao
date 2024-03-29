package com.vullpes.app.model;

import java.io.Serializable;
import java.util.List;

public class PrazosPagamento implements Serializable {

    private int idPrazoPagamento;
    private String nomePrazoPagamento;
    private List<PrazoDiasPagamento> prazosDiasPagamento;

    public int getIdPrazoPagamento() {
        return idPrazoPagamento;
    }

    public void setIdPrazoPagamento(int idPrazoPagamento) {
        this.idPrazoPagamento = idPrazoPagamento;
    }

    public String getNomePrazoPagamento() {
        return nomePrazoPagamento;
    }

    public void setNomePrazoPagamento(String nomePrazoPagamento) {
        this.nomePrazoPagamento = nomePrazoPagamento;
    }

    public List<PrazoDiasPagamento> getPrazosDiasPagamento() {
        return prazosDiasPagamento;
    }

    public void setPrazosDiasPagamento(List<PrazoDiasPagamento> prazosDiasPagamento) {
        this.prazosDiasPagamento = prazosDiasPagamento;
    }
}
