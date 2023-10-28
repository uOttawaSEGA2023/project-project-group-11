package com.example.seg2105project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {

    Context context;
    List<User> requests;
    private ListViewHolder.OnRequestListener myOnRequestListener;

    public ListAdapter(Context context, List<User> requests, ListViewHolder.OnRequestListener onRequestListener) {
        this.context = context;
        this.requests = requests;
        this.myOnRequestListener = onRequestListener;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false), myOnRequestListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.name.setText(requests.get(position).getFirstName() + " " + requests.get(position).getLastName());
        holder.email.setText(requests.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }
}
