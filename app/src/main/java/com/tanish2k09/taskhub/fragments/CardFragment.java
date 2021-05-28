package com.tanish2k09.taskhub.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.tanish2k09.taskhub.dbHelper.dbHelper;

import com.tanish2k09.taskhub.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardFragment extends Fragment {

    private TextView title, text;
    private String titleText = "", previewText = "", fulltext = "";
    public int id = -1;
    CardView content;
    private onDelete deleteInterface;

    public void setArgs(int id,String titleText, String fullText)
    {
        this.id = id;
        this.titleText = titleText;
        this.fulltext = fullText;
        this.previewText = updatePreviewText();
    }

    private String updatePreviewText()
    {
        if(fulltext.length()>22)
        {
            int cnt;
            StringBuilder sb = new StringBuilder();
            for(cnt = 0; cnt < 22; cnt++)
                sb.append(fulltext.charAt(cnt));
            sb.append("...");
            return sb.toString();
        }
        return fulltext;
    }

    public void update_views()
    {
        title.setText(titleText);
        text.setText(previewText);
    }

    public CardFragment() {
        // Required empty public constructor
    }

    public interface onDelete
    {
        void onDeleteNote();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_card, container, false);
        title = v.findViewById(R.id.title);
        text = v.findViewById(R.id.preview);
        content = v.findViewById(R.id.contentCard);
        update_views();

        ImageButton deleteButton = v.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {

            final dbHelper mDB = new dbHelper(v.getContext());

            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("Do you really want to delete the note?\n");
                alertBuilder.setMessage(titleText);
                alertBuilder.setPositiveButton("Yes", (dialog, which) -> {
                    mDB.removeNote(id);
                    dialog.dismiss();
                    deleteInterface.onDeleteNote();
                });
                alertBuilder.setNeutralButton("Cancel", (dialog, which) -> dialog.cancel());
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
        });

        content.setOnClickListener(new View.OnClickListener() {

            final FragmentManager fm = requireActivity().getSupportFragmentManager();

            @Override
            public void onClick(View v) {
                dialogFragment readingDialog = new dialogFragment();
                readingDialog.setArgs(id,titleText,fulltext);
                readingDialog.show(fm,"Reading Dialog");
            }
        });

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        try {
            this.deleteInterface = (onDelete) context;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onDelete");
        }
    }
}
