package com.diegolima.olxclone.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.diegolima.olxclone.R;
import com.diegolima.olxclone.activities.FormAnuncioActivity;
import com.diegolima.olxclone.autenticacao.LoginActivity;
import com.diegolima.olxclone.helper.FirebaseHelper;

public class HomeFragment extends Fragment {

	private Button btn_novo_anuncio;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_home, container, false);

		iniciaComponentes(view);
		configCliques();

		return view;
	}

	private void configCliques(){
		btn_novo_anuncio.setOnClickListener(v -> {
			if (FirebaseHelper.getAutenticado()){
				startActivity(new Intent(getActivity(), FormAnuncioActivity.class));
			}else{
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}
		});
	}

	private void iniciaComponentes(View view){
		btn_novo_anuncio = view.findViewById(R.id.btn_novo_anuncio);
	}
}