package com.diegolima.olxclone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diegolima.olxclone.R;
import com.diegolima.olxclone.model.Categoria;

import java.util.List;

public class AdapterCategoria extends RecyclerView.Adapter<AdapterCategoria.MyViewHolder> {

	private List<Categoria> categoriaList;
	private OnClickListener onClickListener;

	public AdapterCategoria(List<Categoria> categoriaList, OnClickListener onClickListener) {
		this.categoriaList = categoriaList;
		this.onClickListener = onClickListener;
	}

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categoria_item, parent, false);
		return new MyViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
		Categoria categoria = categoriaList.get(position);

		holder.img_categoria.setImageResource(categoria.getCaminho());
		holder.txt_categoria.setText(categoria.getNome());

		holder.itemView.setOnClickListener(v -> onClickListener.OnClick(categoria));
	}

	@Override
	public int getItemCount() {
		return categoriaList.size();
	}

	public interface OnClickListener{
		void OnClick(Categoria categoria);
	}

	static class MyViewHolder extends RecyclerView.ViewHolder{

		ImageView img_categoria;
		TextView txt_categoria;

		public MyViewHolder(@NonNull View itemView) {
			super(itemView);
			img_categoria = itemView.findViewById(R.id.img_categoria);
			txt_categoria = itemView.findViewById(R.id.txt_categoria);
		}
	}

}
