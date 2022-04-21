package com.diegolima.olxclone.model;

import com.diegolima.olxclone.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.List;

public class Anuncio {
	private String id;
	private String idUsuario;
	private String titulo;
	private double valor;
	private String descricao;
	private String categoria;
	private Local local;
	private long dataPublicacao;
	private List<String> urlImagens = new ArrayList<>();

	public Anuncio() {
		DatabaseReference anuncioRef = FirebaseHelper.getDatabaseReference();
		this.setId(anuncioRef.push().getKey());
	}

	public void salvar(boolean novoAnuncio){
		DatabaseReference anuncioPublicoRef = FirebaseHelper.getDatabaseReference()
				.child("anuncios_publicos")
				.child(this.getId());
		anuncioPublicoRef.setValue(this);

		DatabaseReference meusAnunciosRef = FirebaseHelper.getDatabaseReference()
				.child("meus_anuncios")
				.child(FirebaseHelper.getIdFirebase())
				.child(this.getId());
		meusAnunciosRef.setValue(this);

		if (novoAnuncio){
			DatabaseReference dataAnuncioPublico = anuncioPublicoRef
					.child("dataPublicacao");
			dataAnuncioPublico.setValue(ServerValue.TIMESTAMP);

			DatabaseReference dataMeusAnuncio = meusAnunciosRef
					.child("dataPublicacao");
			dataMeusAnuncio.setValue(ServerValue.TIMESTAMP);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public long getDataPublicacao() {
		return dataPublicacao;
	}

	public void setDataPublicacao(long dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}

	public List<String> getUrlImagens() {
		return urlImagens;
	}

	public void setUrlImagens(List<String> urlImagens) {
		this.urlImagens = urlImagens;
	}
}
