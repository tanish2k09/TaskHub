package com.tanish2k09.taskhub.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.tanish2k09.taskhub.dbHelper.dbHelper;

import com.tanish2k09.taskhub.R;
import com.tanish2k09.taskhub.fragments.CardFragment;

public class MainActivity extends AppCompatActivity implements CardFragment.onDelete {

    CardView no_note_card;
    ScrollView noteScrollView;
    private int noteCount = 0;
    private dbHelper mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set a dark theme for status bar
        toolbar.setTitleTextColor(getResources().getColor(R.color.textColorCard,getTheme()));
        toolbar.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // Declare the required stuff
        SharedPreferences sp = getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences.Editor sp_editor = sp.edit();
        ImageButton newNote = findViewById(R.id.newNoteButton);
        boolean init = sp.getBoolean("init",false);

        noteScrollView = findViewById(R.id.noteScrollView);
        no_note_card = findViewById(R.id.no_notes_card);


        newNote.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), newNoteActivity.class);
            i.putExtra("id",-1);
            startActivity(i);
        });

        mDb = new dbHelper(this);

        // If this is first run, create a intro card
        if(!init)
        {
            sp_editor.putBoolean("init",true);
            sp_editor.apply();
            mDb.writeInitVal(0,"Intro","Example card");
        }
    }

    private void refreshCards()
    {
        Cursor cursor;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft;

        for (Fragment fragment:fm.getFragments()) {
            if(fragment != null)
                fm.beginTransaction().remove(fragment).commit();
        }

        noteCount = 0;

        // Now we read the database and inflate cards
        int id;
        cursor = mDb.db.rawQuery("select "+dbHelper.ID+" from "+ dbHelper.TABLE_NAME, null);
        if(cursor != null)
        {
            if (cursor.moveToFirst())
            {
                while(!cursor.isAfterLast()) {
                    ft = fm.beginTransaction();
                    CardFragment card = new CardFragment();
                    id = cursor.getInt(cursor.getColumnIndex(dbHelper.ID));
                    if(id >= 0)
                    {
                        card.setArgs(id, mDb.getTitleFromID(id), mDb.getTextFromID(id));
                        ft.add(R.id.notes_holder_ll, card);
                        ft.commit();
                        noteCount++;
                        cursor.moveToNext();
                    }
                }
            }
            cursor.close();
        }
    }

    // Change layout component visibility as per situation
    public void update_layout()
    {
        if(noteCount == 0) {
            no_note_card.setVisibility(View.VISIBLE);
            noteScrollView.setVisibility(View.GONE);
        }
        else {
            noteScrollView.setVisibility(View.VISIBLE);
            no_note_card.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        refreshCards();
        update_layout();
    }

    @Override
    public void onDeleteNote() {
        refreshCards();
        update_layout();
        Toast.makeText(this,"Note Deleted",Toast.LENGTH_SHORT).show();
    }

}
