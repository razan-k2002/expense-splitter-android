package com.example.personalexpensesplitterproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExpensesFragment extends Fragment {

    static final String ARG_GROUP = "group";
    private Group selectedGroup;

    private List<Expense> expenseList;
    private ExpenseAdapter adapter;
    private TextView totalExpenses, expenseCount;

    // ✅ Correct factory method
    public static ExpensesFragment newInstance(Group group) {
        ExpensesFragment fragment = new ExpensesFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_GROUP, group);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedGroup = (Group) getArguments().getSerializable(ARG_GROUP);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.expensesRecyclerView);
        FloatingActionButton fabAdd = view.findViewById(R.id.fabAddExpense);
        totalExpenses = view.findViewById(R.id.totalExpenses);
        expenseCount = view.findViewById(R.id.expenseCount);

        expenseList = new ArrayList<>();
        adapter = new ExpenseAdapter(expenseList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
            View sheetView = getLayoutInflater().inflate(R.layout.bs_add_expense, null);
            bottomSheetDialog.setContentView(sheetView);

            EditText editTitle = sheetView.findViewById(R.id.editExpenseTitle);
            EditText editAmount = sheetView.findViewById(R.id.editExpenseAmount);
            EditText editPaidBy = sheetView.findViewById(R.id.editExpensePaidBy);
            Button btnAddExpense = sheetView.findViewById(R.id.btnAddExpense);
            Button btnCancelExpense = sheetView.findViewById(R.id.btnCancelExpense);

            btnAddExpense.setOnClickListener(v2 -> {
                String title = editTitle.getText().toString().trim();
                String amountStr = editAmount.getText().toString().trim();
                String paidBy = editPaidBy.getText().toString().trim();

                if (title.isEmpty()) {
                    editTitle.setError("Enter a title");
                    return;
                }
                if (amountStr.isEmpty()) {
                    editAmount.setError("Enter amount");
                    return;
                }
                if (paidBy.isEmpty()) {
                    editPaidBy.setError("Enter who paid");
                    return;
                }

                double amount = Double.parseDouble(amountStr);

                // Create expense & add it to list
                Expense expense = new Expense(title, paidBy, amount,
                        new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date()),
                        R.drawable.food);

                expenseList.add(expense);
                adapter.notifyItemInserted(expenseList.size() - 1);
                updateSummary();

                bottomSheetDialog.dismiss();
            });

            btnCancelExpense.setOnClickListener(v2 -> bottomSheetDialog.dismiss());

            bottomSheetDialog.show();
        });


        updateSummary();

        return view;
    }


    private void updateSummary() {
        double total = 0;
        for (Expense e : expenseList) {
            total += e.getAmount();
        }
        totalExpenses.setText("Total: $" + total);
        expenseCount.setText(expenseList.size() + " expenses");
    }
}
