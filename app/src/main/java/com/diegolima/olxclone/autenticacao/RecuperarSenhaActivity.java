package com.diegolima.olxclone.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.diegolima.olxclone.R;
import com.diegolima.olxclone.helper.FirebaseHelper;

public class RecuperarSenhaActivity extends AppCompatActivity {

	private EditText edt_email;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recuperar_senha);

		iniciaComponentes();

		configCliques();
	}

	public void validaDados(View view) {
		String email = edt_email.getText().toString();
		if (!email.isEmpty()) {
			progressBar.setVisibility(View.VISIBLE);
			enviarEmail(email);
		} else {
			edt_email.requestFocus();
			edt_email.setError("Preencha seu email");
		}
	}

	private void configCliques(){
		findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
	}

	private void enviarEmail(String email) {
		FirebaseHelper.getAuth().sendPasswordResetEmail(email).addOnCompleteListener(task -> {
			if (task.isSuccessful()) {
				Toast.makeText(this, "Verifique a Caixa de Entrada do seu e-mail", Toast.LENGTH_SHORT).show();
			} else {
				String erro = FirebaseHelper.validaErros(task.getException().getMessage());
				Toast.makeText(this, erro, Toast.LENGTH_SHORT).show();

				Log.i("INFOTESTE", "enviarEmail: " + task.getException().getMessage());
			}
			progressBar.setVisibility(View.GONE);
		});
	}

	private void iniciaComponentes() {
		TextView text_toolbar = findViewById(R.id.text_toolbar);
		text_toolbar.setText("Recuperar Senha");

		edt_email = findViewById(R.id.edt_email);
		progressBar = findViewById(R.id.progressBar);
	}
}