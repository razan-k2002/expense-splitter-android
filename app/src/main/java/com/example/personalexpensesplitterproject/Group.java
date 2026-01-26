package com.example.personalexpensesplitterproject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Group implements Serializable {
    private String name;
    private double balance;
    private int iconResId;
    private String status;
    private List<Member> members;
    private List<Expense> expenses;

    // Main constructor
    public Group(String name, double balance, int iconResId, String status, List<Member> members) {
        this.name = name;
        this.balance = balance;
        this.iconResId = iconResId;
        this.status = status;
        this.members = members != null ? members : new ArrayList<>();
        this.expenses = new ArrayList<>();
    }

    // Returns dynamic balance description
    public String getBalanceDescription() {
        if (balance > 0) return "You lent $" + String.format("%.2f", balance);
        if (balance < 0) return "You owe $" + String.format("%.2f", Math.abs(balance));
        return "All settled up";
    }

    // Convenience constructors
    public Group(String name, int iconResId) {
        this(name, 0.0, iconResId, "owes", new ArrayList<>());
    }

    public Group(String name, int iconResId, List<Member> members) {
        this(name, 0.0, iconResId, "owes", members);
    }

    // Expenses helper
    public void addExpense(Expense e) {
        if (expenses == null) expenses = new ArrayList<>();
        expenses.add(e);
    }

    // Getters & setters
    public String getName() { return name; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public int getIconResId() { return iconResId; }
    public void setIconResId(int iconResId) { this.iconResId = iconResId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<Member> getMembers() { return members; }
    public void setMembers(List<Member> members) { this.members = members; }
    public List<Expense> getExpenses() {
        if (expenses == null) expenses = new ArrayList<>();
        return expenses;
    }
    public void setExpenses(List<Expense> expenses) { this.expenses = expenses; }
}
