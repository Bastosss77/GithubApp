package org.jazzilla.contentsquareapp.repository.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.List;

public class RepositoryTopicsAdapter extends RecyclerView.Adapter<RepositoryTopicsAdapter.TopicsViewHolder> {
    private List<String> mTopics;

    public RepositoryTopicsAdapter(@NonNull List<String> topicsList) {
        mTopics = topicsList;
    }

    @NonNull
    @Override
    public TopicsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Chip chip = new Chip(parent.getContext());

        return new TopicsViewHolder(chip);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicsViewHolder holder, int position) {
        holder.bind(mTopics.get(position));
    }

    @Override
    public int getItemCount() {
        return mTopics.size();
    }

    final static class TopicsViewHolder extends RecyclerView.ViewHolder {


        public TopicsViewHolder(Chip chip) {
            super(chip);
        }

        public void bind(String topic) {
            ((Chip) itemView).setText(topic);
        }
    }
}
