package android.curso.minhaaplicacao.view.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.curso.minhaaplicacao.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;


public class Graficos extends Fragment {
PieChartView pieChartView, pieChartView1;
View view;
    public Graficos() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_graficos, container, false);

        pieChartView = view.findViewById(R.id.chart);

        List<SliceValue> pieData = new ArrayList<>();

        pieData.add(new SliceValue(15, Color.BLUE).setLabel("Produto A"));
        pieData.add(new SliceValue(25, Color.GRAY).setLabel("Produto b"));
        pieData.add(new SliceValue(10, Color.RED).setLabel("Produto c"));
        pieData.add(new SliceValue(60, Color.MAGENTA).setLabel("Produto d"));

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelTextSize(12);
        pieChartData.setHasCenterCircle(true).setCenterText1("Top  Produtos").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));
        pieChartView.setPieChartData(pieChartData);

        pieChartView1 = view.findViewById(R.id.chart1);

        List<SliceValue> pieData1 = new ArrayList<>();

        pieData1.add(new SliceValue(15, Color.BLUE).setLabel("Cliente  A"));
        pieData1.add(new SliceValue(25, Color.GRAY).setLabel("Cliente b"));
        pieData1.add(new SliceValue(10, Color.RED).setLabel("Cliente c"));
        pieData1.add(new SliceValue(60, Color.MAGENTA).setLabel("Cliente d"));

        PieChartData pieChartData1 = new PieChartData(pieData);
        pieChartData1.setHasLabels(true).setValueLabelTextSize(12);
        pieChartData1.setHasCenterCircle(true).setCenterText1("Top Clientes").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));
        pieChartView1.setPieChartData(pieChartData);

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
