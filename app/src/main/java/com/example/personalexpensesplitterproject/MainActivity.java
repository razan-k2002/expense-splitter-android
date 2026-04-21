package com.example.personalexpensesplitterproject;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Group> groups;
    private GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(
                androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
        );
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.blue));
        EditText searchGroups = findViewById(R.id.searchGroups);
        searchGroups.clearFocus();

        groups = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.groupsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new GroupAdapter(groups, this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton addGroupBtn = findViewById(R.id.fabAddGroup);
        addGroupBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateGroupActivity.class);
            startActivityForResult(intent, 100);
        });

        searchGroups.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {
                    adapter.filter(s.toString());
                }
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && addGroupBtn.isShown()) {
                    addGroupBtn.hide();
                } else if (dy < 0 && !addGroupBtn.isShown()) {
                    addGroupBtn.show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            String groupName = data.getStringExtra("groupName");
            int groupIcon = data.getIntExtra("groupIcon", R.drawable.ic_group);
            ArrayList<Member> members = (ArrayList<Member>) data.getSerializableExtra("members");

            if (members == null) members = new ArrayList<>();

            Group newGroup = new Group(
                    groupName,
                    0.0,
                    groupIcon,
                    "No Expenses",
                    members
            );

            groups.add(newGroup);
            adapter.notifyItemInserted(groups.size() - 1);
            adapter.refreshFullList();
        }
    }
}
