package com.example.personalexpensesplitterproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {
    private List<Member> memberList;
    public MemberAdapter(List<Member> memberList) {
        this.memberList = memberList;
    }
    @Override
    public MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_members, parent, false);
        return new MemberViewHolder(view);
    }
@Override
    public void onBindViewHolder(MemberViewHolder holder, int position) {
        Member member = memberList.get(position);
        holder.memberName.setText(member.getName());
        holder.memberIcon.setImageResource(member.getIcon());
    }
@Override
    public int getItemCount() {
        return memberList.size();
    }
class MemberViewHolder extends RecyclerView.ViewHolder {
        private final ImageView memberIcon;
    private final TextView memberName;
    public MemberViewHolder(@NonNull View itemView) {
        super(itemView);
        memberIcon = itemView.findViewById(R.id.memberIcon);
        memberName = itemView.findViewById(R.id.memberName);
    }
}
}
