package com.lockit;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;
import java.util.concurrent.Callable;

public class CustomRecyclerAdapter<T, V extends View & BaseListItemView<T>> extends
        RecyclerView.Adapter<CustomRecyclerAdapter<T, V>.CustomViewHolder> {

    private final List<T> items;
    private final Callable<V> viewBuilder;
    private final Function<T, Void> clickListener;

    public CustomRecyclerAdapter(List<T> items, Callable<V> viewBuilder, Function<T, Void> clickListener) {
        this.items = items;
        this.viewBuilder = viewBuilder;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try {
            return new CustomViewHolder(viewBuilder.call());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onBindViewHolder(CustomRecyclerAdapter<T, V>.CustomViewHolder holder, int position) {
        V item = holder.getItemView();
        T t = items.get(position);
        item.bind(t);
        item.setOnClickListener(__ -> clickListener.apply(t));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public T getItem(final int position) {
        return items.get(position);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private final V itemView;

        public CustomViewHolder(V itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        public V getItemView() {
            return itemView;
        }
    }
}
