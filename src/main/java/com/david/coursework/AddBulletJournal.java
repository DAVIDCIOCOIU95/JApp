package com.david.coursework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import static com.david.coursework.DatabaseManipulator.TABLE_NAME;
import static com.david.coursework.DatabaseManipulator.db;
import static com.david.coursework.MonkeyDiary.currentDate;

public class AddBulletJournal extends AppCompatActivity implements View.OnClickListener {
    private DatabaseManipulator dm;
    String[] stg1;
    final Calendar myCalendar = Calendar.getInstance();

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bullett_journal);
        getSupportActionBar().hide();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        TextView txtDate = (TextView) findViewById(R.id.textViewDate);
        txtDate.setText(currentDate());

        View saveButton = findViewById(R.id.imageButtonSave);
        saveButton.setOnClickListener(this);

        View calendarButton = findViewById(R.id.imageButtonCalendar);
        calendarButton.setOnClickListener(this);

        View moreButton = findViewById(R.id.imageButtonMore);
        moreButton.setOnClickListener(this);

        dm = new DatabaseManipulator(this);
        updateFieldsWithDate(currentDate());

    }



    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButtonSave:
                String dateSelected = ((TextView) findViewById(R.id.textViewDate)).getText().toString();
                String grateful = ((EditText) findViewById(R.id.editTextGrateful)).getText().toString();
                String great = ((EditText) findViewById(R.id.editTextGreat)).getText().toString();
                String affirmations = ((EditText) findViewById(R.id.editTextAffirmations)).getText().toString();
                this.dm.insertBulletDiary(dateSelected, grateful, great, affirmations);
                // Make a toast for successful insertion
                Context context = getApplicationContext();
                CharSequence text = "New memos added!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                break;
            case R.id.imageButtonCalendar:
                new DatePickerDialog(this, R.style.DialogTheme,  date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.imageButtonMore:
                Intent checkData = new Intent(AddBulletJournal.this, CheckData.class);
                checkData.putExtra("Request", "Bullet");
                startActivity(checkData);
                break;
        }
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            // Set the calendar object to the chosen date
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            // Return a string used to retrieve data from the database and update the edit text and date fields
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String chosenDateString = formatter.format(myCalendar.getTime());
            updateFieldsWithDate(chosenDateString);
        }

    };


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
                    // If bullet selected do nothing, else do show a dialog
                    if(item.getItemId() == R.id.nav_bullet) {
                        Context context = getApplicationContext();
                        CharSequence text = "Your are already on Bullet Diary.";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    } else {
                        // Show alert
                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(AddBulletJournal.this, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(AddBulletJournal.this);
                        }
                        builder.setMessage("If you leave before saving, your changes will be lost.\nDo you want to leave?");
                        builder.setCancelable(false);
                        builder.setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        builder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Deal with each button
                                        switch (item.getItemId()) {
                                            // Case home just go home
                                            case R.id.nav_home:
                                                Intent selectedFragmentHome = new Intent(AddBulletJournal.this, MainActivity.class);
                                                startActivity(selectedFragmentHome);
                                                overridePendingTransition(0, 0);
                                                break;
                                            // Case monkey got o monkey
                                            case R.id.nav_monkey:
                                                Intent selectedFragmentMonkey = new Intent(AddBulletJournal.this, MonkeyDiary.class);
                                                startActivity(selectedFragmentMonkey);
                                                overridePendingTransition(0, 0);
                                                break;
                                        }
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    return true;
                }
            };



    @Override
    public void onBackPressed()
    {
        showInformationSaveDialog();
    }

    protected void showInformationSaveDialog() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(AddBulletJournal.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(AddBulletJournal.this);
        }
        builder.setMessage("If you leave before saving, your changes will be lost.\nDo you want to leave?");
        builder.setCancelable(false);
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel everything
                        AddBulletJournal.this.finish();
                        Intent homePage = new Intent(AddBulletJournal.this, MainActivity.class);
                        startActivity(homePage);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }





    private void updateFieldsWithDate(String date) {
        // get data in edit views if table exists in db
        stg1 = dm.selectBulletDiary(date);
        EditText grateful = (EditText) findViewById(R.id.editTextGrateful);
        EditText great = (EditText) findViewById(R.id.editTextGreat);
        EditText affirmations = (EditText) findViewById(R.id.editTextAffirmations);
        TextView txtDate = (TextView) findViewById(R.id.textViewDate);
        txtDate.setText(date);
        grateful.setText(stg1[1]);
        great.setText(stg1[2]);
        affirmations.setText(stg1[3]);
    }

    public static String currentDate() {
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String todayDateString = formatter.format(todayDate);
        return todayDateString;
    }

    @Override
    public void onResume(){
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.getMenu().findItem(R.id.nav_bullet).setChecked(true);
        super.onResume();
    }
}
