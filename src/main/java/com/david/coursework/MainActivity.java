package com.david.coursework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.nio.charset.MalformedInputException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener, AdapterView.OnItemSelectedListener {
    final Calendar myCalendar = Calendar.getInstance();
    String[] stg1;
    private DatabaseManipulator dm;
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
    private boolean moreCode = false;
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
        new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
                // if home stay here, else start activity
                if(item.getItemId() == R.id.nav_home)
                {
                    Context context = getApplicationContext();
                    CharSequence text = "You are already on Home.";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                    // Show alert
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(MainActivity.this);
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
                                    // Switch case
                                    switch (item.getItemId()) {
                                        case R.id.nav_bullet:
                                            Intent selectedFragmentBullet = new Intent(MainActivity.this, AddBulletJournal.class);
                                            startActivity(selectedFragmentBullet);
                                            overridePendingTransition(0,0);
                                            break;
                                        case R.id.nav_monkey:
                                            Intent selectedFragmentMonkey = new Intent(MainActivity.this, MonkeyDiary.class);
                                            startActivity(selectedFragmentMonkey);
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

    public static String currentDate() {
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String todayDateString = formatter.format(todayDate);
        return todayDateString;
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButtonSave:
                String dateSelected = ((TextView) findViewById(R.id.textViewDate)).getText().toString();
                String quote = ((EditText) findViewById(R.id.editTextQuote)).getText().toString();
                this.dm.insertQuote(dateSelected, quote);
                // Make a toast for successful insertion
                Context context = getApplicationContext();
                CharSequence text = "New quote added!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                if(moreCode == false)
                setQuoteList();
                break;
            case R.id.imageButtonCalendar:
                new DatePickerDialog(this, R.style.DialogTheme,  date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.imageButtonMore:
                setQuoteList();
                break;
            case R.id.imageButtonNotification:
                Intent setReminder = new Intent(MainActivity.this, Reminder.class);
                startActivity(setReminder);
                break;
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        /*
        Spinner spinnerMenu = findViewById(R.id.spinnerMenu);
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this, R.array.spinnerMenu, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMenu.setAdapter(adapterSpinner);
        spinnerMenu.setOnItemSelectedListener(this);
        spinnerMenu.setSelected(false);
        */

        View notificationButton = findViewById(R.id.imageButtonNotification);
        notificationButton.setOnClickListener(this);

        dm = new DatabaseManipulator(this);
        updateFieldsWithDate(currentDate());
    }

    public void setQuoteAdapter() {
        String[] maintitle = {"Date:"};
        String[] subtitle = {"Quote:"};
        ListView list;
        MyListAdapterDiary adapter=new MyListAdapterDiary(this, maintitle, subtitle);
        list=(ListView)findViewById(R.id.listQuote);
        list.setAdapter(adapter);
    }


    public void setQuoteList() {
        // List view

            List<String[]> namesList = null;
            String[] stgList;
            String[] stgEmpty = {""};
            ListView quoteDetail = (ListView) findViewById(R.id.listQuote);
            // Get the database and the string in the database
            namesList = dm.selectAllQuote();

            stgList = new String[namesList.size()];
            int x = 0;

            String stg;
            for (String[] name : namesList) {
                stg =
                        getString(R.string.date).toUpperCase() +" "
                                + name[0] + "\n"+ getString(R.string.daily_quote).toUpperCase() +"\n"
                                + name[1]+ "\n";
                stgList[x] = stg;
                x++;
            }
        if (moreCode == false) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.mytextview,
                    stgList);
            quoteDetail.setAdapter(adapter);
            moreCode = true;

        } else {
            quoteDetail.setAdapter(null);
            moreCode = false;
        }
    }

    @Override
    public void onBackPressed()
    {
        showInformationSaveDialog();
    }

    protected void showInformationSaveDialog() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(MainActivity.this);
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
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void updateFieldsWithDate(String date) {
        // get data in edit views if table exists in db
        stg1 = dm.selectQuote(date);
        EditText quote = (EditText) findViewById(R.id.editTextQuote);
        TextView txtDate = (TextView) findViewById(R.id.textViewDate);
        txtDate.setText(date);
        quote.setText(stg1[1]);
    }

    @Override
    public void onResume(){
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.getMenu().findItem(R.id.nav_home).setChecked(true);
        super.onResume();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        /*
        String text = parent.getItemAtPosition(position).toString();
            if(text.equals("Set Reminder"))
        {
            Intent timePickerIntent = new Intent(MainActivity.this, Reminder.class);
            startActivity(timePickerIntent);
        }

         */

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
