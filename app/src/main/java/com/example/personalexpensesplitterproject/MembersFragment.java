package com.example.personalexpensesplitterproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MembersFragment extends Fragment {

    static final String ARG_GROUP = "group";
    private Group selectedGroup;
    private List<Member> members;
    private MemberAdapter memberAdapter; // ✅ Make it global so we can use it anywhere

    public static MembersFragment newInstance(Group group) {
        MembersFragment fragment = new MembersFragment();
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
            if (selectedGroup != null) {
                members = selectedGroup.getMembers();
            } else {
                members = new ArrayList<>();
            }
        } else {
            members = new ArrayList<>();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_members, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.membersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // ✅ Initialize adapter and set it
        memberAdapter = new MemberAdapter(members);
        recyclerView.setAdapter(memberAdapter);

        // ✅ Handle add member button
        Button btnAddMember = view.findViewById(R.id.btnAddMember);
        btnAddMember.setOnClickListener(v -> {
            MemberUtils.showAddMemberBottomSheet(requireContext(), members, memberAdapter);
        });

        return view;
    }
}
