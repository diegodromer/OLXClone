package com.diegolima.olxclone.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import com.diegolima.olxclone.R;
import com.diegolima.olxclone.activities.CategoriasActivity;
import com.diegolima.olxclone.activities.DetalhesAnuncioActivity;
import com.diegolima.olxclone.activities.EstadosActivity;
import com.diegolima.olxclone.activities.FiltrosActivity;
import com.diegolima.olxclone.activities.FormAnuncioActivity;
import com.diegolima.olxclone.adapter.AdapterAnuncio;
import com.diegolima.olxclone.autenticacao.LoginActivity;
import com.diegolima.olxclone.helper.FirebaseHelper;
import com.diegolima.olxclone.helper.SPFiltro;
import com.diegolima.olxclone.model.Anuncio;
import com.diegolima.olxclone.model.Categoria;
import com.diegolima.olxclone.model.Filtro;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment implements AdapterAnuncio.OnClickListener {

	private final int REQUEST_CATEGORIA = 100;

	private AdapterAnuncio adapterAnuncio;
	private List<Anuncio> anuncioList = new ArrayList<>();

	private RecyclerView rv_anuncios;
	private ProgressBar progressBar;
	private TextView text_info;

	private Filtro filtro = new Filtro();

	private SearchView search_view;
	private EditText edit_search_view;

	private Button btn_filtros;
	private Button btn_categorias;
	private Button btn_regioes;

	private Button btn_novo_anuncio;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_home, container, false);

		iniciaComponentes(view);

		configRV();

		configCliques();

		configSearchView();

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();

		configFiltros();
	}

	private void configSearchView() {
		edit_search_view = search_view.findViewById(androidx.appcompat.R.id.search_src_text);

		search_view.findViewById(androidx.appcompat.R.id.search_close_btn).setOnClickListener(v -> {
			limparPesquisa();
		});

		search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				SPFiltro.setFiltro(requireActivity(), "pesquisa", query);

				configFiltros();

				ocultarTeclado();

				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {

				return false;
			}
		});
	}

	private void limparPesquisa() {
		search_view.clearFocus();

		edit_search_view.setText("");

		SPFiltro.setFiltro(requireActivity(), "pesquisa", "");

		configFiltros();

		ocultarTeclado();
	}

	private void configFiltros() {
		filtro = SPFiltro.getFiltro(requireActivity());

		if (!filtro.getEstado().getRegiao().isEmpty()) {
			btn_regioes.setText(filtro.getEstado().getRegiao());
		} else {
			btn_regioes.setText("Regiões");
		}
		if (!filtro.getCategoria().isEmpty()) {
			btn_categorias.setText(filtro.getCategoria());
		} else {
			btn_categorias.setText("Categorias");
		}

		recuperaAnuncios();
	}

	private void recuperaAnuncios() {
		DatabaseReference anunciosRef = FirebaseHelper.getDatabaseReference()
				.child("anuncios_publicos");

		anunciosRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				if (snapshot.exists()) {
					anuncioList.clear();
					for (DataSnapshot ds : snapshot.getChildren()) { // getChildren -> para percorrer todos anuncios do nó
						Anuncio anuncio = ds.getValue(Anuncio.class);
						anuncioList.add(anuncio);
					}

					//filtro por categoria
					if (!filtro.getCategoria().isEmpty()) {
						if (!filtro.getCategoria().equals("Todas as categorias")) {
							for (Anuncio anuncio : new ArrayList<>(anuncioList)) {
								if (!anuncio.getCategoria().equals(filtro.getCategoria())) {
									anuncioList.remove(anuncio);
								}
							}
						}
					}

					//filtro por estado
					if (!filtro.getEstado().getUf().isEmpty()) {
						for (Anuncio anuncio : new ArrayList<>(anuncioList)) {
							if (!anuncio.getLocal().getUf().contains(filtro.getEstado().getUf()))
								anuncioList.remove(anuncio);
						}
					}

					//filtro por ddd
					if (!filtro.getEstado().getDdd().isEmpty()) {
						for (Anuncio anuncio : new ArrayList<>(anuncioList)) {
							if (!anuncio.getLocal().getUf().equals(filtro.getEstado().getUf())) {
								anuncioList.remove(anuncio);
							}
						}
					}

					//filtro por nome pesquisado
					if (!filtro.getPesquisa().isEmpty()) {
						for (Anuncio anuncio : new ArrayList<>(anuncioList)) {
							if (!anuncio.getTitulo().toLowerCase().contains(filtro.getPesquisa().toLowerCase())) {
								anuncioList.remove(anuncio);
							}
						}
					}

					//filtro por valor minimo
					if (filtro.getValorMin() > 0) {
						for (Anuncio anuncio : new ArrayList<>(anuncioList)) {
							if (anuncio.getValor() < filtro.getValorMin()) {
								anuncioList.remove(anuncio);
							}
						}
					}

					//filtro por valor maximo
					if (filtro.getValorMax() > 0) {
						for (Anuncio anuncio : new ArrayList<>(anuncioList)) {
							if (anuncio.getValor() > filtro.getValorMax()) {
								anuncioList.remove(anuncio);
							}
						}
					}

					text_info.setText("");
					Collections.reverse(anuncioList);
					adapterAnuncio.notifyDataSetChanged();
					progressBar.setVisibility(View.GONE);
				} else {
					text_info.setText("Nenhum anúncio cadastrado.");
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});
	}

	private void configCliques() {
		btn_novo_anuncio.setOnClickListener(v -> {
			if (FirebaseHelper.getAutenticado()) {
				startActivity(new Intent(getActivity(), FormAnuncioActivity.class));
			} else {
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}
		});
		btn_categorias.setOnClickListener(view -> {
			Intent intent = new Intent(requireContext(), CategoriasActivity.class);
			intent.putExtra("todas", true);
			startActivityForResult(intent, REQUEST_CATEGORIA);
		});
		btn_filtros.setOnClickListener(v -> startActivity(new Intent(requireContext(), FiltrosActivity.class)));
		btn_regioes.setOnClickListener(v -> startActivity(new Intent(requireContext(), EstadosActivity.class)));
	}

	private void configRV() {
		rv_anuncios.setLayoutManager(new LinearLayoutManager(getActivity()));
		rv_anuncios.setHasFixedSize(true);
		adapterAnuncio = new AdapterAnuncio(anuncioList, this);
		rv_anuncios.setAdapter(adapterAnuncio);
	}

	private void iniciaComponentes(View view) {
		btn_novo_anuncio = view.findViewById(R.id.btn_novo_anuncio);

		rv_anuncios = view.findViewById(R.id.rv_anuncios);
		progressBar = view.findViewById(R.id.progressBar);
		text_info = view.findViewById(R.id.text_info);

		btn_filtros = view.findViewById(R.id.btn_filtros);
		btn_categorias = view.findViewById(R.id.btn_categorias);
		btn_regioes = view.findViewById(R.id.btn_regioes);
		search_view = view.findViewById(R.id.search_view);
		search_view = view.findViewById(R.id.search_view);
	}

	private void ocultarTeclado() {
		InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(search_view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	@Override
	public void OnClick(Anuncio anuncio) {
		Intent intent = new Intent(requireContext(), DetalhesAnuncioActivity.class);
		intent.putExtra("anuncioSelecionado", anuncio);
		startActivity(intent);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == requireActivity().RESULT_OK) {
			if (requestCode == REQUEST_CATEGORIA) {
				Categoria categoriaSelecionada = (Categoria) data.getExtras().getSerializable("categoriaSelecionada");
				SPFiltro.setFiltro(requireActivity(), "categoria", categoriaSelecionada.getNome());

				configFiltros();
			}
		}
	}
}