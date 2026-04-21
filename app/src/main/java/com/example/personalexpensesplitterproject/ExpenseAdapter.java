package com.example.personalexpensesplitterproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenseList;

    public ExpenseAdapter(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);

        holder.expenseTitle.setText(expense.getTitle());
        holder.expenseDetails.setText(
                expense.getPaidBy() + " • " + expense.getDate()
        );
        // Inside onBindViewHolder
        holder.expenseAmount.setText(String.format(Locale.US, "$%.2f", expense.getAmount()));
        holder.expenseIcon.setImageResource(expense.getIconRes());
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        ImageView expenseIcon;
        TextView expenseTitle, expenseDetails, expenseAmount;

        ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseIcon = itemView.findViewById(R.id.expenseIcon);
            expenseTitle = itemView.findViewById(R.id.expenseTitle);
            expenseDetails = itemView.findViewById(R.id.expenseDetails);
            expenseAmount = itemView.findViewById(R.id.expenseAmount);
        }
    }
}