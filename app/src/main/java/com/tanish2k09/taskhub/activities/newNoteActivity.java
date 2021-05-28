package com.tanish2k09.taskhub.activities;

import android.content.Intent;
import androidx.constraintlayout.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.materialtextfield.MaterialTextField;
import com.tanish2k09.taskhub.dbHelper.dbHelper;

import com.tanish2k09.taskhub.R;

import java.util.Objects;

public class newNoteActivity extends AppCompatActivity {

    private EditText title_box, note_box;
    private String preview_text;
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        // Required variables in whole scope instead of conditional
        ConstraintLayout parentAF = findViewById(R.id.parent_layout_new_note);
        CardView edit_note_card = findViewById(R.id.edit_note_card);
        TextView preview_view = findViewById(R.id.preview_new);
        TextView preview_title_view = findViewById(R.id.title_new);
        MaterialTextField mtf = findViewById(R.id.materialTextField);
        final CardView saveCard = findViewById(R.id.saveCard);

        title_box = findViewById(R.id.edit_title);
        note_box = findViewById(R.id.edit_note);

        i = getIntent();

        // Change the status bar icons to dark ones
        parentAF.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        final InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);

        // Set click responses
        edit_note_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note_box.requestFocus();
                assert inputMethodManager != null;
                inputMethodManager.showSoftInput(note_box, InputMethodManager.SHOW_IMPLICIT);
                note_box.setSelection(note_box.getText().length());
            }
        });

        saveCard.setOnClickListener(new View.OnClickListener() {
            int id = -1;
            @Override
            public void onClick(View v)
            {
                assert inputMethodManager != null;
                inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                dbHelper mDB = new dbHelper(v.getContext());
                if(id == -1) {
                    id = i.getIntExtra("id", -1);
                }
                // Check whether we were editing or creating new
                if(id == -1) {
                    id = mDB.writeVal(title_box.getText().toString(), note_box.getText().toString());
                    Toast.makeText(v.getContext(),"New note created",Toast.LENGTH_SHORT).show();
                }
                else {
                    mDB.updateVal(id, title_box.getText().toString(), note_box.getText().toString());
                    Toast.makeText(v.getContext(),"Note Updated",Toast.LENGTH_SHORT).show();
                }
                newNoteActivity.super.onBackPressed();
            }
        });

        if(i.getBooleanExtra("provision",false))
        {
            title_box.setText(i.getStringExtra("title"));
            note_box.setText(i.getStringExtra("text"));
            preview_view.setText(updatePreviewText(note_box.getText().toString()));
            preview_title_view.setText(title_box.getText().toString());
            mtf.expand();
        }

        setTextWatchers();
    }

    private void setTextWatchers()
    {
        title_box.addTextChangedListener(new TextWatcher() {

            TextView title_new = findViewById(R.id.title_new);

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                title_new.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        note_box.addTextChangedListener(new TextWatcher() {

            TextView preview_new = findViewById(R.id.preview_new);
            boolean needs_refresh = true;
            boolean append_dot = true;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                needs_refresh = s.length() <= 22;

                if(needs_refresh) {
                    preview_text = s.toString();
                    preview_new.setText(preview_text);
                    append_dot = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // If note is more than 22 chars, then append ... to preview to let user know about more material
                if(!needs_refresh && append_dot) {
                    preview_new.append("...");
                    append_dot = false;
                }
            }
        });
    }

    private String updatePreviewText(String fulltext)
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

    @Override
    public void onBackPressed()
    {
        Toast.makeText(this,"Editing Cancelled",Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }
}
