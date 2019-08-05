package com.vullpes.app.view.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.vullpes.app.classes.ImageSaver;
import com.vullpes.app.classes.MoneyTextWatcher;
import com.vullpes.app.classes.OnBackPressed;
import com.vullpes.app.controller.ControleProdutos;

import com.vullpes.app.model.Produto;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.curso.minhaaplicacao.R;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;



public class CadastroProduto extends Fragment implements OnBackPressed {
    private static final int SOLICITAR_PERMISSAO = 1;
    View view;
    Context context;
    ArrayList<Produto> produto;
    CircleImageView imagemProduto;
    FloatingActionButton camera;
    private String mImageFileLocation;
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
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Cadastro Produto");

        if(produto!=null){
            NumberFormat z = NumberFormat.getCurrencyInstance();
            nomeProduto.setText(produto.get(0).getNomeProduto());
            custoProduto.setText(z.format(produto.get(0).getCustoProduto()));
            valorVenda.setText(z.format(produto.get(0).getValorUnitario()));

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

        Locale mLocale = new Locale("pt", "BR");
        valorVenda.addTextChangedListener(new MoneyTextWatcher(valorVenda, mLocale));
        custoProduto.addTextChangedListener(new MoneyTextWatcher(custoProduto, mLocale));

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
                            checarPermissao();
                            Intent intent = new Intent();
                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                            File photoFile = null;
                            try{
                                photoFile = createImageFile();
                            }catch(Exception e){
                                e.printStackTrace();
                            }


                            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile( getActivity(),
                                    getActivity().getApplicationContext().getPackageName() + ".provider",
                                    photoFile));
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
                            if(new ImageSaver(getContext(),"produtos",produto.get(0).getNomeProduto().toString()+".png").deleteFile()){
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
                        prod.setCustoProduto(Double.parseDouble(cleancusto));
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            // Senão vamos compartilhar a imagem
            if(requestCode == RESULT_CAMERA && resultCode == -1){
                rotateImage(setReducedImageSize());
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

    File createImageFile(){
        File image = null;
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "IMAGE_"+timeStamp+"_";
            File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            image = File.createTempFile(imageFileName,".jpg",storageDirectory);
            mImageFileLocation = image.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private void checarPermissao(){

        // Verifica  o estado da permissão de WRITE_EXTERNAL_STORAGE
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);


        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            // Se for diferente de PERMISSION_GRANTED, então vamos exibir a tela padrão
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, SOLICITAR_PERMISSAO);
        } else {
            // Senão vamos compartilhar a imagem

        }
    }



    private void rotateImage(Bitmap bitmap){
        ExifInterface exifInterface = null;
        try{
            exifInterface = new ExifInterface(mImageFileLocation);
        }catch(IOException e){
            e.printStackTrace();
        }
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientation){

            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            default:
        }
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        imagemProduto.setImageBitmap(rotatedBitmap);

    }

    private Bitmap setReducedImageSize(){
        int imageTargetViewWidth = imagemProduto.getWidth();
        int imageTargetViewHeight = imagemProduto.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
        int cameraImageWidth = bmOptions.outWidth;
        int cameraImageHeight = bmOptions.outHeight;

        int scaleFactor = Math.min(cameraImageWidth/imageTargetViewWidth,cameraImageHeight/imageTargetViewHeight);
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(mImageFileLocation, bmOptions);

    }


    @Override
    public void OnBackPressed() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_fragment, new Graficos()).commit();
    }
}
