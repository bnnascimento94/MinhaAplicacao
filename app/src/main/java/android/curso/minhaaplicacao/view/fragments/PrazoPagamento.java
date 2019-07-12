package android.curso.minhaaplicacao.view.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.curso.minhaaplicacao.R;
import android.widget.Button;
import android.widget.EditText;


public class PrazoPagamento extends Fragment {
    EditText prazoPagamento;
    Button confirmarPrazoPagamento;
    View view;
    public PrazoPagamento() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PrazoPagamento newInstance(String param1, String param2) {
        PrazoPagamento fragment = new PrazoPagamento();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
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
        view = inflater.inflate(R.layout.fragment_prazo_pagamento, container, false);

        prazoPagamento = view.findViewById(R.id.txtNomePrazo);
        confirmarPrazoPagamento = view.findViewById(R.id.btnConfirmarPrazoPagamento);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event







}
