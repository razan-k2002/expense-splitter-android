package com.example.personalexpensesplitterproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SummaryFragment extends Fragment {

    static final String ARG_GROUP = "group";
    private Group selectedGroup;

    private TextView totalGroupExpenses, averagePerPerson, settlementMessage;
    private RecyclerView balancesRecyclerView;

    private List<MemberBalance> balances;

    public static SummaryFragment newInstance(Group group) {
        SummaryFragment fragment = new SummaryFragment();
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
        balances = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        totalGroupExpenses = view.findViewById(R.id.totalGroupExpenses);
        averagePerPerson = view.findViewById(R.id.averagePerPerson);
        settlementMessage = view.findViewById(R.id.settlementMessage);

        balancesRecyclerView = view.findViewById(R.id.balancesRecyclerView);
        balancesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        updateSummary();

        return view;
    }

    private void updateSummary() {
        balances.clear();

        if (selectedGroup == null) {
            totalGroupExpenses.setText("Total: $0");
            averagePerPerson.setText("Avg per person: $0");
            settlementMessage.setText("No group selected.");
            balancesRecyclerView.setAdapter(new BalanceAdapter(balances));
            return;
        }

        List<Member> members = selectedGroup.getMembers();
        List<Expense> expenses = selectedGroup.getExpenses();

        // 1) total
        double total = 0;
        for (Expense e : expenses) total += e.getAmount();

        totalGroupExpenses.setText(String.format(Locale.getDefault(), "Total: $%.2f", total));

        // 2) average per person
        double avg = 0;
        if (members != null && !members.isEmpty()) {
            avg = total / members.size();
            averagePerPerson.setText(String.format(Locale.getDefault(), "Avg per person: $%.2f", avg));
        } else {
            averagePerPerson.setText("Avg per person: $0");
        }

        // 3) compute how much each member paid
        Map<String, Double> paidMap = new HashMap<>();
        if (members != null) {
            for (Member m : members) paidMap.put(m.getName(), 0.0);
        }

        for (Expense ex : expenses) {
            String payer = ex.getPaidBy();
            if (payer == null) continue;
            double prev = paidMap.containsKey(payer) ? paidMap.get(payer) : 0.0;
            paidMap.put(payer, prev + ex.getAmount());
        }

        // 4) build balances
        for (Member m : members) {
            double paid = paidMap.containsKey(m.getName()) ? paidMap.get(m.getName()) : 0.0;
            double memberBalance = paid - avg; // positive => gets back
            balances.add(new MemberBalance(m.getName(), m.getIcon(), memberBalance));
        }

        // 5) set adapter
        balancesRecyclerView.setAdapter(new BalanceAdapter(balances));

        // 6) Simple settlement calculation (greedy)
        String settlement = buildSettlementMessage(balances);
        settlementMessage.setText(settlement);
    }

    // greedy algorithm to produce simple settlement suggestions (fewest transactions)
    private String buildSettlementMessage(List<MemberBalance> balanceList) {
        StringBuilder sb = new StringBuilder();
        if (balanceList == null || balanceList.isEmpty()) return "No balances.";

        // copy and sort creditors & debtors
        List<MemberBalance> creditors = new ArrayList<>();
        List<MemberBalance> debtors = new ArrayList<>();
        for (MemberBalance mb : balanceList) {
            if (mb.getBalance() > 0.01) creditors.add(new MemberBalance(mb.getMemberName(), mb.getMemberIconRes(), mb.getBalance()));
            else if (mb.getBalance() < -0.01) debtors.add(new MemberBalance(mb.getMemberName(), mb.getMemberIconRes(), mb.getBalance()));
        }

        Comparator<MemberBalance> credComp = (a, b) -> Double.compare(b.getBalance(), a.getBalance()); // desc
        Comparator<MemberBalance> debtComp = (a, b) -> Double.compare(a.getBalance(), b.getBalance()); // asc (more negative first)
        Collections.sort(creditors, credComp);
        Collections.sort(debtors, debtComp);

        int step = 0;
        while (!creditors.isEmpty() && !debtors.isEmpty() && step < 1000) {
            MemberBalance c = creditors.get(0);
            MemberBalance d = debtors.get(0);

            double amount = Math.min(c.getBalance(), -d.getBalance());
            amount = Math.round(amount * 100.0) / 100.0;

            sb.append(String.format(Locale.getDefault(), "%s pays %s $%.2f\n", d.getMemberName(), c.getMemberName(), amount));

            // update balances
            c.setBalance(c.getBalance() - amount);
            d.setBalance(d.getBalance() + amount);

            // remove near-zero entries
            if (Math.abs(c.getBalance()) < 0.01) creditors.remove(0);
            if (Math.abs(d.getBalance()) < 0.01) debtors.remove(0);

            // re-sort (simple way)
            Collections.sort(creditors, credComp);
            Collections.sort(debtors, debtComp);
            step++;
        }

        if (sb.length() == 0) return "All settled — no one owes anything.";
        return sb.toString().trim();
    }
}
