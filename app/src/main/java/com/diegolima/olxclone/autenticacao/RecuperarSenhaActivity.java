package com.diegolima.olxclone.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.diegolima.olxclone.R;

public class RecuperarSenhaActivity extends AppCompatActivity {

    private EditText edt_email;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        iniciaComponentes();
    }

    public void validaDados(View view){
        String email = edt_email.getText().toString();
        if (!email.isEmpty()){
            enviarEmail(email);
        }else {
            edt_email.requestFocus();
            edt_email.setError("Preencha seu email");
        }
    }

    private void enviarEmail(String email){

    }

    private void iniciaComponentes(){
        edt_email = findViewById(R.id.edt_email);
        progressBar = findViewById(R.id.progressBar);
    }
}