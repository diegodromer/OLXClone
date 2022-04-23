package com.diegolima.olxclone.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.diegolima.olxclone.R;
import com.diegolima.olxclone.adapter.SliderAdapter;
import com.diegolima.olxclone.autenticacao.LoginActivity;
import com.diegolima.olxclone.helper.FirebaseHelper;
import com.diegolima.olxclone.helper.GetMask;
import com.diegolima.olxclone.model.Anuncio;
import com.diegolima.olxclone.model.Favorito;
import com.diegolima.olxclone.model.Usuario;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class DetalhesAnuncioActivity extends AppCompatActivity {

	private SliderView sliderView;
	private TextView text_titulo_anuncio;
	private TextView text_valor_anuncio;
	private TextView text_data_publicacao;
	private TextView text_descricao_anuncio;
	private TextView text_categoria_anuncio;
	private TextView text_cep_anuncio;
	private TextView text_municipio_anuncio;
	private TextView text_bairro_anuncio;
	private LikeButton like_button;

	private Anuncio anuncio;

	private Usuario usuario;

	private List<String> favoritosList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalhes_anuncio);

		iniciaComponentes();

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			anuncio = (Anuncio) bundle.getSerializable("anuncioSelecionado");

			configDados();
			recuperaUsuario();
		}

		configLikeButton();
		recuperaFavoritos();
		configCliques();
	}

	private void configLikeButton() {
		like_button.setOnLikeListener(new OnLikeListener() {
			@Override
			public void liked(LikeButton likeButton) {
				if (FirebaseHelper.getAutenticado()) {
					configSnackBar("", "Anúncio salvo.", R.drawable.like_button_on_red, true);
				} else {
					likeButton.setLiked(false);
					alertAutenticacao("Para adicionar este anúncio a sua lista de favoritos é preciso estar autenticado no app, deseja fazer isso agora?");
				}
			}

			@Override
			public void unLiked(LikeButton likeButton) {
				configSnackBar("DESFAZER", "Anúncio removido.", R.drawable.like_button_off, false);
			}
		});
	}

	private void configSnackBar(String actionMsg, String msg, int icon, Boolean like) {

		configFavoritos(like);

		Snackbar snackbar = Snackbar.make(like_button, msg, Snackbar.LENGTH_SHORT);
		snackbar.setAction(actionMsg, v-> {
			if(!like){
				configFavoritos(true);
			}
		});

		TextView text_snack_bar = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
		text_snack_bar.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
		text_snack_bar.setCompoundDrawablePadding(24);
		snackbar.setActionTextColor(Color.parseColor("#F78323"))
				.setTextColor(Color.parseColor("#FFFFFF"))
				.show();
	}

	private void recuperaFavoritos(){
		if (FirebaseHelper.getAutenticado()){
			DatabaseReference favoritosRef = FirebaseHelper.getDatabaseReference()
					.child("favoritos")
					.child(FirebaseHelper.getIdFirebase());
			favoritosRef.addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(@NonNull DataSnapshot snapshot) {
					for (DataSnapshot ds : snapshot.getChildren()){
						favoritosList.add(ds.getValue(String.class));
					}

					if (favoritosList.contains(anuncio.getId())){
						like_button.setLiked(true);
					}
				}

				@Override
				public void onCancelled(@NonNull DatabaseError error) {

				}
			});
		}
	}

	private void configFavoritos(Boolean like){
		if (like){
			like_button.setLiked(true);
			favoritosList.add(anuncio.getId());
		}else{
			like_button.setLiked(false);
			favoritosList.remove(anuncio.getId());
		}

		Favorito favorito= new Favorito();
		favorito.setFavoritos(favoritosList);
		favorito.salvar();
	}

	private void alertAutenticacao(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Você não está autenticado");
		builder.setMessage(msg);
		builder.setNegativeButton("Não", ((dialog, which) -> {
			dialog.dismiss();
		})).setPositiveButton("Sim", ((dialog, which) -> {
			startActivity(new Intent(this, LoginActivity.class));
		}));

		AlertDialog dialog = builder.create();
		dialog.show();
	}

	private void configCliques() {
		findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
		findViewById(R.id.ib_ligar).setOnClickListener(v -> ligarAnunciante());
	}

	private void ligarAnunciante() {
		if (FirebaseHelper.getAutenticado()) {
			Intent intent = new Intent(Intent.ACTION_DIAL,
					Uri.fromParts("tel", usuario.getTelefone(), null));
			startActivity(intent);
		} else {
			startActivity(new Intent(this, LoginActivity.class));
		}
	}

	private void recuperaUsuario() {
		DatabaseReference usuarioRef = FirebaseHelper.getDatabaseReference()
				.child("usuarios")
				.child(anuncio.getIdUsuario());
		usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				usuario = snapshot.getValue(Usuario.class);
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});
	}

	private void configDados() {

		sliderView.setSliderAdapter(new SliderAdapter(anuncio.getUrlImagens()));
		sliderView.startAutoCycle();
		sliderView.setScrollTimeInSec(4);
		sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
		sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);

		text_titulo_anuncio.setText(anuncio.getTitulo());
		text_valor_anuncio.setText(getString(R.string.valor_anuncio, GetMask.getValor(anuncio.getValor())));
		text_data_publicacao.setText(getString(R.string.data_publicacao, GetMask.getDate(anuncio.getDataPublicacao(), 3)));
		text_descricao_anuncio.setText(anuncio.getDescricao());
		text_categoria_anuncio.setText(anuncio.getCategoria());
		text_cep_anuncio.setText(anuncio.getLocal().getCep());
		text_municipio_anuncio.setText(anuncio.getLocal().getLocalidade());
		text_bairro_anuncio.setText(anuncio.getLocal().getBairro());
	}

	private void iniciaComponentes() {
		sliderView = findViewById(R.id.sliderView);
		text_titulo_anuncio = findViewById(R.id.text_titulo_anuncio);
		text_valor_anuncio = findViewById(R.id.text_valor_anuncio);
		text_data_publicacao = findViewById(R.id.text_data_publicacao);
		text_descricao_anuncio = findViewById(R.id.text_descricao_anuncio);
		text_categoria_anuncio = findViewById(R.id.text_categoria_anuncio);
		text_cep_anuncio = findViewById(R.id.text_cep_anuncio);
		text_municipio_anuncio = findViewById(R.id.text_municipio_anuncio);
		text_bairro_anuncio = findViewById(R.id.text_bairro_anuncio);
		like_button = findViewById(R.id.like_button);
	}
}