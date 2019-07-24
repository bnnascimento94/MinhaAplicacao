package android.curso.minhaaplicacao.view.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.curso.minhaaplicacao.classes.ImageSaver;
import android.curso.minhaaplicacao.classes.MoneyTextWatcher;
import android.curso.minhaaplicacao.controller.ControleProdutos;

import android.curso.minhaaplicacao.model.Produto;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.curso.minhaaplicacao.R;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;



public class CadastroProduto extends Fragment {
    private static final int SOLICITAR_PERMISSAO = 1;
    View view;
    Context context;
    ArrayList<Produto> produto;
    CircleImageView imagemProduto;
    FloatingActionButton camera;
    byte[] fotoArray;
    Bitmap raw;
    private AlertDialog alerta;
    EditText nomeProduto,custoProduto,valorVenda;
    Button btnSalvar;
    /** RESULT_CAMERA */
    private static final int RESULT_CAMERA = 111;

    /** RESULT_GALERIA */
    private static final int RESULT_GALERIA = 222;
    public CadastroProduto() {
        // Required empty public constructor
        context = getContext();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            produto =(ArrayList<Produto>) bundle.getSerializable("produto");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_cadastro_produto, container, false);
        nomeProduto = view.findViewById(R.id.editNomeProduto);
        custoProduto = view.findViewById(R.id.editCustoProduto);
        valorVenda = view.findViewById(R.id.editTextValorVenda);
        imagemProduto = view.findViewById(R.id.imageView3);
        btnSalvar = view.findViewById(R.id.btnSalvar);
        camera = view.findViewById(R.id.editFoto);
        imagemProduto.setImageResource(R.drawable.produto);

        Locale mLocale = new Locale("pt", "BR");
        valorVenda.addTextChangedListener(new MoneyTextWatcher(valorVenda, mLocale));
        custoProduto.addTextChangedListener(new MoneyTextWatcher(custoProduto, mLocale));

