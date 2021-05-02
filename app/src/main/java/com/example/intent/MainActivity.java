package com.example.intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.intent.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding activityMainBinding;

    public static final String PARAMETRO = "PARAMETRO";

    private final int OUTRA_ACTIVITY_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());

        getSupportActionBar().setTitle("Tratando Intents");
        getSupportActionBar().setSubtitle("Tem subtítulo");

        setContentView(activityMainBinding.getRoot());

        Log.v(getString(R.string.app_name) + "/" + getLocalClassName(), "onCreate: Iniciando ciclo completo");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(getString(R.string.app_name) + "/" + getLocalClassName(), "onStart: Iniciando ciclo visivel");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(getString(R.string.app_name) + "/" + getLocalClassName(), "onResume: Iniciando ciclo primeiro plano");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(getString(R.string.app_name) + "/" + getLocalClassName(), "onPause: Finalizando ciclo primeiro plano");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(getString(R.string.app_name) + "/" + getLocalClassName(), "onStop: Finalizando ciclo visivel");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(getString(R.string.app_name) + "/" + getLocalClassName(), "onDestroy: Finalizando ciclo completo");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.outraActivityMenuItem:
                //Intent outraActivityIntent = new Intent(this, OutraActivity.class);
                Intent outraActivityIntent = new Intent("RECEBER_E_RETORNAR_ACTION");

                //passagem de forma 1
                //Bundle parametrosBundle = new Bundle();
                //parametrosBundle.putString(PARAMETRO, activityMainBinding.parametroEt.getText().toString());
                //outraActivityIntent.putExtras(parametrosBundle);

                //passagem de forma 2
                outraActivityIntent.putExtra(PARAMETRO, activityMainBinding.parametroEt.getText().toString());

                startActivityForResult(outraActivityIntent, OUTRA_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.viewMenuItem:
                Intent abrirNavegadorIntent = new Intent(Intent.ACTION_VIEW);
                abrirNavegadorIntent.setData(Uri.parse("https://sptfy.com/5Psb"));
                startActivity(abrirNavegadorIntent);

                return true;
            case R.id.ligacaoItem:
                verifyCallPhonePermission();

                return true;
        }
        return false;
    }

    private void verifyCallPhonePermission(){
        Intent ligarIntent = new Intent(Intent.ACTION_CALL);
        ligarIntent.setData(Uri.parse("tel: " + activityMainBinding.parametroEt.getText().toString()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PackageManager.PERMISSION_GRANTED == checkSelfPermission(Manifest.permission.CALL_PHONE)) {
                startActivity(ligarIntent);
            } else {
                //Solicitando permissão em tempo de execução

            }
        } else {
            startActivity(ligarIntent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OUTRA_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK ){
            String retorno = data.getStringExtra(OutraActivity.RETORNO);
            if (retorno != null){
                activityMainBinding.retornoTv.setText(retorno);
            }
        }
    }
}