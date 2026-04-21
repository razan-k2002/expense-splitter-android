package com.example.personalexpensesplitterproject;

import android.graphics.Color;
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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SummaryFragment extends Fragment {

    static final String ARG_GROUP = "group";
    private Group selectedGroup;

    private TextView totalGroupExpenses, averagePerPerson, settlementMessage;
    private RecyclerView balancesRecyclerView;
    private PieChart contributionChart;

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
        contributionChart = view.findViewById(R.id.contributionChart);
        balancesRecyclerView = view.findViewById(R.id.balancesRecyclerView);

        balancesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        balancesRecyclerView.setNestedScrollingEnabled(false);
        setupChartConfig();

        return view;
    }

    private void setupChartConfig() {
        contributionChart.setUsePercentValues(true);
        contributionChart.getDescription().setEnabled(false);
        contributionChart.setExtraOffsets(5, 10, 5, 5);
        contributionChart.setDragDecelerationFrictionCoef(0.95f);
        contributionChart.setDrawHoleEnabled(true);
        contributionChart.setHoleColor(Color.WHITE);
        contributionChart.setTransparentCircleRadius(61f);
        contributionChart.setCenterText("Contributions");
        contributionChart.setCenterTextSize(18f);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateSummary();
    }

    public void updateSummary() {
        if (balances == null) balances = new ArrayList<>();
        balances.clear();

        List<Expense> expenses = new ArrayList<>();
        if (getActivity() instanceof GroupDetailsActivity) {
            expenses = ((GroupDetailsActivity) getActivity()).getAllExpenses();
        }

        if (selectedGroup == null) return;

        List<Member> members = selectedGroup.getMembers();

        double total = 0;
        for (Expense e : expenses) total += e.getAmount();

        totalGroupExpenses.setText(String.format(Locale.US, "Total: $%.2f", total));
        double avg = (members == null || members.isEmpty()) ? 0 : total / members.size();
        averagePerPerson.setText(String.format(Locale.US, "Avg per person: $%.2f", avg));

        Map<String, Double> paidMap = new HashMap<>();
        if (members != null) {
            for (Member m : members) paidMap.put(m.getName(), 0.0);
        }

        for (Expense ex : expenses) {
            String payer = ex.getPaidBy();
            if (payer == null) continue;
            paidMap.put(payer, paidMap.getOrDefault(payer, 0.0) + ex.getAmount());
        }

        updatePieChart(paidMap);

        if (members != null) {
            for (Member m : members) {
                double paid = paidMap.getOrDefault(m.getName(), 0.0);
                double memberBalance = paid - avg;
                balances.add(new MemberBalance(m.getName(), m.getIcon(), memberBalance));
            }
        }

        balancesRecyclerView.setAdapter(new BalanceAdapter(balances));
        settlementMessage.setText(buildSettlementMessage(balances));
    }

    private void updatePieChart(Map<String, Double> paidMap) {
        List<PieEntry> entries = new ArrayList<>();        for (Map.Entry<String, Double> entry : paidMap.entrySet()) {
            if (entry.getValue() > 0) {
                entries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
            }
        }

        if (entries.isEmpty()) {
            contributionChart.clear();
            contributionChart.setNoDataText("No expenses recorded yet");
            return;
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.MATERIAL_COLORS) colors.add(c);
        for (int c : ColorTemplate.VORDIPLOM_COLORS) colors.add(c);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(12f);
        contributionChart.setData(data);
        contributionChart.highlightValues(null);
        contributionChart.invalidate();
        contributionChart.animateY(1000);
    }

    private String buildSettlementMessage(List<MemberBalance> balanceList) {
        if (balanceList == null || balanceList.isEmpty()) return "No balances.";
        List<MemberBalance> creditors = new ArrayList<>();
        List<MemberBalance> debtors = new ArrayList<>();
        for (MemberBalance mb : balanceList) {
            if (mb.getBalance() > 0.01) creditors.add(new MemberBalance(mb.getMemberName(), mb.getMemberIconRes(), mb.getBalance()));
            else if (mb.getBalance() < -0.01) debtors.add(new MemberBalance(mb.getMemberName(), mb.getMemberIconRes(), mb.getBalance()));
        }
        StringBuilder sb = new StringBuilder();
        while (!creditors.isEmpty() && !debtors.isEmpty()) {
            MemberBalance c = creditors.get(0);
            MemberBalance d = debtors.get(0);
            double amount = Math.min(c.getBalance(), Math.abs(d.getBalance()));
            sb.append(String.format(Locale.US, "%s pays %s $%.2f\n", d.getMemberName(), c.getMemberName(), amount));
            c.setBalance(c.getBalance() - amount);
            d.setBalance(d.getBalance() + amount);
            if (c.getBalance() < 0.01) creditors.remove(0);
            if (Math.abs(d.getBalance()) < 0.01) debtors.remove(0);
        }
        return sb.length() == 0 ? "All settled!" : sb.toString().trim();
    }
}