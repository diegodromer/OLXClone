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
import com.diegolima.olxclone.helper.EstadosList;
import com.diegolima.olxclone.helper.SPFiltro;
import com.diegolima.olxclone.model.Estado;

public class EstadosActivity extends AppCompatActivity implements AdapterEstado.OnClickListener {

	private RecyclerView rv_estados;
	private AdapterEstado adapterEstado;

	private boolean acesso = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_estados);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null){
			acesso = bundle.getBoolean("filtros");
		}

		iniciaCompomentes();
		configRV();
		configCliques();
	}

	private void configCliques(){
		findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
	}

	private void configRV(){
		rv_estados.setLayoutManager(new LinearLayoutManager(this));
		rv_estados.setHasFixedSize(true);
		adapterEstado = new AdapterEstado(EstadosList.getList(), this);
		rv_estados.setAdapter(adapterEstado);
	}

	private void iniciaCompomentes(){
		TextView text_toolbar = findViewById(R.id.text_toolbar);
		text_toolbar.setText("Estados");

		rv_estados = findViewById(R.id.rv_estados);
	}

	@Override
	public void OnClick(Estado estado) {
		if (!estado.getNome().equals("Brasil")){
			SPFiltro.setFiltro(this, "ufEstado", estado.getUf());
			SPFiltro.setFiltro(this, "nomeEstado", estado.getNome());
			if (acesso){
				finish();
			}else{
				startActivity(new Intent(this, RegioesActivity.class));
			}
		}else{
			SPFiltro.setFiltro(this, "ufEstado", "");
			SPFiltro.setFiltro(this, "nomeEstado", "");

			finish();
		}
	}
}