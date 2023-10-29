package com.example.seg2105project;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Custom RecyclerView ViewHolder for displaying the users information.
 * This class contains references to the views within items in the RecyclerView and
 * provides callback for click events.
 */
public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView name, email;
    OnRequestListener onRequestListener;

    /**
     * ListViewHolder Constructor
     *
     * @param itemView          Item in the RecyclerView.
     * @param onRequestListener For handling item click events.
     */
    public ListViewHolder(@NonNull View itemView, OnRequestListener onRequestListener) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        email = itemView.findViewById(R.id.email);
        this.onRequestListener = onRequestListener;

        itemView.setOnClickListener(this);
    }

    /**
     * Called when the item view is clicked. Invokes the onRequestClick callback.
     *
     * @param view The clicked view.
     */
    @Override
    public void onClick(View view) {
        onRequestListener.onRequestClick(getBindingAdapterPosition());
    }

    /**
     * Interface for handling item click events in the RecyclerView.
     */
    public interface OnRequestListener {
        void onRequestClick(int position);
    }
}
