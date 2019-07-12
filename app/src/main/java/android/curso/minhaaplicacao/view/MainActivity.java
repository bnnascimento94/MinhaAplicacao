package android.curso.minhaaplicacao.view;

import android.content.Intent;
import android.curso.minhaaplicacao.R;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button btnEntrar, btnSair;
    TextView txtUsuario, txtSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnEntrar = findViewById(R.id.btnEntrar);
        btnSair = findViewById(R.id.btnSair);
        txtUsuario = findViewById(R.id.txtUsuario);
        txtSenha = findViewById(R.id.txtSenha);


        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtUsuario.getText().toString().equals("")){
                    txtUsuario.setError("Informe o Usuário");

                }else if(txtSenha.getText().toString().equals("")){
                    txtSenha.setError("informe a senha");
                }
                else{
                    Intent telaPrincipal = new Intent(MainActivity.this, TelaPrincipalActivity.class);
                    startActivity(telaPrincipal);
                    finish();
                }


            }

        });



    }
}
