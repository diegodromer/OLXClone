package com.diegolima.olxclone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.diegolima.olxclone.R;
import com.squareup.picasso.Picasso;

import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private final List<String> urlsImagens;

    public SliderAdapter(List<String> urlsImagens) {
        this.urlsImagens = urlsImagens;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        String urlImagem = urlsImagens.get(position);
        Picasso.get().load(urlImagem).into(viewHolder.imageViewBackground);
    }

    @Override
    public int getCount() {
        return urlsImagens.size();
    }

    static class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        ImageView imageViewBackground;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.imageViewBackground);
        }
    }

}
