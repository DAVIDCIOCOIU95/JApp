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
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MonkeyDiary extends AppCompatActivity implements View.OnClickListener {
    private DatabaseManipulator dm;
    String[] stg1;
    final Calendar myCalendar = Calendar.getInstance();

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monkey_diary);
        getSupportActionBar().hide();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.getMenu().findItem(R.id.nav_monkey).setChecked(true);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        TextView txtDate = (TextView) findViewById(R.id.textViewDate);
        txtDate.setText(currentDate());
        // Set Save Button
        View saveButton = findViewById(R.id.imageButtonSave);
        saveButton.setOnClickListener(this);
        // Set Calendar Button
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
                String monkeyEdit = ((EditText) findViewById(R.id.editTextMonkeyEnter)).getText().toString();
                this.dm.insertMonkeyDiary(dateSelected,monkeyEdit);
                // Make a toast for successful insertion
                Context context = getApplicationContext();
                CharSequence text = "New memos added!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                break;
            case R.id.imageButtonCalendar:
                new DatePickerDialog(this, R.style.DialogTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.imageButtonMore:
                Intent checkData = new Intent(MonkeyDiary.this, CheckData.class);
                checkData.putExtra("Request", "Diary");
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
            //Log.e("chosen date", chosenDateString);
            updateFieldsWithDate(chosenDateString);
        }

    };

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
                    // If monkey selected do nothing, else do show a dialog
                    if(item.getItemId() == R.id.nav_monkey) {
                        Context context = getApplicationContext();
                        CharSequence text = "Your are already on Diary.";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    } else {
                        // Show alert
                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(MonkeyDiary.this, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(MonkeyDiary.this);
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
                                                Intent selectedFragmentHome = new Intent(MonkeyDiary.this, MainActivity.class);
                                                startActivity(selectedFragmentHome);
                                                overridePendingTransition(0, 0);
                                                break;
                                                // Case bullet go to bullet
                                            case R.id.nav_bullet:
                                                Intent selectedFragmentBullet = new Intent(MonkeyDiary.this, AddBulletJournal.class);
                                                startActivity(selectedFragmentBullet);
                                                overridePendingTransition(0,0);
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

    private void updateFieldsWithDate(String date) {
        // get data in edit views if table exists in db
        stg1 = dm.selectMonkeyDiary(date);
        Log.e("chosen date in update fields", date);
        Log.e("stg1 content", stg1[0]);
        EditText monkeyEdit = (EditText) findViewById(R.id.editTextMonkeyEnter);
        TextView txtDate = (TextView) findViewById(R.id.textViewDate);
        txtDate.setText(date);
        monkeyEdit.setText(stg1[1]);
    }

    @Override
    public void onBackPressed()
    {
        showInformationSaveDialog();
    }

    protected void showInformationSaveDialog() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(MonkeyDiary.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(MonkeyDiary.this);
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
                        MonkeyDiary.this.finish();
                        Intent homePage = new Intent(MonkeyDiary.this, MainActivity.class);
                        startActivity(homePage);

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onResume() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.getMenu().findItem(R.id.nav_monkey).setChecked(true);
        super.onResume();
    }

    public static String currentDate() {
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String todayDateString = formatter.format(todayDate);
        return todayDateString;
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
