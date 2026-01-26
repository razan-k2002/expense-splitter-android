package com.example.personalexpensesplitterproject;

public class Expense {
    private String title;
    private String paidBy;
    private double amount;
    private String date;
    private int iconResId;

    public Expense(String title, String paidBy, double amount, String date, int iconResId) {
        this.title = title;
        this.paidBy = paidBy;
        this.amount = amount;
        this.date = date;
        this.iconResId = iconResId;
    }

    // Getters
    public String getTitle() { return title; }
    public String getPaidBy() { return paidBy; }
    public double getAmount() { return amount; }
    public String getDate() { return date; }
    public int getIconResId() { return iconResId; }
}
