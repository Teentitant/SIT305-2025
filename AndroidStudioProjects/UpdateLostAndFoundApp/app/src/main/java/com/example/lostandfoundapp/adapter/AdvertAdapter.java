package com.example.lostandfoundapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lostandfoundapp.databinding.ItemAdvertBinding;
import com.example.lostandfoundapp.model.Advert;

public class AdvertAdapter extends ListAdapter<Advert, AdvertAdapter.AdvertViewHolder> {

    private OnItemClickListener listener;

    public AdvertAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Advert> DIFF_CALLBACK = new DiffUtil.ItemCallback<Advert>() {
        @Override
        public boolean areItemsTheSame(@NonNull Advert oldItem, @NonNull Advert newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Advert oldItem, @NonNull Advert newItem) {
            return oldItem.postType.equals(newItem.postType) &&
                    oldItem.name.equals(newItem.name) &&
                    oldItem.location.equals(newItem.location) &&
                    oldItem.date.equals(newItem.date);
        }
    };

    @NonNull
    @Override
    public AdvertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAdvertBinding binding = ItemAdvertBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AdvertViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdvertViewHolder holder, int position) {
        Advert currentAdvert = getItem(position);
        holder.bind(currentAdvert);
    }

    class AdvertViewHolder extends RecyclerView.ViewHolder {
        private ItemAdvertBinding binding;

        public AdvertViewHolder(ItemAdvertBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                int position = getBindingAdapterPosition(); // Use getBindingAdapterPosition
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });
        }

        public void bind(Advert advert) {
            binding.textViewItemName.setText(advert.postType + ": " + advert.name);
            binding.textViewItemDetails.setText("Location: " + advert.location + " on " + advert.date);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Advert advert);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}