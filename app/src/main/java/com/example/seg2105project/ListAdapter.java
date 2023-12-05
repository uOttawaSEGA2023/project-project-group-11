package com.example.seg2105project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * This class represents a custom adapter for the list of registration requests.
 */
public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {

    Context context;
    List<User> requests;
    private ListViewHolder.OnRequestListener myOnRequestListener;

    public ListAdapter(Context context, List<User> requests, ListViewHolder.OnRequestListener onRequestListener) {
        this.context = context;
        this.requests = requests;
        this.myOnRequestListener = onRequestListener;
    }

    /**
     * When the RecyclerView needs to make a new view holder to represent an item
     *
     * @param parent The parent ViewGroup that the new view will be attached to.
     * @param viewType The type of view to be created (used in case the RecyclerView
     *                 has multiple view types).
     * @return A new instance of ListViewHolder that holds the view for an item in the RecyclerView.
     */
    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false), myOnRequestListener);
    }

    /**
     * Binds data to the views within the ListViewHolder for a specific item.
     *
     * @param holder The ListViewHolder for the item to be bound.
     * @param position The position of the item within the data source (requests list).
     */
    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.name.setText(requests.get(position).getFirstName() + " " + requests.get(position).getLastName());
        holder.email.setText(requests.get(position).getEmail());
    }

    /**
     * Gets the item count
     *
     * @return number of items in requests
     */
    @Override
    public int getItemCount() {
        return requests.size();
    }
}
