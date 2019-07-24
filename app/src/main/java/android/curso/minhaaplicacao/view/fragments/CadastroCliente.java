package android.curso.minhaaplicacao.view.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.curso.minhaaplicacao.classes.CarregadorDeFoto;
import android.curso.minhaaplicacao.classes.ImageSaver;
import android.curso.minhaaplicacao.controller.ControleClientes;
import android.curso.minhaaplicacao.model.Cliente;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.curso.minhaaplicacao.R;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class CadastroCliente extends Fragment {
    private static final int SOLICITAR_PERMISSAO = 1;
    Context context;
    ArrayList<Cliente> cliente;
    View view;
    byte[] fotoArray;
    Bitmap raw;
    CircleImageView foto;
    FloatingActionButton camera;
    private AlertDialog alerta;
    TextView nomeCliente, email, telefone, endereco;
    Button btnSalvar;
    /** RESULT_CAMERA */
    private static final int RESULT_CAMERA = 111;

    /** RESULT_GALERIA */
    private static final int RESULT_GALERIA = 222;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cadastro_cliente, container, false);
        nomeCliente = view.findViewById(R.id.txtNome);
        email = view.findViewById(R.id.txtEmail);
        telefone = view.findViewById(R.id.txtTelefone);
        endereco = view.findViewById(R.id.txtEndereco);
        camera = view.findViewById(R.id.editFoto);
        btnSalvar = view.findViewById(R.id.btnSalvar);
        foto = view.findViewById(R.id.imageView2);


        foto.setImageResource(R.drawable.user_no_pic);

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
                            if(new ImageSaver(getContext(),"clientes",nomeCliente.getText().toString()+".png").deleteFile()){
                                new ImageSaver(getContext()).
                                        setFileName(nomeCliente.getText().toString()+".png").
                                        setDirectoryName("clientes").
                                        save(bitmap);

                                ControleClientes clientes = new ControleClientes(getActivity().getApplicationContext());
                                clientes.alterar(client);
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


    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);//Restaura o Activity

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == RESULT_CAMERA && resultCode == -1){
                try {
                    Bitmap foto1 = (Bitmap)data.getExtras().get("data");
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    Bitmap fotoFinal = Bitmap.createBitmap(foto1, 0, 0, foto1.getWidth(),
                            foto1.getHeight(), matrix, true);
                    foto.setImageBitmap(fotoFinal);
                } catch (Exception e) {
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

}
