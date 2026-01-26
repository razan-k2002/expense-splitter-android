package com.example.personalexpensesplitterproject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private List<Group> groups;
    private List<Group> fullList;  // full copy of all groups
    private Context context;

    public GroupAdapter(List<Group> groups, Context context) {
        this.groups = groups;
        this.context = context;
        this.fullList = new ArrayList<>(groups); // initial full copy
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_group, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Group group = groups.get(position);
        holder.groupName.setText(group.getName());
        holder.groupBalance.setText(group.getBalanceDescription());
        holder.groupIcon.setImageResource(group.getIconResId());

        holder.arrowForward.setOnClickListener(v -> {
            Intent intent = new Intent(context, GroupDetailsActivity.class);
            intent.putExtra("groupName", group.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    // ✅ FIXED FILTER METHOD
    public void filter(String text) {
        groups.clear();
        if (text == null || text.trim().isEmpty()) {
            groups.addAll(fullList);
        } else {
            String searchText = text.toLowerCase().trim();
            for (Group g : fullList) {
                if (g.getName().toLowerCase().contains(searchText)) {
                    groups.add(g);
                }
            }
        }
        notifyDataSetChanged();
    }

    // ✅ Keep fullList updated whenever a new group is added
    public void refreshFullList() {
        fullList.clear();
        fullList.addAll(groups);
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView groupName, groupBalance;
        ImageView groupIcon;
        ImageView arrowForward;
        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.groupName);
            groupBalance = itemView.findViewById(R.id.groupBalance);
            groupIcon = itemView.findViewById(R.id.groupIcon);
            arrowForward = itemView.findViewById(R.id.arrowForward);
        }
    }
}