        if(produto!=null){
            nomeProduto.setText(produto.get(0).getNomeProduto());
            custoProduto.setText(String.valueOf(produto.get(0).getCustoProduto()));
            valorVenda.setText(String.valueOf(produto.get(0).getValorUnitario()));

            Bitmap bitmap = new ImageSaver(getContext()).
                    setFileName(produto.get(0).getNomeArquivo()).
                    setDirectoryName(produto.get(0).getDiretorioFoto()).
                    load();

            if(bitmap!=null){
                imagemProduto.setImageBitmap(bitmap);
            }else{
                imagemProduto.setImageResource(R.drawable.produto);
            }

        }

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verifica  o estado da permissão de WRITE_EXTERNAL_STORAGE
                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);


                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    // Se for diferente de PERMISSION_GRANTED, então vamos exibir a tela padrão
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, SOLICITAR_PERMISSAO);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Selecione a Ação");
                    builder.setMessage("Deseja capturar a Imagem da galeria ou Tirar a Foto?");
                    builder.setPositiveButton("Tirar Foto", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE );
                            startActivityForResult(intent, RESULT_CAMERA);

                        }
                    });
                    builder.setNegativeButton("Galeria", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, RESULT_GALERIA);
                        }
                    });
                    alerta = builder.create();
                    alerta.show();

                }

            }
        });


        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean dadosValidados = true;

                if(!(nomeProduto.getText().length()>0)){
                    nomeProduto.setError("*");
                    nomeProduto.requestFocus();
                    dadosValidados = false;
                }
                else if(!(custoProduto.getText().length()>0)){
                    custoProduto.setError("*");
                    custoProduto.requestFocus();
                    dadosValidados = false;
                }
                else if(!(valorVenda.getText().length()>0)){
                    valorVenda.setError("*");
                    valorVenda.requestFocus();
                    dadosValidados = false;
                }

                if(dadosValidados){

                    if(produto!=null){
                        try{
                            Locale locale = new Locale("pt", "BR");
                            String replaceable = String.format("[%s.\\s]", NumberFormat.getCurrencyInstance(locale).getCurrency().getSymbol());
                            String cleancusto = custoProduto.getText().toString().replaceAll(replaceable, "").replaceAll(",",".");
                            String cleanPreco =  valorVenda.getText().toString().replaceAll(replaceable, "").replaceAll(",",".");

                            Produto prod = new Produto();
                            prod.setIdProduto(produto.get(0).getIdProduto());
                            prod.setNomeProduto(nomeProduto.getText().toString());

                            prod.setCustoProduto(Double.parseDouble(cleancusto));
                            //String venda = valorVenda.getText().toString().replaceAll("[,]",".").replaceAll("[R$]","");
                            prod.setValorUnitario(Double.parseDouble(cleanPreco));
                            prod.setDiretorioFoto("produtos");
                            prod.setNomeArquivo(nomeProduto.getText().toString()+".png");

                            Bitmap bitmap = ((BitmapDrawable)imagemProduto.getDrawable()).getBitmap();
                            if(new ImageSaver(getContext(),"produtos",nomeProduto.getText().toString()+".png").deleteFile()){
                                new ImageSaver(getContext()).
                                        setFileName(nomeProduto.getText().toString()+".png").
                                        setDirectoryName("produtos").
                                        save(bitmap);
                                ControleProdutos cprod = new ControleProdutos(getContext());
                                if(cprod.alterar(prod)){
                                    Toast.makeText(getActivity().getApplicationContext(),"Alterado com Êxito",Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getActivity().getApplicationContext(),"Não foi possível alterar com Êxito",Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        catch(Exception e){
                            Log.e("Erro editar -> ",""+e);
                        }

                    }
                    else{
                        Locale locale = new Locale("pt", "BR");
                        String replaceable = String.format("[%s.\\s]", NumberFormat.getCurrencyInstance(locale).getCurrency().getSymbol());
                        String cleancusto = custoProduto.getText().toString().replaceAll(replaceable, "").replaceAll(",",".");
                        String cleanPreco =  valorVenda.getText().toString().replaceAll(replaceable, "").replaceAll(",",".");

                        Produto prod = new Produto();
                        prod.setNomeProduto(nomeProduto.getText().toString());
                        //String custo = custoProduto.getText().toString().replaceAll("[,]",".").replaceAll("[R$]","");
                        prod.setCustoProduto(Double.parseDouble(cleancusto));
                        //String venda = valorVenda.getText().toString().replaceAll("[,]",".").replaceAll("[R$]","");
                        prod.setValorUnitario(Double.parseDouble(cleanPreco));
                        prod.setDiretorioFoto("produtos");
                        prod.setNomeArquivo(nomeProduto.getText().toString()+".png");

                        Bitmap bitmap = ((BitmapDrawable)imagemProduto.getDrawable()).getBitmap();
                        new ImageSaver(getContext()).
                                setFileName(nomeProduto.getText().toString()+".png").
                                setDirectoryName("produtos").
                                save(bitmap);

                        ControleProdutos cprod = new ControleProdutos(getContext());
                        if(cprod.salvar(prod)){
                            nomeProduto.setText("");
                            custoProduto.setText("0");
                            valorVenda.setText("0");
                            imagemProduto.setImageResource(R.drawable.produto);
                            Toast.makeText(getActivity().getApplicationContext(),"Salvo com Êxito",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getActivity().getApplicationContext(),"Não foi possível salvar",Toast.LENGTH_LONG).show();
                        }
                    }

                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"Preencha os campos obrigatórios",Toast.LENGTH_LONG).show();
                }


            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);//Salva Activity
    }


    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);//Restaura o Activity

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            // Senão vamos compartilhar a imagem
            if(requestCode == RESULT_CAMERA && resultCode == -1){
                Bitmap foto = (Bitmap)data.getExtras().get("data");
                imagemProduto.setImageBitmap(foto);
            }
            else if(requestCode == RESULT_GALERIA && resultCode == -1){
                Uri imageUri = data.getData();
                String[] colunaArquivo = { MediaStore.Images.Media.DATA };
                Cursor cursor = getActivity().getContentResolver().query(imageUri, colunaArquivo, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(colunaArquivo[0]);
                String picturePath = cursor.getString(columnIndex);

                Bitmap foto = BitmapFactory.decodeFile(picturePath.toString());

                if(foto != null){
                    imagemProduto.setImageBitmap(foto);
                }
            }


    }


}
