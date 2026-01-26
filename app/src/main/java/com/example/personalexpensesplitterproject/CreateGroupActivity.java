package com.example.personalexpensesplitterproject;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupActivity extends AppCompatActivity {
    final int defaultBg = R.drawable.circle_background;
    final int selectedBg = R.drawable.blue_stroke;
    final ImageView [] selectedIcon = {null};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.grey_light));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        EditText groupNameInput = findViewById(R.id.groupNameInput);
        Button saveButton = findViewById(R.id.saveButton);


        ImageView [] icons = {
                findViewById(R.id.icon1),
                findViewById(R.id.icon2),
                findViewById(R.id.icon3),
                findViewById(R.id.icon4),
                findViewById(R.id.icon5),
                findViewById(R.id.icon6),
                findViewById(R.id.icon7),
                findViewById(R.id.icon8),
                findViewById(R.id.icon9)
        };
        icons[0].setTag(R.drawable.food);
        icons[1].setTag(R.drawable.beach);
        icons[2].setTag(R.drawable.camping);
        icons[3].setTag(R.drawable.hiking);
        icons[4].setTag(R.drawable.movie);
        icons[5].setTag(R.drawable.picnic);
        icons[6].setTag(R.drawable.sea);
        icons[7].setTag(R.drawable.ski);
        icons[8].setTag(R.drawable.travel);

        for (ImageView icon : icons) {
            icon.setOnClickListener(v -> {
                if(selectedIcon[0] == icon){
                    icon.setBackgroundResource(defaultBg);
                    selectedIcon[0]= null;
                }
                else {
                    if (selectedIcon[0]!=null) {
                        selectedIcon[0].setBackgroundResource(defaultBg);
                    }
                    icon.setBackgroundResource(selectedBg);
                    selectedIcon[0] = icon;
                }
            });
        }
        findViewById(R.id.topAppBar).setOnClickListener(v -> finish());
        List<Member> members = new ArrayList<>();
        RecyclerView membersRecycler = findViewById(R.id.membersRecyclerView);
        MemberAdapter memberAdapter = new MemberAdapter(members);
        membersRecycler.setLayoutManager(new LinearLayoutManager(this));
        membersRecycler.setAdapter(memberAdapter);
        TextView addMembers = findViewById(R.id.addMembers);
        addMembers.setOnClickListener(v -> {
            MemberUtils.showAddMemberBottomSheet(this, members, memberAdapter);
        });

        saveButton.setOnClickListener(v -> {
            String groupName = groupNameInput.getText().toString().trim();
            if (!groupName.isEmpty()) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("groupName", groupName);
                resultIntent.putExtra("groupIcon",
                        selectedIcon[0] != null ? (Integer) selectedIcon[0].getTag() : R.drawable.ic_group);
                resultIntent.putExtra("members", new ArrayList<>(members)); // ✅ right place

                setResult(RESULT_OK, resultIntent);  // send data back
                finish();
            } else {
                groupNameInput.setError("Group name cannot be empty");
            }
        });

    }
    }
