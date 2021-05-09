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
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.intent.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding activityMainBinding;

    public static final String PARAMETRO = "PARAMETRO";

    private final int OUTRA_ACTIVITY_REQUEST_CODE = 0;
    private final int CALL_PHONE_PERMISSION_REQUEST_CODE = 1;
    private final int PICK_IMAGE_FILE_REQUEST_CODE = 3;


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
            case R.id.discadorMenuItem:
                Intent discarItem = new Intent(Intent.ACTION_DIAL);
                discarItem.setData(Uri.parse("tel:"+activityMainBinding.parametroEt.getText().toString()));
                startActivity(discarItem);
                return true;
            case R.id.pickMenuItem:
                startActivityForResult(getPickImageIntent(), PICK_IMAGE_FILE_REQUEST_CODE);
                return true;
            case R.id.chooseMenuItem:
                Intent escolherActivityIntent = new Intent(Intent.ACTION_CHOOSER);
                escolherActivityIntent.putExtra(Intent.EXTRA_INTENT, getPickImageIntent());
                escolherActivityIntent.putExtra(Intent.EXTRA_TITLE, "Escolha um app pra selecionar a imagem!");
                startActivityForResult(escolherActivityIntent, PICK_IMAGE_FILE_REQUEST_CODE);
                return true;
        }
        return false;
    }

    private Intent getPickImageIntent(){
        Intent pegarImagemIntent = new Intent(Intent.ACTION_PICK);
        String diretorioImagens = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
        pegarImagemIntent.setDataAndType(Uri.parse(diretorioImagens), "image/*");

        return pegarImagemIntent;
    }

    private void verifyCallPhonePermission(){
        Intent ligarIntent = new Intent(Intent.ACTION_CALL);
        ligarIntent.setData(Uri.parse("tel:" + activityMainBinding.parametroEt.getText().toString()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PackageManager.PERMISSION_GRANTED == checkSelfPermission(Manifest.permission.CALL_PHONE)) {
                startActivity(ligarIntent);
            } else {
                //Solicitando permissão em tempo de execução
                Toast.makeText(this, "Sem permissão para efetuar ligações", Toast.LENGTH_SHORT).show();
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_PERMISSION_REQUEST_CODE);
            }
        } else {
            startActivity(ligarIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PHONE_PERMISSION_REQUEST_CODE) {
            if (permissions[0].equals(Manifest.permission.CALL_PHONE) && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "É necessaria dar permissão para efetuar ligações", Toast.LENGTH_SHORT).show();
            }
            verifyCallPhonePermission();
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
        } else if (requestCode == PICK_IMAGE_FILE_REQUEST_CODE && resultCode == RESULT_OK){
                Uri imagemUri = data.getData();

                Intent vizualizarImagem = new Intent(Intent.ACTION_VIEW, imagemUri);
                startActivity(vizualizarImagem);    
        }
    }
}