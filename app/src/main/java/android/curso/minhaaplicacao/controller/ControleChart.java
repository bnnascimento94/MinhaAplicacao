package android.curso.minhaaplicacao.controller;

import android.content.Context;
import android.curso.minhaaplicacao.dataSource.DataSource;

import java.util.Map;

public class ControleChart extends DataSource {
    public ControleChart(Context context) {
        super(context);
    }

    public Map<String,Double> getBestSellerProducts(){
        return topSalesProducts();
    }
    public Map<String, Double> getBestClients(){
        return topClientesPedidos();

    }
}
