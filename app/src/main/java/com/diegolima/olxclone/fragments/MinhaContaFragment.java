package com.diegolima.olxclone.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.diegolima.olxclone.R;
import com.diegolima.olxclone.activities.EnderecoActivity;
import com.diegolima.olxclone.activities.MainActivity;
import com.diegolima.olxclone.activities.PerfilActivity;
import com.diegolima.olxclone.autenticacao.LoginActivity;
import com.diegolima.olxclone.helper.FirebaseHelper;
import com.diegolima.olxclone.model.Endereco;
import com.diegolima.olxclone.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.internal.InternalTokenProvider;
import com.squareup.picasso.Picasso;

public class MinhaContaFragment extends Fragment {

	private TextView text_conta;
	private TextView text_usuario;
	private ImageView imagem_perfil;

	private Usuario usuario;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_minha_conta, container, false);

		iniciaComponentes(view);
		configCliques(view);
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		recuperaUsuario();
	}

	private void recuperaUsuario(){
		if (FirebaseHelper.getAutenticado()){
			DatabaseReference usuarioRef = FirebaseHelper.getDatabaseReference()
					.child("usuarios")
					.child(FirebaseHelper.getIdFirebase());
			usuarioRef.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(@NonNull DataSnapshot snapshot) {
					usuario = snapshot.getValue(Usuario.class);
					configConta();
				}

				@Override
				public void onCancelled(@NonNull DatabaseError error) {

				}
			});
		}
	}

	private void configConta(){
		text_usuario.setText(usuario.getNome());

		if (usuario.getImagemPerfil() != null){
			imagem_perfil.setScaleType(ImageView.ScaleType.CENTER_CROP);
			Picasso.get()
					.load(usuario.getImagemPerfil())
					.placeholder(R.drawable.loading)
					.into(imagem_perfil);
		}
	}

	private void configCliques(View view){
		view.findViewById(R.id.menu_perfil).setOnClickListener(v -> redirecionaUsuario(PerfilActivity.class));
		view.findViewById(R.id.menu_endereco).setOnClickListener(v -> redirecionaUsuario(EnderecoActivity.class));

		if (FirebaseHelper.getAutenticado()){
			text_conta.setText("Sair");
		}else{
			text_conta.setText("Clique aqui");
		}

		text_conta.setOnClickListener(v -> {
			if (FirebaseHelper.getAutenticado()){
				FirebaseHelper.getAuth().signOut();
				startActivity(new Intent(getActivity(), MainActivity.class));
			}else{
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}
		});
	}

	private void redirecionaUsuario(Class<?> clazz){
		if (FirebaseHelper.getAutenticado()){
			startActivity(new Intent(getActivity(), clazz));
		}else{
			startActivity(new Intent(getActivity(), LoginActivity.class));
		}
	}

	private void iniciaComponentes(View view){
		text_conta = view.findViewById(R.id.text_conta);
		text_usuario = view.findViewById(R.id.text_usuario);
		imagem_perfil = view.findViewById(R.id.imagem_perfil);
	}
}