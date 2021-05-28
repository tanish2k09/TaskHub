package com.tanish2k09.taskhub.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tanish2k09.taskhub.R;
import com.tanish2k09.taskhub.activities.newNoteActivity;

public class dialogFragment extends DialogFragment {

    private String title = "", fullText = "";
    private int id = -1;

    public void setArgs(int id, String title, String fullText)
    {
        this.title = title;
        this.fullText = fullText;
        this.id = id;
    }

    public dialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dialog, container, false);
        TextView titleView = v.findViewById(R.id.reading_title), textView = v.findViewById(R.id.reading_text);

        titleView.setText(title);
        textView.setText(fullText);

        FrameLayout openEdit = v.findViewById(R.id.openEditing);
        openEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),newNoteActivity.class);
                i.putExtra("provision",true);
                i.putExtra("id",id);
                i.putExtra("title",title);
                i.putExtra("text",fullText);
                startActivity(i);
            }
        });

        return v;
    }
}
