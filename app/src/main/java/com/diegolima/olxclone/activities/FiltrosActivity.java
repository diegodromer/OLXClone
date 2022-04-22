package com.diegolima.olxclone.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.diegolima.olxclone.R;
import com.diegolima.olxclone.helper.SPFiltro;
import com.diegolima.olxclone.model.Categoria;
import com.diegolima.olxclone.model.Filtro;
import com.google.android.material.bottomappbar.BottomAppBarTopEdgeTreatment;

import java.util.Locale;

public class FiltrosActivity extends AppCompatActivity {

	private Button btn_regioes;
	private Button btn_estados;
	private Button btn_categoria;

	private CurrencyEditText edt_valor_min;
	private CurrencyEditText edt_valor_max;

	private final int REQUEST_CATEGORIA = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filtros);

		iniciaComponentes();

		configCliques();
	}

	@Override
	protected void onStart() {
		super.onStart();
		configFiltros();
	}

	private void configFiltros() {
		Filtro filtro = SPFiltro.getFiltro(this);
		if (!filtro.getEstado().getNome().isEmpty()) {
			btn_estados.setText(filtro.getEstado().getNome());
			btn_regioes.setVisibility(View.VISIBLE);
		} else {
			btn_estados.setText("Todos os estados");
			btn_regioes.setVisibility(View.GONE);
		}

		if (!filtro.getCategoria().isEmpty()) {
			btn_categoria.setText(filtro.getCategoria());
		} else {
			btn_categoria.setText("Todas as categorias");
		}

		if (!filtro.getEstado().getRegiao().isEmpty()) {
			btn_regioes.setText(filtro.getEstado().getRegiao());
		} else {
			btn_regioes.setText("Todas as regiões");
		}

		if (filtro.getValorMin() > 0){
			edt_valor_min.setValue(filtro.getValorMin() * 100);
		}else{
			edt_valor_min.setValue(0);
		}
		if (filtro.getValorMax() > 0){
			edt_valor_min.setValue(filtro.getValorMin() * 100);
		}else{
			edt_valor_max.setValue(0);
		}
	}

	private void configCliques() {
		findViewById(R.id.btn_limpar).setOnClickListener(v -> {
			SPFiltro.LimparFiltros(this);
			finish();
		});
		findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());

		btn_regioes.setOnClickListener(v -> {
			Intent intent = new Intent(this, RegioesActivity.class);
			intent.putExtra("filtros", true);
			startActivity(intent);
		});
		btn_estados.setOnClickListener(v -> {
			Intent intent = new Intent(this, EstadosActivity.class);
			intent.putExtra("filtros", true);
			startActivity(intent);
		});
		btn_categoria.setOnClickListener(v -> {
			Intent intent = new Intent(this, CategoriasActivity.class);
			startActivityForResult(intent, REQUEST_CATEGORIA);
		});
		findViewById(R.id.btn_filtrar).setOnClickListener(v -> {
			recuperaValores();
			finish();
		});
	}

	private void recuperaValores(){
		String valorMin = String.valueOf(edt_valor_min.getRawValue() / 100);
		String valorMax = String.valueOf(edt_valor_max.getRawValue() / 100);

		SPFiltro.setFiltro(this, "valorMin", valorMin);
		SPFiltro.setFiltro(this, "valorMax", valorMax);
	}

	private void iniciaComponentes() {
		btn_regioes = findViewById(R.id.btn_regioes);
		btn_estados = findViewById(R.id.btn_estados);
		btn_categoria = findViewById(R.id.btn_categoria);

		edt_valor_min = findViewById(R.id.edt_valor_min);
		edt_valor_max = findViewById(R.id.edt_valor_max);

		edt_valor_min.setLocale(new Locale("PT", "br"));
		edt_valor_max.setLocale(new Locale("PT", "br"));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CATEGORIA) {
				Categoria categoriaSelecionada = (Categoria) data.getExtras().getSerializable("categoriaSelecionada");
				SPFiltro.setFiltro(this, "categoria", categoriaSelecionada.getNome());

				configFiltros();
			}
		}
	}
}