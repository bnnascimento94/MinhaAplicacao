package android.curso.minhaaplicacao.view.fragments;

import android.content.Context;
import android.curso.minhaaplicacao.controller.ControleContasReceber;
import android.curso.minhaaplicacao.controller.ControlePedidos;
import android.curso.minhaaplicacao.view.adapters.ContasReceberAdapter;
import android.curso.minhaaplicacao.view.adapters.PedidosAdapter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.curso.minhaaplicacao.R;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;


public class ContasReceber extends Fragment {
 RecyclerView rv;
 EditText txtData, txtCliente;
 CheckBox chkMesAtual, chkQuitado, chkAberto;
 Button btnBuscar;
    public ContasReceber() {
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
        View view = inflater.inflate(R.layout.fragment_contas_receber, container, false);
        txtData = view.findViewById(R.id.txtData);
        txtCliente = view.findViewById(R.id.txtCliente);
        chkAberto = view.findViewById(R.id.chkAberto);
        chkMesAtual = view.findViewById(R.id.chkMesAtual);
        chkAberto = view.findViewById(R.id.chkAberto);
        chkQuitado = view.findViewById(R.id.chkQuitado);
        btnBuscar = view.findViewById(R.id.btnBuscar);

        rv= view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        setHasOptionsMenu(true);
        rv.setAdapter(getAdapter());


        txtCliente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ControleContasReceber controleContasReceber = new ControleContasReceber(getContext());
                if(txtData.getText().length() >0){
                    ContasReceberAdapter contasReceberAdapter = new ContasReceberAdapter (controleContasReceber.getAllContasReceberByClienteAndDate(s.toString(),txtData.getText().toString()));
                    rv.setAdapter(contasReceberAdapter);
                }else if(chkAberto.isChecked()){
                    ContasReceberAdapter contasReceberAdapter = new ContasReceberAdapter (controleContasReceber.getAllContasReceberByClienteAndAberta(s.toString()));
                    rv.setAdapter(contasReceberAdapter);

                }else if (chkMesAtual.isChecked()){
                    ContasReceberAdapter contasReceberAdapter = new ContasReceberAdapter (controleContasReceber.getAllContasReceberByClienteAndCurrentMonth(s.toString()));
                    rv.setAdapter(contasReceberAdapter);

                }else if(chkQuitado.isChecked()){
                    ContasReceberAdapter contasReceberAdapter = new ContasReceberAdapter (controleContasReceber.getAllContasReceberByClienteAndQuitada(s.toString()));
                    rv.setAdapter(contasReceberAdapter);
                }else{
                    ContasReceberAdapter contasReceberAdapter = new ContasReceberAdapter (controleContasReceber.getAllContasReceberByCliente(s.toString()));
                    rv.setAdapter(contasReceberAdapter);

                }


            }
        });

        chkAberto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ControleContasReceber controleContasReceber = new ControleContasReceber(getContext());
                if(isChecked){
                    if(chkMesAtual.isChecked()){
                        ContasReceberAdapter contasReceberAdapter = new ContasReceberAdapter (controleContasReceber.getAllContasReceberAbertasCurrentMonth());
                        rv.setAdapter(contasReceberAdapter);
                    }
                    else if(txtCliente.getText().length()>0){
                        ContasReceberAdapter contasReceberAdapter = new ContasReceberAdapter (controleContasReceber.getAllContasReceberByClienteAndAberta(txtCliente.getText().toString()));
                        rv.setAdapter(contasReceberAdapter);

                    }
                    else {
                        ContasReceberAdapter contasReceberAdapter = new ContasReceberAdapter (controleContasReceber.getAllContasReceberAberta());
                        rv.setAdapter(contasReceberAdapter);
                    }
                }
                else{

                    rv.setAdapter(getAdapter());
                }
            }
        });

        chkQuitado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ControleContasReceber controleContasReceber = new ControleContasReceber(getContext());
                if(isChecked){
                    if(chkMesAtual.isChecked()){
                        ContasReceberAdapter contasReceberAdapter = new ContasReceberAdapter (controleContasReceber.getAllContasReceberQuitadasCurrentMonth());
                        rv.setAdapter(contasReceberAdapter);
                    }else if(chkAberto.isChecked()){
                        ContasReceberAdapter contasReceberAdapter = new ContasReceberAdapter (controleContasReceber.getAllContasReceber());
                        rv.setAdapter(contasReceberAdapter);
                    }else{
                        ContasReceberAdapter contasReceberAdapter = new ContasReceberAdapter (controleContasReceber.getAllContasReceberQuitadas());
                        rv.setAdapter(contasReceberAdapter);
                    }

                }else{
                    rv.setAdapter(getAdapter());

                }

            }
        });

        chkMesAtual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ControleContasReceber controleContasReceber = new ControleContasReceber(getContext());
                if(isChecked){
                    if(chkQuitado.isChecked()){
                        ContasReceberAdapter contasReceberAdapter = new ContasReceberAdapter (controleContasReceber.getAllContasReceberQuitadasCurrentMonth());
                        rv.setAdapter(contasReceberAdapter);

                    }else if (chkAberto.isChecked()){
                        ContasReceberAdapter contasReceberAdapter = new ContasReceberAdapter (controleContasReceber.getAllContasReceberAbertasCurrentMonth());
                        rv.setAdapter(contasReceberAdapter);
                    }else if (txtCliente.getText().length()>0){
                        ContasReceberAdapter contasReceberAdapter = new ContasReceberAdapter (controleContasReceber.getAllContasReceberByClienteAndCurrentMonth(txtCliente.getText().toString()));
                        rv.setAdapter(contasReceberAdapter);

                    }else{
                        ContasReceberAdapter contasReceberAdapter = new ContasReceberAdapter (controleContasReceber.getAllContasReceberCurrentMonth());
                        rv.setAdapter(contasReceberAdapter);
                    }
                }
                else{
                    rv.setAdapter(getAdapter());
                }
            }
        });



        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ControleContasReceber controleContasReceber = new ControleContasReceber(getContext());
                if(chkQuitado.isChecked()){
                    ContasReceberAdapter contasReceberAdapter = new ContasReceberAdapter (controleContasReceber.getContasAReceberByDataAndQuitadas(txtData.getText().toString()));
                    rv.setAdapter(contasReceberAdapter);
                }else if (chkAberto.isChecked()){
                    ContasReceberAdapter contasReceberAdapter = new ContasReceberAdapter (controleContasReceber.getContasAReceberByDataAndAbertas(txtData.getText().toString()));
                    rv.setAdapter(contasReceberAdapter);
                }else if (txtCliente.getText().length()>0){
                    ContasReceberAdapter contasReceberAdapter = new ContasReceberAdapter (controleContasReceber.getAllContasReceberByClienteAndDate(txtCliente.getText().toString(),txtData.getText().toString()));
                    rv.setAdapter(contasReceberAdapter);
                }else{
                    ContasReceberAdapter contasReceberAdapter = new ContasReceberAdapter (controleContasReceber.getContasAReceberByData(txtData.getText().toString()));
                    rv.setAdapter(contasReceberAdapter);
                }
            }
        });


       return view;
    }

    public ContasReceberAdapter getAdapter(){
        ControleContasReceber controleContasReceber = new ControleContasReceber(getContext());
        ContasReceberAdapter contasReceberAdapter = new ContasReceberAdapter (controleContasReceber.getAllContasReceber());
        return contasReceberAdapter;
    }


}