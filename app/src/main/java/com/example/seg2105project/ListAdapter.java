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

    public ListAdapter(Context context, List<User> requests) {
        this.context = context;
        this.requests = requests;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false));
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
