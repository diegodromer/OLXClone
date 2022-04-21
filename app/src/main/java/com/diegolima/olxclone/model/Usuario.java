package com.diegolima.olxclone.model;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.diegolima.olxclone.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class Usuario {

	private String id;
	private String nome;
	private String email;
	private String telefone;
	private String senha;
	private String imagemPerfil;
	public Usuario() {

	}

	public void salvar(ProgressBar progressBar, Context context) {
		DatabaseReference referenceRef = FirebaseHelper.getDatabaseReference();
		referenceRef.child("usuarios")
				.child(this.getId())
				.setValue(this).addOnCompleteListener(task -> {
					if (task.isSuccessful()){
						progressBar.setVisibility(View.GONE);
						Toast.makeText(context, "Imagem salva com sucesso!", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(context, "Erro de upload, tente novamente mais tarde.", Toast.LENGTH_SHORT).show();
					}
					progressBar.setVisibility(View.GONE);
		});
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getImagemPerfil() {
		return imagemPerfil;
	}

	public void setImagemPerfil(String imagemPerfil) {
		this.imagemPerfil = imagemPerfil;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	@Exclude
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
}
