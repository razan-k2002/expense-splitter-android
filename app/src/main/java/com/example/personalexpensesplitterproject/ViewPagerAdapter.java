package com.example.personalexpensesplitterproject;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private final Group selectedGroup;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, Group selectedGroup) {
        super(fragmentActivity);
        this.selectedGroup = selectedGroup;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return MembersFragment.newInstance(selectedGroup);
            case 1:
                return ExpensesFragment.newInstance(selectedGroup); // implement later
            case 2:
                return SummaryFragment.newInstance(selectedGroup);  // implement later
            default:
                return new Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Members, Expenses, Summary
    }
}
