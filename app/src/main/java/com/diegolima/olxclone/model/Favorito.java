package com.diegolima.olxclone.model;

import com.diegolima.olxclone.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class Favorito {

	private List<String> favoritos;

	public void salvar(){
		DatabaseReference favoritoRef = FirebaseHelper.getDatabaseReference()
				.child("favoritos")
				.child(FirebaseHelper.getIdFirebase());
		favoritoRef.setValue(getFavoritos());
	}

	public List<String> getFavoritos() {
		return favoritos;
	}

	public void setFavoritos(List<String> favoritos) {
		this.favoritos = favoritos;
	}
}
