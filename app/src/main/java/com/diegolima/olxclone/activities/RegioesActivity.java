package com.diegolima.olxclone.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.diegolima.olxclone.R;
import com.diegolima.olxclone.adapter.AdapterEstado;
import com.diegolima.olxclone.adapter.AdapterRegiao;
import com.diegolima.olxclone.helper.RegioesList;
import com.diegolima.olxclone.helper.SPFiltro;

import java.util.ArrayList;
import java.util.List;

public class RegioesActivity extends AppCompatActivity implements AdapterRegiao.OnClickListener {

	private AdapterRegiao adapterRegiao;
	private RecyclerView rv_regioes;
	private boolean acesso = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regioes);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null){
			acesso = bundle.getBoolean("filtros");
		}

		iniciaComponentes();

		configCliques();
		configRV();
	}

	private void configRV() {
		rv_regioes.setLayoutManager(new LinearLayoutManager(this));
		rv_regioes.setHasFixedSize(true);
		adapterRegiao = new AdapterRegiao(RegioesList.getList(SPFiltro.getFiltro(this).getEstado().getUf()), this);
		rv_regioes.setAdapter(adapterRegiao);
	}

	private void configCliques() {
		findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
	}

	private void iniciaComponentes() {
		TextView text_toolbar = findViewById(R.id.text_toolbar);
		text_toolbar.setText("Selecione a região");

		rv_regioes = findViewById(R.id.rv_regioes);
	}

	@Override
	public void OnClick(String regiao) {
		if (!regiao.equals("Todas as regiões")){
			SPFiltro.setFiltro(this, "ddd", regiao.substring(4, 6));
			SPFiltro.setFiltro(this, "regiao", regiao);
		}else{
			SPFiltro.setFiltro(this, "ddd", "");
			SPFiltro.setFiltro(this, "regiao", "");
		}
		if (acesso){
			finish();
		}else{
			startActivity(new Intent(this, MainActivity.class));
		}
	}
}