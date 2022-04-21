package com.diegolima.olxclone.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.diegolima.olxclone.R;
import com.diegolima.olxclone.helper.FirebaseHelper;
import com.diegolima.olxclone.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.santalu.maskara.widget.MaskEditText;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class PerfilActivity extends AppCompatActivity {

	private EditText edt_nome;
	private MaskEditText edt_telefone;
	private EditText edt_email;
	private ProgressBar progressBar;

	private final int SELECAO_GALERIA = 100;

	private ImageView imagem_perfil;

	private String caminhoImagem;

	private Usuario usuario;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perfil);

		iniciaComponentes();
		configCliques();
		recuperaPerfil();
	}

	private void salvarImagemPerfil() {
		StorageReference storageReference = FirebaseHelper.getStorageReference()
				.child("imagens")
				.child("perfil")
				.child(FirebaseHelper.getIdFirebase() + ".jpeg");

		UploadTask uploadTask = storageReference.putFile(Uri.parse(caminhoImagem));
		uploadTask.addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnCompleteListener(task -> {
			String urlImagem = task.getResult().toString();
			usuario.setImagemPerfil(urlImagem);
			usuario.salvar(progressBar, getBaseContext());
		})).addOnFailureListener(e -> Toast.makeText(this, "Erro de upload, tente novamente mais tarde.", Toast.LENGTH_SHORT).show());
	}

	private void recuperaPerfil() {
		progressBar.setVisibility(View.VISIBLE);
		DatabaseReference perfilRef = FirebaseHelper.getDatabaseReference()
				.child("usuarios")
				.child(FirebaseHelper.getIdFirebase());
		perfilRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				usuario = snapshot.getValue(Usuario.class);
				configDados();
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});
	}

	private void configDados() {
		edt_nome.setText(usuario.getNome());
		edt_telefone.setText(usuario.getTelefone());
		edt_email.setText(usuario.getEmail());

		progressBar.setVisibility(View.GONE);

		if (usuario.getImagemPerfil() != null){
			Picasso.get().load(usuario.getImagemPerfil()).into(imagem_perfil);
		}

	}

	public void validaDados(View view) {
		String nome = edt_nome.getText().toString();
		String telefone = edt_telefone.getUnMasked();

		if (!nome.isEmpty()) {
			if (!telefone.isEmpty()) {
				if (telefone.length() == 11) {

					progressBar.setVisibility(View.VISIBLE);

					if (caminhoImagem != null){
						salvarImagemPerfil();
					}else{
						usuario.salvar(progressBar, getBaseContext());
					}
				} else {
					edt_telefone.requestFocus();
					edt_telefone.setError("Telefone inválido.");
				}
			} else {
				edt_telefone.requestFocus();
				edt_telefone.setError("Preencha o telefone.");
			}
		}
	}

	private void configCliques() {
		imagem_perfil.setOnClickListener(v -> verificaPermissaoGaleria());
	}

	private void verificaPermissaoGaleria() {
		PermissionListener permissionListener = new PermissionListener() {
			@Override
			public void onPermissionGranted() {
				abrirGaleria();
			}

			private void abrirGaleria() {
				Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, SELECAO_GALERIA);
			}

			@Override
			public void onPermissionDenied(List<String> deniedPermissions) {
				Toast.makeText(PerfilActivity.this, "Permissões Negadas.", Toast.LENGTH_SHORT).show();
			}
		};

		TedPermission.create()
				.setPermissionListener(permissionListener)
				.setDeniedTitle("Permissões negadas")
				.setDeniedMessage("Se você não aceitar a permissão, não poderá acessar a Galeria do dispositivo\ndeseja ativar a permissão agora?")
				.setDeniedCloseButtonText("Não")
				.setGotoSettingButtonText("Sim")
				.setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
				.check();
	}

	private void iniciaComponentes() {
		TextView text_toolbar = findViewById(R.id.text_toolbar);
		text_toolbar.setText("Perfil");

		edt_nome = findViewById(R.id.edt_nome);
		edt_telefone = findViewById(R.id.edt_telefone);
		edt_email = findViewById(R.id.edt_email);
		progressBar = findViewById(R.id.progressBar);

		imagem_perfil = findViewById(R.id.imagem_perfil);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			Uri imagemSelecionada = data.getData();
			Bitmap imagemRecuperada;

			try {
				imagemRecuperada = MediaStore.Images.Media.getBitmap(getContentResolver(), imagemSelecionada);
				imagem_perfil.setImageBitmap(imagemRecuperada);

				caminhoImagem = imagemSelecionada.toString();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}