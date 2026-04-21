package com.example.personalexpensesplitterproject;

import java.io.Serializable;

public class MemberBalance implements Serializable {
    private String memberName;
    private int memberIconRes;
    private double balance;

    public MemberBalance(String memberName, int memberIconRes, double balance) {
        this.memberName = memberName;
        this.memberIconRes = memberIconRes;
        this.balance = balance;
    }

    public String getMemberName() { return memberName; }
    public int getMemberIconRes() { return memberIconRes; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}
