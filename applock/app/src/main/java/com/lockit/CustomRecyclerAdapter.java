package com.lockit;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import rx.functions.Func0;

public class CustomRecyclerAdapter<T, V extends View & BaseListItemView<T>> extends
        RecyclerView.Adapter<CustomRecyclerAdapter.CustomViewHolder> {

    private final List<T> items;
    private final Func0<V> viewBuilder;

    public CustomRecyclerAdapter(List<T> items, Func0<V> viewBuilder) {
        this.items = items;
        this.viewBuilder = viewBuilder;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(viewBuilder.call());
    }

    @Override
    public void onBindViewHolder(CustomRecyclerAdapter.CustomViewHolder holder, int position) {
        ((BaseListItemView) holder.getItemView()).bind(items.get(position));
//        ((AppItemView) holder.getItemView()).item.setOnClickListener(v -> bus().post(items.get(position)));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public T getItem(final int position) {
        return items.get(position);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private V itemView;

        public CustomViewHolder(V itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        public V getItemView() {
            return itemView;
        }
    }
}
