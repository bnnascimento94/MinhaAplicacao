package android.curso.minhaaplicacao.view.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.curso.minhaaplicacao.R;



public class PrazoDiasPagamento extends Fragment {




    public PrazoDiasPagamento() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PrazoDiasPagamento newInstance(String param1, String param2) {
        PrazoDiasPagamento fragment = new PrazoDiasPagamento();
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
        return inflater.inflate(R.layout.fragment_prazo_dias_pagamento, container, false);
    }






}
