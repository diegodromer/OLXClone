package com.diegolima.olxclone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diegolima.olxclone.R;

import java.util.List;

public class AdapterRegiao extends RecyclerView.Adapter<AdapterRegiao.MyViewHolder> {

	private List<String> regioesList;
	private OnClickListener onClickListener;

	public AdapterRegiao(List<String> regioesList, OnClickListener onClickListener) {
		this.regioesList = regioesList;
		this.onClickListener = onClickListener;
	}

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.regiao_item, parent, false);
		return new MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
		String regiao = regioesList.get(position);

		holder.text_regiao.setText(regiao);

		holder.itemView.setOnClickListener(view -> onClickListener.OnClick(regiao));
	}

	@Override
	public int getItemCount() {
		return regioesList.size();
	}

	public interface OnClickListener {
		void OnClick(String regiao);
	}

	static class MyViewHolder extends RecyclerView.ViewHolder{

		TextView text_regiao;

		public MyViewHolder(@NonNull View itemView) {
			super(itemView);

			text_regiao = itemView.findViewById(R.id.text_regiao);
		}
	}

}
