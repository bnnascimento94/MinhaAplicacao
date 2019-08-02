package android.curso.minhaaplicacao.view.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.curso.minhaaplicacao.controller.ControleContasReceber;
import android.curso.minhaaplicacao.controller.ControleDiasPrazo;
import android.curso.minhaaplicacao.controller.ControlePedidos;
import android.curso.minhaaplicacao.view.CarrinhoActivity;
import android.curso.minhaaplicacao.view.TelaPrincipalActivity;
import android.curso.minhaaplicacao.view.adapters.ContasReceberAdapter;
import android.curso.minhaaplicacao.view.adapters.PedidosAdapter;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.curso.minhaaplicacao.R;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class ContasReceber extends Fragment {
 RecyclerView rv;
 EditText txtData, txtCliente, txtData1,txtData2;
 CheckBox chkMesAtual, chkQuitado, chkAberto;
 Button btnBuscar;
 Calendar myCalendar;
 Spinner mes;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contas_receber, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Contas a Receber");

        rv= view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        setHasOptionsMenu(true);
        rv.setAdapter(getAdapter());

       return view;
    }
    private void updateLabel(){
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat,new Locale("pt","BR"));
        txtData.setText(sdf.format(myCalendar.getTime()));

    }
    private void updateLabel1(){
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat,new Locale("pt","BR"));
        txtData1.setText(sdf.format(myCalendar.getTime()));

    }
    private void updateLabel2(){
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat,new Locale("pt","BR"));
        txtData2.setText(sdf.format(myCalendar.getTime()));

    }

    public ContasReceberAdapter getAdapter(){
        ControleContasReceber controleContasReceber = new ControleContasReceber(getContext());
        ContasReceberAdapter contasReceberAdapter = new ContasReceberAdapter (controleContasReceber.getAllContasReceber());
        return contasReceberAdapter;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.tela_principal, menu);

        MenuItem item1 = menu.findItem(R.id.action_search);

        MenuItemCompat.setShowAsAction(item1, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item1,null);

        // listening to search query text change
        item1.setVisible(true);
        item1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                dialogBox();
                return false;
            }
        });


    }




    public void dialogBox(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.pagamento_conta_receber_filtro);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        txtData =  dialog.findViewById(R.id.txtDataContaReceber);
        txtCliente =  dialog.findViewById(R.id.txtClienteContaReceber);
        chkAberto = dialog.findViewById(R.id.chkAbertoContaReceber);
        chkMesAtual = dialog.findViewById(R.id.chkMesAtualContaReceber);
        chkQuitado = dialog.findViewById(R.id.chkQuitadoContaReceber);
        btnBuscar = dialog.findViewById(R.id.btnBuscarContaReceber);
        mes = dialog.findViewById(R.id.spnMes);
        txtData1 = dialog.findViewById(R.id.txtData1ContaReceber);
        txtData2 = dialog.findViewById(R.id.txtData2ContaReceber);
        final boolean data = false,data1= false,data2= false;

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.meses,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mes.setAdapter(adapter);


        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    //
                updateLabel();


            }
        };

        txtData.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                new DatePickerDialog(getContext(),date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                return false;
            }
        });

        txtData1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                new DatePickerDialog(getContext(),date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                return false;
            }
        });
        txtData2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                new DatePickerDialog(getContext(),date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                return false;
            }
        });
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
                }
                else{
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
                if(txtData.getText().length()>0){
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
                }else if(txtData1.getText().length()>0&& txtData2.getText().length()>0){
                    if(chkQuitado.isChecked()){
                        ContasReceberAdapter contasReceberAdapter = new ContasReceberAdapter (controleContasReceber.getContasAReceberEntreDatasAndQuitado(txtData1.getText().toString(),txtData2.getText().toString()));
                        rv.setAdapter(contasReceberAdapter);
                    }else if (chkAberto.isChecked()){
                        ContasReceberAdapter contasReceberAdapter = new ContasReceberAdapter (controleContasReceber.getContasAReceberEntreDatasAndAReceber(txtData1.getText().toString(),txtData2.getText().toString()));
                        rv.setAdapter(contasReceberAdapter);
                    }else if (txtCliente.getText().length()>0){
                        ContasReceberAdapter contasReceberAdapter = new ContasReceberAdapter (controleContasReceber.getContasAReceberEntreDatasAndCliente(txtData1.getText().toString(),txtData2.getText().toString(),txtCliente.getText().toString()));
                        rv.setAdapter(contasReceberAdapter);
                    }else{
                        ContasReceberAdapter contasReceberAdapter = new ContasReceberAdapter (controleContasReceber.getContasAReceberEntreDatas(txtData1.getText().toString(),txtData2.getText().toString()));
                        rv.setAdapter(contasReceberAdapter);
                    }
                }else if(txtCliente.getText().length() == 0 &&
                        txtData.getText().length() == 0 &&
                        txtData1.getText().length() == 0 &&
                        txtData2.getText().length() ==0 &&
                        chkAberto.isChecked() ==false &&
                        chkQuitado.isChecked() == false &&
                        chkMesAtual.isChecked() == false){

                        rv.setAdapter(getAdapter());

                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }








}
