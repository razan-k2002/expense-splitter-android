package com.example.personalexpensesplitterproject;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Group> groups; // class-level
    private GroupAdapter adapter; // class-level

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextInputLayout searchLayout = findViewById(R.id.searchGroupsLayout);
        TextInputEditText searchText = findViewById(R.id.searchGroups);

        // --- All your search text styling code is fine ---
        searchText.setTextColor(Color.BLACK);
        searchText.setHintTextColor(Color.GRAY);
        searchLayout.setHintTextColor(ColorStateList.valueOf(Color.GRAY));
        searchLayout.setBoxStrokeColor(Color.LTGRAY);
        searchLayout.setBoxStrokeWidth(1);
        searchLayout.setBoxBackgroundColor(Color.WHITE);
        searchLayout.setBoxStrokeColorStateList(ColorStateList.valueOf(Color.GRAY));
        searchLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
        searchText.setHighlightColor(ContextCompat.getColor(this, R.color.grey_light));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            searchLayout.setCursorColor(ColorStateList.valueOf(Color.GRAY));
        }
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.grey_light));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // --- RecyclerView and Adapter Setup ---
        groups = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.groupsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // CORRECT: Initialize the adapter ONLY ONCE and store it in the class-level variable.
        adapter = new GroupAdapter(groups, this); // 'groups' is already a List, no need to cast.
        recyclerView.setAdapter(adapter);

        // --- Floating Action Button Setup ---
        FloatingActionButton addGroupBtn = findViewById(R.id.fabAddGroup);
        addGroupBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateGroupActivity.class);
            startActivityForResult(intent, 100); // launch new group screen
        });

        // DO NOT initialize the adapter again here. It's already set.
        // REMOVED: adapter = new GroupAdapter((List<Group>) groups, (Context) this);
        // REMOVED: recyclerView.setAdapter(adapter);

        // --- Search Functionality Setup ---
        // Now, this TextWatcher will correctly reference the one and only adapter instance.
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // It's good practice to check if the adapter is null before using it.
                if (adapter != null) {
                    adapter.filter(s.toString());
                }
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            String groupName = data.getStringExtra("groupName");
            int groupIcon = data.getIntExtra("groupIcon", R.drawable.ic_group);

            // Get members list from CreateGroupActivity
            ArrayList<Member> members = (ArrayList<Member>) data.getSerializableExtra("members");

            if (members == null) {
                members = new ArrayList<>();
            }

            // Create new group with correct arguments
            Group newGroup = new Group(
                    groupName,          // name
                    0.0,                // initial balance
                    groupIcon,          // icon resource
                    "owes",             // status
                    members             // members list
            );

            groups.add(newGroup);
            adapter.notifyItemInserted(groups.size() - 1);
            adapter.refreshFullList();

        }
    }



}
