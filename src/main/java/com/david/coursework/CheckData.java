package com.david.coursework;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class CheckData extends AppCompatActivity {
    DatabaseManipulator dm;


    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_layout);
        getSupportActionBar().hide();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle extras = getIntent().getExtras();
        String result = extras.getString("Request");
        // Check for what activity is returned by data,
        if(result.equals("Bullet")) {
            List<String[]> names2 = null;
            String[] stg1;
            ListView bulletDetail = (ListView) findViewById(R.id.list);
            // Get the database and the string in the database
            dm = new DatabaseManipulator(this);
            names2 = dm.selectAllBullet();

            stg1 = new String[names2.size()];
            int x = 0;
            String stg;
            for (String[] name : names2) {
                stg =
                        getString(R.string.date).toUpperCase() + " "
                                + name[0] + "\n" + getString(R.string.what_are_you_grateful_for).toUpperCase()+"\n"
                                + name[1] + "\n" + getString(R.string.what_would_make_today_great).toUpperCase()+ "\n"
                                + name[2] + "\n" + getString(R.string.daily_affirmations).toUpperCase()+"\n"
                                + name[3]+ "\n";
                stg1[x] = stg;
                x++;
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.mytextview,
                    stg1);
            bulletDetail.setAdapter(adapter);
        }
        else if(result.equals("Diary")) {
            TextView myTitle = (TextView) findViewById(R.id.textViewTitle);
            myTitle.setText("Diary");
            List<String[]> names2 = null;
            String[] stg1;
            ListView bulletDetail = (ListView) findViewById(R.id.list);
            // Get the database and the string in the database
            dm = new DatabaseManipulator(this);
            names2 = dm.selectAllMonkeyDiary();

            stg1 = new String[names2.size()];
            int x = 0;
            String stg;
            for (String[] name : names2) {
                stg =
                        getString(R.string.date).toUpperCase() + " "
                                + name[0] + "\n" + getString(R.string.diary).toUpperCase() +"\n"
                                + name[1] + "\n";
                stg1[x] = stg;
                x++;
            }
            // Trying out customized list adapter
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.mytextview,
                    stg1);
            bulletDetail.setAdapter(adapter);
        } else {

        }
    }

}

