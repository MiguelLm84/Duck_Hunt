package com.miguel_lm.duckhunt.ui.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import com.miguel_lm.duckhunt.databinding.FragmentUserRankingBinding;
import com.miguel_lm.duckhunt.model.User;
import java.util.List;


public class MyUserRecyclerViewAdapter extends RecyclerView.Adapter<MyUserRecyclerViewAdapter.ViewHolder> {

    private final List<User> mValues;

    public MyUserRecyclerViewAdapter(List<User> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentUserRankingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        int pos = position + 1;
        holder.tv_position.setText(pos + ".");
        holder.tv_ducks.setText(String.valueOf(mValues.get(position).getDucks()));
        holder.tv_nickname.setText(mValues.get(position).getNick());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView tv_position;
        public final TextView tv_ducks;
        public final TextView tv_nickname;
        public User mItem;

        public ViewHolder(FragmentUserRankingBinding binding) {
            super(binding.getRoot());
            tv_position = binding.tvPosition;
            tv_ducks = binding.tvDucks;
            tv_nickname = binding.tvNickname;
            tv_nickname.setSelected(true);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + tv_nickname.getText() + "'";
        }
    }
}