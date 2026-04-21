package com.example.personalexpensesplitterproject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

public class GroupViewModel extends ViewModel {

    private final MutableLiveData<List<Expense>> expenses =
            new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Expense>> getExpenses() {
        return expenses;
    }

    public void addExpense(Expense expense) {
        List<Expense> list = expenses.getValue();
        if (list == null) list = new ArrayList<>();
        list.add(expense);
        expenses.setValue(list);
    }

    private final MutableLiveData<List<Member>> members =
            new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Member>> getMembers() {
        return members;
    }

    public void addMember(Member member) {
        List<Member> list = members.getValue();
        if (list == null) list = new ArrayList<>();
        list.add(member);
        members.setValue(list);
    }

    public double getTotalExpenses() {
        double total = 0;
        List<Expense> list = expenses.getValue();
        if (list != null) {
            for (Expense e : list) total += e.getAmount();
        }
        return total;
    }
}