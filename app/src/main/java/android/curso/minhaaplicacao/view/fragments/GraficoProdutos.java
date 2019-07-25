package android.curso.minhaaplicacao.view.fragments;

import android.curso.minhaaplicacao.R;
import android.curso.minhaaplicacao.controller.ControleChart;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class GraficoProdutos extends Fragment {
    View view;
    PieChartView pieChartView;
    public GraficoProdutos(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.guia_graficos_produtos, container, false);

        pieChartView = view.findViewById(R.id.chart5);

        List<SliceValue> pieData = new ArrayList<>();
        ControleChart controleChart = new ControleChart(getContext());
        Map<String,Double> produtos = controleChart.getBestSellerProducts();
        int controler = 0;
        for (String produto  : produtos.keySet()) {
            Float c =  produtos.get (produto).floatValue();

            switch (controler){
                case 0:
                    pieData.add(new SliceValue(c, Color.BLUE).setLabel(produto));
                    break;
                case 1:
                    pieData.add(new SliceValue(c, Color.GRAY).setLabel(produto));
                    break;
                case 2:
                    pieData.add(new SliceValue(c, Color.RED).setLabel(produto));
                    break;
                case 3:
                    pieData.add(new SliceValue(c, Color.MAGENTA).setLabel(produto));
                    break;
                case 4:
                    pieData.add(new SliceValue(c, Color.GREEN).setLabel(produto));
                    break;

            }
            controler += 1;
        }


        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelTextSize(8);
        pieChartData.setHasCenterCircle(true).setCenterText1("Produtos mais vendidos").setCenterText1FontSize(15).setCenterText1Color(Color.parseColor("#0097A7"));
        pieChartView.setPieChartData(pieChartData);



        return view;
    }

}
