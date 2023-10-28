package com.example.seg2105project;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView name, email;
    OnRequestListener onRequestListener;

    public ListViewHolder(@NonNull View itemView, OnRequestListener onRequestListener) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        email = itemView.findViewById(R.id.email);
        this.onRequestListener = onRequestListener;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onRequestListener.onRequestClick(getBindingAdapterPosition());
    }

    public interface OnRequestListener{
        void onRequestClick(int position);
    }
}
