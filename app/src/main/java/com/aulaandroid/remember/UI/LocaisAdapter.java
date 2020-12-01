package com.aulaandroid.remember.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aulaandroid.remember.Model.Locais;
import com.aulaandroid.remember.R;

import java.util.ArrayList;
import java.util.List;

public class LocaisAdapter extends RecyclerView.Adapter<LocaisAdapter.LocaisHolder> {
    private List<Locais> results = new ArrayList<>();
    private static ItemClickListener itemClickListener;
    @NonNull
    @Override
    public LocaisHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_locais, parent, false);
        return new LocaisHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull LocaisHolder holder, int position) {
        Locais locais = results.get(position);
        holder.tituloLocais.setText(locais.getTitulo());
        holder.descricaoLocais.setText(locais.getDescricao());
        holder.dataLocais.setText(locais.getHoje());
        holder.latlonLocais.setText(locais.getLatLon());
    }
    @Override
    public int getItemCount() {
        return results.size();
    }
    public void setResults(List<Locais> results) {
        this.results = results;
        notifyDataSetChanged();
    }
    class LocaisHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private EditText tituloLocais;
        private EditText descricaoLocais;
        private EditText dataLocais;
        private TextView latlonLocais;

        public LocaisHolder(@NonNull View itemView) {
            super(itemView);
            tituloLocais = itemView.findViewById(R.id.tituloLocais);
            descricaoLocais = itemView.findViewById(R.id.descricaoLocais);
            dataLocais = itemView.findViewById(R.id.dataLocais);
            latlonLocais = itemView.findViewById(R.id.lonlatLocais);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if(itemClickListener != null) {
                itemClickListener.onItemClick(getAdapterPosition(), results.get(getAdapterPosition()));
            }
        }
    }
    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    public interface ItemClickListener {
        void onItemClick(int position,Locais locais);
    }
}

