package com.vullpes.app.model;

import java.io.Serializable;

public class PrazoDiasPagamento implements Serializable {

    private int idPrazoDias;
    private int idPrazo;
    private int numeroDias;
    private double porcentagem;

    public int getIdPrazoDias() {
        return idPrazoDias;
    }

    public void setIdPrazoDias(int idPrazoDias) {
        this.idPrazoDias = idPrazoDias;
    }

    public int getIdPrazo() {
        return idPrazo;
    }

    public void setIdPrazo(int idPrazo) {
        this.idPrazo = idPrazo;
    }

    public int getNumeroDias() {
        return numeroDias;
    }

    public void setNumeroDias(int numeroDias) {
        this.numeroDias = numeroDias;
    }

    public double getPorcentagem() {
        return porcentagem;
    }

    public void setPorcentagem(double porcentagem) {
        this.porcentagem = porcentagem;
    }
}
