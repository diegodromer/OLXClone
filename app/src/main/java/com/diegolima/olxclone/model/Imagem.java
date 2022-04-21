package com.diegolima.olxclone.model;

public class Imagem {
	private String caminhoImagem;
	private int index;

	public Imagem(String caminhoImagem, int index) {
		this.caminhoImagem = caminhoImagem;
		this.index = index;
	}

	public String getCaminhoImagem() {
		return caminhoImagem;
	}

	public void setCaminhoImagem(String caminhoImagem) {
		this.caminhoImagem = caminhoImagem;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
