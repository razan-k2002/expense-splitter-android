package com.example.personalexpensesplitterproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.List;

public class MemberUtils {

    public interface OnMemberAddedListener {
        void onMemberAdded();
    }

    public static void showAddMemberBottomSheet(Context context, List<Member> members, MemberAdapter adapter, OnMemberAddedListener listener) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View sheetView = LayoutInflater.from(context).inflate(R.layout.dialog_add_member, null);
        bottomSheetDialog.setContentView(sheetView);

        EditText editMemberName = sheetView.findViewById(R.id.editMemberName);
        Button btnAddMember = sheetView.findViewById(R.id.btnAddMember);
        ImageButton btnClose = sheetView.findViewById(R.id.btnClose);

        btnAddMember.setOnClickListener(v2 -> {
            String name = editMemberName.getText().toString().trim();
            if (!name.isEmpty()) {
                members.add(new Member(name, R.drawable.ic_person));
                adapter.notifyItemInserted(members.size() - 1);
                if (listener != null) {
                    listener.onMemberAdded();
                }

                bottomSheetDialog.dismiss();
            } else {
                editMemberName.setError("Name cannot be empty");
            }
        });

        btnClose.setOnClickListener(v2 -> bottomSheetDialog.dismiss());
        bottomSheetDialog.show();
    }
}