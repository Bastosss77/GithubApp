package org.jazzilla.contentsquareapp.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jazzilla.contentsquareapp.R;
import org.jazzilla.contentsquareapp.core.appmodel.RepositoryAppModel;

import java.util.ArrayList;
import java.util.List;

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder> {
    private List<RepositoryAppModel> mRepositoriesList = new ArrayList<>();
    private OnItemClickedListener mItemClickedListener;

    @NonNull
    @Override
    public RepositoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_list_item, parent, false);

        return new RepositoryViewHolder(view, mItemClickedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RepositoryViewHolder holder, int position) {
        RepositoryAppModel repository = mRepositoriesList.get(position);

        holder.bind(repository);
    }

    @Override
    public int getItemCount() {
        return mRepositoriesList.size();
    }

    public void addItems(@NonNull List<RepositoryAppModel> repositories) {
        mRepositoriesList.addAll(repositories);
        notifyDataSetChanged();
    }

    public void setItems(@NonNull List<RepositoryAppModel> repositories) {
        mRepositoriesList = repositories;
        notifyDataSetChanged();
    }

    public void setOnItemClickedListener(OnItemClickedListener listener) {
        mItemClickedListener = listener;
    }

    final class RepositoryViewHolder extends RecyclerView.ViewHolder {
        private TextView mRepoNameTextView;
        private TextView mOwnerNameTextView;
        private ImageView mOwnerImageView;

        private OnItemClickedListener mListener;

        RepositoryViewHolder(View v, OnItemClickedListener listener) {
            super(v);
            mListener = listener;
            mRepoNameTextView = v.findViewById(R.id.repoNameTextView);
            mOwnerNameTextView = v.findViewById(R.id.ownerNameTextView);
            mOwnerImageView = v.findViewById(R.id.ownerImageView);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null) {
                        RepositoryAppModel repository = mRepositoriesList.get(getAdapterPosition());
                        mListener.onItemClicked(repository);
                    }
                }
            });
        }

        void bind(RepositoryAppModel repository) {
            mRepoNameTextView.setText(repository.name);
            mOwnerNameTextView.setText(repository.owner.name);

            Picasso.get()
                    .load(repository.owner.avatarUri)
                    .placeholder(R.drawable.ic_person)
                    .fit()
                    .into(mOwnerImageView);
        }
    }

    interface OnItemClickedListener {
        void onItemClicked(RepositoryAppModel repository);
    }
}
