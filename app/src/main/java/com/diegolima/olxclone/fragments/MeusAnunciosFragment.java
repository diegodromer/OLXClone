package com.diegolima.olxclone.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.diegolima.olxclone.R;
import com.diegolima.olxclone.adapter.AdapterAnuncio;
import com.diegolima.olxclone.helper.FirebaseHelper;
import com.diegolima.olxclone.model.Anuncio;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MeusAnunciosFragment extends Fragment implements AdapterAnuncio.OnClickListener {

	private AdapterAnuncio adapterAnuncio;
	private List<Anuncio> anuncioList = new ArrayList<>();
	private SwipeableRecyclerView rv_anuncios;
	private ProgressBar progressBar;
	private TextView text_info;
	private Button btn_logar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_meus_anuncios, container, false);

		iniciaComponentes(view);
		configRV();
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		recuperaAnuncios();
	}

	private void recuperaAnuncios(){
		if (FirebaseHelper.getAutenticado()) {
			DatabaseReference anunciosRef = FirebaseHelper.getDatabaseReference()
					.child("meus_anuncios")
							.child(FirebaseHelper.getIdFirebase());

			anunciosRef.addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(@NonNull DataSnapshot snapshot) {
					if (snapshot.exists()){
						for (DataSnapshot ds : snapshot.getChildren()){ // getChildren -> para percorrer todos anuncios do nó
							Anuncio anuncio = ds.getValue(Anuncio.class);
							anuncioList.add(anuncio);
						}
						text_info.setText("");
						Collections.reverse(anuncioList);
						adapterAnuncio.notifyDataSetChanged();
						progressBar.setVisibility(View.GONE);
					}else{
						text_info.setText("Nenhum anúncio cadastrado.");
					}
				}

				@Override
				public void onCancelled(@NonNull DatabaseError error) {

				}
			});
		}else{
			btn_logar.setVisibility(View.VISIBLE);
			text_info.setText("");
			progressBar.setVisibility(View.GONE);
		}
	}

	private void configRV(){
		rv_anuncios.setLayoutManager(new LinearLayoutManager(getActivity()));
		rv_anuncios.setHasFixedSize(true);
		adapterAnuncio = new AdapterAnuncio(anuncioList, this);
		rv_anuncios.setAdapter(adapterAnuncio);
	}

	private void iniciaComponentes(View view){
		rv_anuncios = view.findViewById(R.id.rv_anuncios);
		progressBar = view.findViewById(R.id.progressBar);
		text_info = view.findViewById(R.id.text_info);
		btn_logar = view.findViewById(R.id.btn_logar);
	}

	@Override
	public void OnClick(Anuncio anuncio) {

	}
}