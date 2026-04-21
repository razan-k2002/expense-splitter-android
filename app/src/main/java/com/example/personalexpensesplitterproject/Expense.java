package com.example.personalexpensesplitterproject;
public class Expense {

    private String title;
    private String paidBy;
    private double amount;
    private String date;
    private int iconRes;

    public Expense(String title, String paidBy, double amount, String date, int iconRes) {
        this.title = title;
        this.paidBy = paidBy;
        this.amount = amount;
        this.date = date;
        this.iconRes = iconRes;
    }

    public String getTitle() { return title; }
    public String getPaidBy() { return paidBy; }
    public double getAmount() { return amount; }
    public String getDate() { return date; }
    public int getIconRes() { return iconRes; }
}