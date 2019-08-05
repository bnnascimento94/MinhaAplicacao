package com.vullpes.app.view.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import com.vullpes.app.classes.CarregadorDeFoto;
import com.vullpes.app.classes.ImageSaver;
import com.vullpes.app.classes.Mask;
import com.vullpes.app.classes.OnBackPressed;
import com.vullpes.app.controller.ControleClientes;
import com.vullpes.app.model.Cliente;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class CadastroCliente extends Fragment implements OnBackPressed {
    private static final int SOLICITAR_PERMISSAO = 1;
    private ProgressBar progressBar;
    Boolean concluido = false;
    Context context;
    ArrayList<Cliente> cliente;
    View view;
    CircleImageView foto;
    FloatingActionButton camera;
    private AlertDialog alerta;
    EditText nomeCliente, email, telefone, endereco;
    Button btnSalvar;
    /** RESULT_CAMERA */
    private static final int RESULT_CAMERA = 111;
    /** RESULT_GALERIA */
    private static final int RESULT_GALERIA = 222;
    private String mImageFileLocation;


    public CadastroCliente() {
        // Required empty public constructor
        context  = getContext();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            cliente =(ArrayList<Cliente>) bundle.getSerializable("cliente");
        }

        // The callback can be enabled or disabled here or in handleOnBackPressed()
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cadastro_cliente, container, false);
        progressBar = view.findViewById(R.id.progressBar_cyclic);
        nomeCliente = view.findViewById(R.id.txtNome);
        email = view.findViewById(R.id.txtEmail);
        telefone = view.findViewById(R.id.txtTelefone);
        endereco = view.findViewById(R.id.txtEndereco);
        camera = view.findViewById(R.id.editFoto);
        btnSalvar = view.findViewById(R.id.btnSalvar);
        foto = view.findViewById(R.id.imageView2);
        foto.setImageResource(R.drawable.user_no_pic);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Cadastro Cliente");

        telefone.addTextChangedListener(Mask.insert("(##)#####-####", telefone));

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
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
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,FileProvider.getUriForFile(getActivity(),
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


        if(cliente !=null){
            String nome = cliente.get(0).getNomeCliente();
            nomeCliente.setText(cliente.get(0).getNomeCliente());
            email.setText(cliente.get(0).getEmailCliente());
            telefone.setText(String.valueOf(cliente.get(0).getTelefoneCliente()));
            endereco.setText(cliente.get(0).getEnderecoCliente());

            Bitmap bitmap = new ImageSaver(getContext()).
                    setFileName(cliente.get(0).getNomeArquivo()).
                    setDirectoryName(cliente.get(0).getDiretorioFoto()).
                    load();

            if(bitmap!=null){
                foto.setImageBitmap(bitmap);
            }else{
                foto.setImageResource(R.drawable.user_no_pic);
            }
        }

        btnSalvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean dadosValidados = true;

                if(!(nomeCliente.getText().length()>0)){
                    nomeCliente.setError("*");
                    nomeCliente.requestFocus();
                    dadosValidados = false;
                }
                else if(!(telefone.getText().length()>0)){
                    telefone.setError("*");
                    telefone.requestFocus();
                    dadosValidados = false;
                }
                else if(!(endereco.getText().length()>0)){
                    endereco.setError("*");
                    endereco.requestFocus();
                    dadosValidados = false;
                }

                if(dadosValidados){
                    if(cliente!=null){
                        try{
                            Cliente client = new Cliente();
                            client.setIdCliente(cliente.get(0).getIdCliente());
                            client.setNomeCliente(nomeCliente.getText().toString());
                            client.setEmailCliente(email.getText().toString());
                            client.setTelefoneCliente(telefone.getText().toString());
                            client.setEnderecoCliente(endereco.getText().toString());
                            client.setDiretorioFoto("clientes");
                            client.setNomeArquivo(nomeCliente.getText().toString()+".png");

                            Bitmap bitmap = ((BitmapDrawable)foto.getDrawable()).getBitmap();
                            if(new ImageSaver(getContext(),"clientes",cliente.get(0).getNomeCliente()+".png").deleteFile()){
                                new ImageSaver(getContext()).
                                        setFileName(nomeCliente.getText().toString()+".png").
                                        setDirectoryName("clientes").
                                        save(bitmap);

                                ControleClientes clientes = new ControleClientes(getActivity().getApplicationContext());
                                clientes.alterar(client);
                                concluido = true;
                                Toast.makeText(getActivity().getApplicationContext(),"Dados alterados com êxito!",Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception e){
                            Log.e("Mensagem de ERRO",""+e);
                        }
                    }
                    else{

                        Cliente cliente = new Cliente();
                        cliente.setNomeCliente(nomeCliente.getText().toString());
                        cliente.setEmailCliente(email.getText().toString());
                        cliente.setTelefoneCliente(telefone.getText().toString());
                        cliente.setEnderecoCliente(endereco.getText().toString());
                        cliente.setDiretorioFoto("clientes");
                        cliente.setNomeArquivo(nomeCliente.getText().toString()+".png");

                        Bitmap bitmap = ((BitmapDrawable)foto.getDrawable()).getBitmap();

                        new ImageSaver(getContext()).
                                setFileName(nomeCliente.getText().toString()+".png").
                                setDirectoryName("clientes").
                                save(bitmap);

                        ControleClientes clientes = new ControleClientes(getActivity().getApplicationContext());
                        try{
                            if(clientes.salvar(cliente)){
                                nomeCliente.setText("");
                                email.setText("");
                                telefone.setText("");
                                endereco.setText("");
                                foto.setImageResource(R.drawable.user_no_pic);
                                Toast.makeText(getActivity().getApplicationContext(),"Dados salvos com êxito!",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getActivity().getApplicationContext(),"Não cadastrado, verifique se não tem um cliente com mesmo nome.",Toast.LENGTH_LONG).show();
                            }

                        }catch (Exception e){
                            Toast.makeText(getActivity().getApplicationContext(),"Não cadastrado, verifique se não tem um cliente com mesmo nome.",Toast.LENGTH_LONG).show();
                            Log.e("Mensagem de ERRO",""+e);
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
            if(requestCode == RESULT_CAMERA && resultCode == -1){
                try {
                    rotateImage(setReducedImageSize());
                } catch (Exception e ) {
                    Toast.makeText(getContext()," "+e,Toast.LENGTH_LONG);
                }
            }
            else if(requestCode == RESULT_GALERIA && resultCode == -1){

                try {
                    Uri imageUri = data.getData();
                    String[] colunaArquivo = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getActivity().getContentResolver().query(imageUri, colunaArquivo, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(colunaArquivo[0]);
                    String picturePath = cursor.getString(columnIndex);
                    Bitmap fotoPositioned = CarregadorDeFoto.carrega(picturePath);
                    if(foto != null){
                        foto.setImageBitmap(fotoPositioned);
                    }
                } catch (IOException e) {
                    Toast.makeText(getContext()," "+e,Toast.LENGTH_LONG);
                }

            }
    }

    private void startTimerThread(final Boolean valor) {
        final Integer[] controle = {0};
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            public void run() {

                while(controle[0] < 100) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        public void run() {
                            try{
                                progressBar.setVisibility(View.VISIBLE);
                            }
                            catch(Exception e){
                                Log.e("Erro total carrinho=>",""+e);
                            }

                        }
                    });
                    controle[0] += 1;
                }
                progressBar.setVisibility(View.GONE);
            }
        };
        new Thread(runnable).start();
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
        foto.setImageBitmap(rotatedBitmap);

    }

    private Bitmap setReducedImageSize(){
        int imageTargetViewWidth = foto.getWidth();
        int imageTargetViewHeight = foto.getHeight();

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
