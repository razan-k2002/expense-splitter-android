package com.example.personalexpensesplitterproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
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
    private RecyclerView recyclerView;

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

        recyclerView = view.findViewById(R.id.expensesRecyclerView);
        FloatingActionButton fabAdd = view.findViewById(R.id.fabAddExpense);
        totalExpenses = view.findViewById(R.id.totalExpenses);
        expenseCount = view.findViewById(R.id.expenseCount);

        expenseList = new ArrayList<>();
        adapter = new ExpenseAdapter(expenseList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> showAddExpenseSheet());

        updateSummary();
        return view;
    }

    private void showAddExpenseSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        View sheetView = getLayoutInflater().inflate(R.layout.bs_add_expense, null);
        bottomSheetDialog.setContentView(sheetView);

        EditText editTitle = sheetView.findViewById(R.id.editExpenseTitle);
        EditText editAmount = sheetView.findViewById(R.id.editExpenseAmount);

        AutoCompleteTextView editPaidBy = sheetView.findViewById(R.id.editExpensePaidBy);

        Button btnAddExpense = sheetView.findViewById(R.id.btnAddExpense);
        Button btnCancelExpense = sheetView.findViewById(R.id.btnCancelExpense);

        if (selectedGroup != null && selectedGroup.getMembers() != null) {
            List<String> memberNames = new ArrayList<>();
            for (Member m : selectedGroup.getMembers()) {
                memberNames.add(m.getName());
            }

            ArrayAdapter<String> memberAdapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    memberNames
            );
            editPaidBy.setAdapter(memberAdapter);

            editPaidBy.setOnClickListener(v -> {
                ((AutoCompleteTextView) v).showDropDown();
            });
            editPaidBy.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) editPaidBy.showDropDown();
            });
        }

        btnAddExpense.setOnClickListener(v2 -> {
            String title = editTitle.getText().toString().trim();
            String amountStr = editAmount.getText().toString().trim();
            String paidBy = editPaidBy.getText().toString().trim();

            if (title.isEmpty() || amountStr.isEmpty() || paidBy.isEmpty()) {
                Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean memberExists = false;
            for (Member m : selectedGroup.getMembers()) {
                if (m.getName().equalsIgnoreCase(paidBy)) {
                    memberExists = true;
                    paidBy = m.getName();
                    break;
                }
            }

            if (!memberExists) {
                editPaidBy.setError("Select a valid member from this group");
                return;
            }

            try {
                double amount = Double.parseDouble(amountStr);

                Expense expense = new Expense(
                        title,
                        paidBy,
                        amount,
                        new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date()),
                        R.drawable.food
                );

                expenseList.add(expense);
                adapter.notifyItemInserted(expenseList.size() - 1);
                if (getActivity() instanceof GroupDetailsActivity) {
                    ((GroupDetailsActivity) getActivity()).addExpenseToSharedList(expense);
                }
                recyclerView.scrollToPosition(expenseList.size() - 1);

                updateSummary();
                bottomSheetDialog.dismiss();

            } catch (NumberFormatException e) {
                editAmount.setError("Invalid amount");
            }
        });

        btnCancelExpense.setOnClickListener(v2 -> bottomSheetDialog.dismiss());
        bottomSheetDialog.show();
    }

    private void updateSummary() {
        double total = 0;
        for (Expense e : expenseList) {
            total += e.getAmount();
        }
        totalExpenses.setText("Total: $" + String.format(Locale.US, "%.2f", total));
        expenseCount.setText(expenseList.size() + (expenseList.size() == 1 ? " expense" : " expenses"));
    }
}