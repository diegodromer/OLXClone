package com.diegolima.olxclone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diegolima.olxclone.R;
import com.diegolima.olxclone.helper.GetMask;
import com.diegolima.olxclone.model.Anuncio;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterAnuncio extends RecyclerView.Adapter<AdapterAnuncio.MyViewHolder> {

	private List<Anuncio> anuncioList;
	private OnClickListener onClickListener;

	public AdapterAnuncio(List<Anuncio> anuncioList, OnClickListener onClickListener) {
		this.anuncioList = anuncioList;
		this.onClickListener = onClickListener;
	}

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.anuncio_item, parent, false);
		return new MyViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
		Anuncio anuncio = anuncioList.get(position);

		Picasso.get().load(anuncio.getUrlImagens().get(0)).into(holder.img_anuncio);
		holder.text_titulo.setText(anuncio.getTitulo());
		holder.text_valor.setText("R$ " + GetMask.getValor(anuncio.getValor()));
		holder.text_local.setText(GetMask.getDate(anuncio.getDataPublicacao(), 4) + ", " + anuncio.getLocal().getLocalidade());

		holder.itemView.setOnClickListener(view -> onClickListener.OnClick(anuncio));
	}

	@Override
	public int getItemCount() {
		return anuncioList.size();
	}

	public interface OnClickListener{
		void OnClick(Anuncio anuncio);
	}

	static class MyViewHolder extends RecyclerView.ViewHolder {

		ImageView img_anuncio;
		TextView text_titulo;
		TextView text_valor;
		TextView text_local;

		public MyViewHolder(@NonNull View itemView) {
			super(itemView);

			img_anuncio = itemView.findViewById(R.id.img_anuncio);
			text_titulo = itemView.findViewById(R.id.text_titulo);
			text_valor = itemView.findViewById(R.id.text_valor);
			text_local = itemView.findViewById(R.id.text_local);
		}
	}
}
