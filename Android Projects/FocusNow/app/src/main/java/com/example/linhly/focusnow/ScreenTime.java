package com.example.linhly.focusnow;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Calendar;

public class ScreenTime extends AppCompatActivity {
    int id;
    String day;
    int minutes;
    SQLiteDatabase db = null;
    ArrayList<Integer> array_list = new ArrayList <>();
    ArrayList<Integer> days = new ArrayList <>(7);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_time);

        Intent intent = getIntent();
        array_list = intent.getIntegerArrayListExtra("data_array");

        db = openOrCreateDatabase("any", Context.MODE_PRIVATE, null);

        initiate_database();
        obtainData();
        displayData();
    }

    void initiate_database() {
        ContentValues values = new ContentValues();
        db.execSQL("Create table if not exists TimeCollected(ID integer primary key autoincrement, Date string, Minutes int);");
        for (int i = 0; i < array_list.size(); i++) {
            minutes = array_list.get(i);
        }

        Calendar cal = Calendar.getInstance();
        int date = cal.get(Calendar.DAY_OF_WEEK);

        switch (date) {
            case Calendar.SUNDAY:
                day = "Sunday";
                break;
            case Calendar.MONDAY:
                day = "Monday";
                break;
            case Calendar.TUESDAY:
                day = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                day = "Wednesday";
                break;
            case Calendar.THURSDAY:
                day = "Thursday";
                break;
            case Calendar.FRIDAY:
                day = "Friday";
                break;
            case Calendar.SATURDAY:
                day = "Saturday";
                break;
        }

        values.put("Date", day);
        values.put("Minutes", minutes);
        db.insert("TimeCollected", null, values);

        Log.v("Inside: ", "" + array_list);
        logDatabase();
    }

    void obtainData() {
        int sundayC = 0;
        int mondayC = 0;
        int tuesdayC = 0;
        int wednesdayC = 0;
        int thursdayC = 0;
        int fridayC = 0;
        int saturdayC = 0;
        days.add(0, sundayC);
        days.add(1, mondayC);
        days.add(2, tuesdayC);
        days.add(3, wednesdayC);
        days.add(4, thursdayC);
        days.add(5, fridayC);
        days.add(6, saturdayC);

        Cursor minuteValues = null;
        minuteValues = db.rawQuery("Select Date, Minutes from TimeCollected where Minutes != 0" , null);

        Log.v("Records: ", "" + minuteValues.getCount());

        while(minuteValues.moveToNext()) {
            String column_day = minuteValues.getString(minuteValues.getColumnIndex("Date"));
            int column_minutes = minuteValues.getInt(minuteValues.getColumnIndex("Minutes"));

            Log.v("DAY: ", "" + column_day);
            Log.v("MINUTES: ", "" + column_minutes);


            if (column_day == "Sunday") {
                sundayC += column_minutes;
                days.set(0, sundayC);
            } else if (column_day == "Monday") {
                mondayC += column_minutes;
                days.set(1, mondayC);
            } else if (column_day == "Tuesday") {
                Log.v("TUESDAY C: ", "" + tuesdayC);
                tuesdayC += column_minutes;
                days.set(2, tuesdayC);
            } else if (column_day.equals("Wednesday")) {
                wednesdayC += column_minutes;
                days.set(3, wednesdayC);
            } else if (column_day == "Thursday") {
                thursdayC += column_minutes;
                days.set(4, thursdayC);
            } else if (column_day == "Friday") {
                fridayC += column_minutes;
                days.set(5, fridayC);
            } else if (column_day == "Saturday") {
                saturdayC += column_minutes;
                days.set(6, saturdayC);
            }
        }

        minuteValues.close();
        Log.v("DaysMinutes: ", "" + days);
    }

    void displayData() {
        BarChart chart = (BarChart) findViewById(R.id.chart);
        ArrayList<BarEntry> entries = new ArrayList <BarEntry>();

        entries.add(new BarEntry(0, days.get(0)));
        entries.add(new BarEntry(1, days.get(1)));
        entries.add(new BarEntry(2, days.get(2)));
        entries.add(new BarEntry(3, days.get(3)));
        entries.add(new BarEntry(4, days.get(4)));
        entries.add(new BarEntry(5, days.get(5)));
        entries.add(new BarEntry(6, days.get(6)));

        BarDataSet dataSet = new BarDataSet(entries, "Label");

        BarData barData = new BarData(dataSet);
        chart.setData(barData);
        chart.invalidate();
    }

    void logDatabase() {
        String valuesChecker = "";
        Cursor checkValues = null;
        checkValues = db.rawQuery("Select * from TimeCollected" , null);
        while (checkValues.moveToNext()) {
            int column_a = checkValues.getColumnIndex(("ID"));
            int column_b = checkValues.getColumnIndex(("Date"));
            int column_c = checkValues.getColumnIndex(("Minutes"));

            valuesChecker += "ID: ";
            valuesChecker += checkValues.getString(column_a);
            valuesChecker += " ";

            valuesChecker += "Date: ";
            valuesChecker += checkValues.getString(column_b);
            valuesChecker += " ";

            valuesChecker += "Minutes: ";
            valuesChecker += checkValues.getString(column_c);
            valuesChecker += " ";

            Log.v("Time Database: ", "" + valuesChecker);
        }

    }

    public void back(View view) {
        Intent x = new Intent(this, HomeScreen.class);
        startActivity(x);
    }
}
