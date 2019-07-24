package android.curso.minhaaplicacao.view.fragments;

import android.content.Context;
import android.curso.minhaaplicacao.controller.ControleChart;
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
import java.util.Map;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;


public class Graficos extends Fragment {
PieChartView pieChartView, pieChartView1;
    LineChartView lineChartView;
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
        pieChartView1 = view.findViewById(R.id.chart1);

        List<SliceValue> pieData1 = new ArrayList<>();

        Map<String,Double> clientes = controleChart.getBestClients();
        controler = 0;
        for (String cliente  : clientes.keySet()) {
            Float c =  clientes.get (cliente).floatValue();

            switch (controler){
                case 0:
                    pieData1.add(new SliceValue(c, Color.BLUE).setLabel(cliente));
                    break;
                case 1:
                    pieData1.add(new SliceValue(c, Color.GRAY).setLabel(cliente));
                    break;
                case 2:
                    pieData1.add(new SliceValue(c, Color.RED).setLabel(cliente));
                    break;
                case 3:
                    pieData1.add(new SliceValue(c, Color.MAGENTA).setLabel(cliente));
                    break;
                case 4:
                    pieData1.add(new SliceValue(c, Color.GREEN).setLabel(cliente));
                    break;

            }
            controler += 1;
        }


        PieChartData pieChartData1 = new PieChartData(pieData1);
        pieChartData1.setHasLabels(true).setValueLabelTextSize(8);
        pieChartData1.setHasCenterCircle(true).setCenterText1("Cliente / Vendas").setCenterText1FontSize(15).setCenterText1Color(Color.parseColor("#0097A7"));
        pieChartView1.setPieChartData(pieChartData1);


        lineChartView = view.findViewById(R.id.chart5);
        String[] axisData = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept",
                "Oct", "Nov", "Dec"};
        int[] yAxisData = {50, 20, 15, 30, 20, 60, 15, 40, 45, 10, 90, 18};

        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();


        Line line = new Line(yAxisValues).setColor(Color.parseColor("#9C27B0"));

        for (int i = 0; i < axisData.length; i++) {
            axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
        }

        for (int i = 0; i < yAxisData.length; i++) {
            yAxisValues.add(new PointValue(i, yAxisData[i]));
        }

        List lines = new ArrayList();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axis = new Axis();
        axis.setValues(axisValues);
        axis.setTextSize(16);
        axis.setTextColor(Color.parseColor("#03A9F4"));
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        yAxis.setName("Sales in millions");
        yAxis.setTextColor(Color.parseColor("#03A9F4"));
        yAxis.setTextSize(16);
        data.setAxisYLeft(yAxis);

        lineChartView.setLineChartData(data);
        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        viewport.top = 110;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);



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
