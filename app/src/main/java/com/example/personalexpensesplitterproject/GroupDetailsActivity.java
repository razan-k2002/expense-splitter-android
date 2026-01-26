package com.example.personalexpensesplitterproject;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class GroupDetailsActivity extends AppCompatActivity {

    private Group selectedGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);

        selectedGroup = (Group) getIntent().getSerializableExtra("group");

        if (selectedGroup == null) {
            finish(); // safety: close if group not passed
            return;
        }

        // Status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.grey_light));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // Top app bar
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setTitle(selectedGroup.getName());
        topAppBar.setNavigationOnClickListener(v -> finish());

        // Tabs and ViewPager
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        // Adapter passes group to fragments
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, selectedGroup);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("Members"); break;
                case 1: tab.setText("Expenses"); break;
                case 2: tab.setText("Summary"); break;
            }
        }).attach();
    }
}
