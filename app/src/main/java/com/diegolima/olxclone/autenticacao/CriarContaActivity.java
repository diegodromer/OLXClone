package com.diegolima.olxclone.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.diegolima.olxclone.R;
import com.diegolima.olxclone.helper.FirebaseHelper;
import com.diegolima.olxclone.model.Usuario;

public class CriarContaActivity extends AppCompatActivity {

	private EditText edt_nome;
	private EditText edt_email;
	private EditText edt_telefone;
	private EditText edt_senha;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_criar_conta);

		iniciaComponentes();
	}

	public void validaDados(View view) {
		String nome = edt_nome.getText().toString();
		String email = edt_email.getText().toString();
		String telefone = edt_telefone.getText().toString();
		String senha = edt_senha.getText().toString();

		if (!nome.isEmpty()) {
			if (!email.isEmpty()) {
				if (!telefone.isEmpty()) {
					if (!senha.isEmpty()) {

						progressBar.setVisibility(View.VISIBLE);

						Usuario usuario = new Usuario();
						usuario.setNome(nome);
						usuario.setEmail(email);
						usuario.setTelefone(telefone);
						usuario.setSenha(senha);

						cadastrarUsuario(usuario);
					} else {
						edt_nome.requestFocus();
						edt_nome.setError("Preencha sua senha");
					}
				} else {
					edt_nome.requestFocus();
					edt_nome.setError("Preencha seu telefone");
				}
			} else {
				edt_nome.requestFocus();
				edt_nome.setError("Preencha seu email");
			}
		} else {
			edt_nome.requestFocus();
			edt_nome.setError("Preencha seu nome");
		}
	}

	private void cadastrarUsuario(Usuario usuario) {
		FirebaseHelper.getAuth()
				.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
				.addOnCompleteListener(task -> {
					if (task.isSuccessful()){
						String id = task.getResult().getUser().getUid();
						usuario.setId(id);
						usuario.salvar();

						//levar usuario para tela home do app
					}else{
						Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
					}
				});
	}

	private void iniciaComponentes() {
		edt_nome = findViewById(R.id.edt_nome);
		edt_email = findViewById(R.id.edt_email);
		edt_telefone = findViewById(R.id.edt_telefone);
		edt_senha = findViewById(R.id.edt_senha);
		progressBar = findViewById(R.id.progressBar);
	}


}