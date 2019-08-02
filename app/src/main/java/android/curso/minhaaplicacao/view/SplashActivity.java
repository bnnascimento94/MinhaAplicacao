package android.curso.minhaaplicacao.view;

import android.content.Intent;
import android.curso.minhaaplicacao.R;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        apresentarTelaSplash();
    }


    private void apresentarTelaSplash(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent telaPrincipal = new Intent(SplashActivity.this, TelaPrincipalActivity.class);
                startActivity(telaPrincipal);
                finish();
            }
        },SPLASH_TIME_OUT);

    }
}
