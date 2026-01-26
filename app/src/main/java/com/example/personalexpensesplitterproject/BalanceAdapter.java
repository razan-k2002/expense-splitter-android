package com.example.personalexpensesplitterproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class BalanceAdapter extends RecyclerView.Adapter<BalanceAdapter.BalanceViewHolder> {

    private final List<MemberBalance> balances;

    public BalanceAdapter(List<MemberBalance> balances) {
        this.balances = balances;
    }

    @NonNull
    @Override
    public BalanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_balance, parent, false);
        return new BalanceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BalanceViewHolder holder, int position) {
        MemberBalance mb = balances.get(position);
        holder.name.setText(mb.getMemberName());
        holder.icon.setImageResource(mb.getMemberIconRes());

        double bal = mb.getBalance();
        String amt = String.format(Locale.getDefault(), "%.2f", Math.abs(bal));
        if (bal >= 0.0) {
            holder.balance.setText("Gets back $" + amt);
            holder.balance.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_green_dark));
        } else {
            holder.balance.setText("Owes $" + amt);
            holder.balance.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_red_dark));
        }
    }

    @Override
    public int getItemCount() {
        return balances.size();
    }

    static class BalanceViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;
        TextView balance;

        public BalanceViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.memberIcon);
            name = itemView.findViewById(R.id.memberName);
            balance = itemView.findViewById(R.id.memberBalance);
        }
    }
}
