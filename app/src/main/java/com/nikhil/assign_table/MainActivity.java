package com.nikhil.assign_table;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;
    String[] types;
    String[] columnNames;

    Menu m1;
    int pin = 0;
    Cursor c;
    EditText[] editTexts;
    TextView[] textViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        System.out.println("d");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        m1 = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.create) {
            setContentView(R.layout.activity_create);


            final Spinner spinner = findViewById(R.id.spinner);

            String[] columns = {"1", "2", "3", "4", "5", "6"};
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, columns);
            spinner.setAdapter(arrayAdapter);

            final LinearLayout linearLayout = findViewById(R.id.create_layout);

            final EditText[] editTexts = new EditText[6];

//            StringBuilder myTags = new StringBuilder("edittag1");
            final int RADIX = 10;

            final Spinner[] spinners = new Spinner[6];
//            StringBuilder myTags2 = new StringBuilder("spinnertag1");

            final String[] columnTypes = {"int", "varchar(30)"};


            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (pin == 1)
                        linearLayout.removeAllViews();
                    for (int i = 0; i < position + 1; i++) {
                        editTexts[i] = new EditText(getApplicationContext());
//                editTexts[i].setTag(myTags);


                        editTexts[i].setHint("Column name");
                        spinners[i] = new Spinner(getApplicationContext());
                        spinners[i].setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, columnTypes));
//                myTags.setCharAt(7, Character.forDigit(i + 2, RADIX));

                        linearLayout.addView(editTexts[i]);
                        linearLayout.addView(spinners[i]);

                    }


                    pin = 1;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            db = openOrCreateDatabase("mydatabase.db", Context.MODE_PRIVATE, null);

            final EditText editText = findViewById(R.id.editText);
            Button button = findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();


                    String str = "";

                    int flag = 0;
                    for (int i = 0; i < Integer.parseInt(spinner.getSelectedItem().toString()); i++) {

                        if (flag == 1)
                            str += ",";


                        String s = "";
                        if (spinners[i].getSelectedItem() == "int")
                            s = "0";
                        else
                            s = "1";

                        str += editTexts[i].getText() + s + " " + spinners[i].getSelectedItem();

                        flag = 1;

                    }
                    db.execSQL("Create Table IF NOT EXISTS   " + editText.getText().toString() + "(" + str + ")");

                    Toast.makeText(getApplicationContext(), "Table created successfully", Toast.LENGTH_SHORT).show();

                    // Cursor c;
                    //   c =db.rawQuery("SELECT * FROM student1", null);

                    //     while (c.moveToNext()){
                    //        Log.e("asdfghjkl", c.getString(0));
                    //    }

                    //    c.close();
                    // SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.Columns where TABLE_NAME = 'YourTableName'
                    // SQLiteDatabase mDataBase;
                    //  mDataBase = getReadableDatabase();
                    //  Cursor dbCursor = mDataBase.query(TABLE_NAME, null, null, null, null, null, null);
                    //  String[] columnNames = dbCursor.getColumnNames();
                   /* String[] columnNames;
                    Cursor c = db.rawQuery("SELECT * FROM ayusy WHERE 0", null);
                    try {
                        columnNames = c.getColumnNames();
                    } finally {
                        c.close();
                    }
                    for (int i = 0; i < columnNames.length; i++) {
                        Log.i("fghjk", columnNames[i]);
                    }
                    Toast.makeText(getApplicationContext(), "dfghjkltyuikjn", Toast.LENGTH_LONG).show();*/
                }
            });
        }


        if (id == R.id.insert) {
            setContentView(R.layout.activity_insert);

            final Spinner tableName = findViewById(R.id.tableNameSpinner);
            db = openOrCreateDatabase("mydatabase.db", Context.MODE_PRIVATE, null);


            c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name != 'android_metadata'", null);
            ArrayList<String> strings = new ArrayList<>();


            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    strings.add(c.getString(0));
                    c.moveToNext();
                }
            }
            tableName.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, strings));


            final LinearLayout linearLayout = findViewById(R.id.insert_layout);


            tableName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String[] columnNames = null;
                    c = db.rawQuery("SELECT * FROM " + tableName.getSelectedItem().toString() + " WHERE 0", null);
                    try {
                        columnNames = c.getColumnNames();

                    } catch (Exception e1) {
                    }
                    editTexts = new EditText[columnNames.length];
                    textViews = new TextView[columnNames.length];
                    linearLayout.removeAllViews();

                    types = new String[columnNames.length];

                    for (int i = 0; i < columnNames.length; i++) {


                        if (columnNames[i].substring(columnNames[i].length() - 1).equals("0"))
                            types[i] = "int";
                        else types[i] = "varchar";


                        textViews[i] = new TextView(getApplicationContext());
                        editTexts[i] = new EditText(getApplicationContext());

                        textViews[i].setTextSize(20);
                        textViews[i].setText("Column name: " + columnNames[i] + "(" + types[i] + ")");

                        linearLayout.addView(textViews[i]);
                        linearLayout.addView(editTexts[i]);

                    }
//                    db.close();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            Button button = findViewById(R.id.insert_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String str = "INSERT INTO " + tableName.getSelectedItem() + " values(";

                    int flag = 0;
                    for (int i = 0; i < editTexts.length; i++) {


                        if (flag == 1)
                            str += ", ";

                        if (types[i].equals("int"))
                            str += editTexts[i].getText().toString();
                        else
                            str += "'" + editTexts[i].getText().toString() + "'";
                        flag = 1;
                    }
                    str += ");";

                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();


                    db = openOrCreateDatabase("mydatabase.db", Context.MODE_PRIVATE, null);
                    db.execSQL(str);
                }
            });


            c.close();


        }
        if (id == R.id.update) {
            setContentView(R.layout.activity_update);

            final Spinner tableName = findViewById(R.id.tableNameSpinner2);
            db = openOrCreateDatabase("mydatabase.db", Context.MODE_PRIVATE, null);


            c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name != 'android_metadata'", null);
            ArrayList<String> strings = new ArrayList<>();


            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    strings.add(c.getString(0));
                    c.moveToNext();
                }
            }
            tableName.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, strings));

            c.close();


            final LinearLayout linearLayout = findViewById(R.id.update_layout);
            final Spinner columnNamesSpinner = findViewById(R.id.colNames);


            // Table name spinner event
            tableName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    columnNames = null;


                    c = db.rawQuery("SELECT * FROM " + tableName.getSelectedItem().toString() + " WHERE 0", null);
                    try {
                        columnNames = c.getColumnNames();

                    } catch (Exception e1) {
                    }

                    // column names are added to the spinner
                    columnNamesSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, columnNames));


                    editTexts = new EditText[columnNames.length];
                    textViews = new TextView[columnNames.length];
                    linearLayout.removeAllViews();

                    types = new String[columnNames.length];


                    for (int i = 0; i < columnNames.length; i++) {


                        if (columnNames[i].substring(columnNames[i].length() - 1).equals("0"))
                            types[i] = "int";
                        else types[i] = "varchar";


                        textViews[i] = new TextView(getApplicationContext());
                        editTexts[i] = new EditText(getApplicationContext());

                        textViews[i].setTextSize(20);
                        textViews[i].setText("Column name: " + columnNames[i] + "(" + types[i] + ")");

                        linearLayout.addView(textViews[i]);
                        linearLayout.addView(editTexts[i]);

                    }
                    //TABLE 1ST COLUMN DETAILS AND VALUE IN SPINNER THEN DISPLAY SELECTED VALUE DETAILS

//                    c = db.rawQuery("Select * from Student where prn="+prns, null);
//                    c.moveToNext();

//                    String pr=c.getString(0);
//                    String naam=c.getString(1);
//                    String em=c.getString(2);
//                    String phnn=c.getString(3);


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {


                }
            });

            final Spinner rowsSpinner = findViewById(R.id.colValues);


            // rows are shown in 2nd spinner
            columnNamesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    ArrayList<String> rows = new ArrayList<>();
                    c = db.rawQuery("SELECT " + columnNamesSpinner.getSelectedItem().toString() + " FROM " + tableName.getSelectedItem().toString(), null);


                    Toast.makeText(getApplicationContext(), Integer.toString(c.getCount()), Toast.LENGTH_SHORT).show();
                    if (c.moveToFirst()) {
                        while (!c.isAfterLast()) {

                            if (columnNamesSpinner.getSelectedItem().toString().substring(columnNamesSpinner.getSelectedItem().toString().length() - 1).equals("0")) {
                                rows.add(Integer.toString(c.getInt(0)));

                            } else {
                                rows.add(c.getString(0));

                            }
                            c.moveToNext();
                        }
                    }
                    rowsSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, rows));


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            rowsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    String q;
                    if (columnNamesSpinner.getSelectedItem().toString().substring(columnNamesSpinner.getSelectedItem().toString().length() - 1).equals("0")) {

                        q = "SELECT * FROM " + tableName.getSelectedItem().toString() + " WHERE " + columnNamesSpinner.getSelectedItem().toString() + " = " + rowsSpinner.getSelectedItem().toString();
                    } else {
                        q = "SELECT * FROM " + tableName.getSelectedItem().toString() + " WHERE " + columnNamesSpinner.getSelectedItem().toString() + " = '" + rowsSpinner.getSelectedItem().toString() + "'";
                    }
                    c = db.rawQuery(q, null);

                    Log.e("qwert Query", q);
                    if (c.moveToFirst()) {

                        while (!c.isAfterLast()) {

                            for (int i = 0; i < columnNamesSpinner.getCount(); i++) {


                                if (columnNames[i].substring(columnNames[i].length() - 1).equals("0")) {
                                    Log.i("qwert int", Integer.toString(c.getInt(i)));

                                    editTexts[i].setText(Integer.toString(c.getInt(i)));
                                } else {
                                    Log.i("qwert varchar", c.getString(i));
                                    editTexts[i].setText(c.getString(i));

                                }

                            }
                            c.moveToNext();
                            break;

                        }
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            Button button = findViewById(R.id.update_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db = openOrCreateDatabase("mydatabase.db", Context.MODE_PRIVATE, null);

                    String str = "UPDATE " + tableName.getSelectedItem().toString() + " SET ";

                    int flag = 0;
                    for (int i = 0; i < columnNames.length; i++) {
                        if (flag == 1) {
                            str += ", ";
                        }

                        str += columnNames[i] + " = ";

                        if (columnNames[i].substring(columnNames[i].length() - 1).equals("0")) {
                            str += Integer.parseInt(editTexts[i].getText().toString());
                        } else {
                            str += "'" + editTexts[i].getText().toString() + "'";
                        }

                        flag = 1;

                    }

                    str += " WHERE " + columnNamesSpinner.getSelectedItem().toString() + " = ";
                    if (columnNamesSpinner.getSelectedItem().toString().substring(columnNamesSpinner.getSelectedItem().toString().length() - 1).equals("0")) {
                        str += Integer.parseInt(rowsSpinner.getSelectedItem().toString());
                    } else
                        str += "'" + rowsSpinner.getSelectedItem().toString() + "'";

                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();


                    db.execSQL(str);

                }
            });


        }
        if (id == R.id.show) {
            setContentView(R.layout.activity_show);


            final Spinner tableName = findViewById(R.id.tableNameSpinner2S);
            db = openOrCreateDatabase("mydatabase.db", Context.MODE_PRIVATE, null);


            c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name != 'android_metadata'", null);
            ArrayList<String> strings = new ArrayList<>();


            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    strings.add(c.getString(0));
                    c.moveToNext();
                }
            }
            tableName.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, strings));

            c.close();


            final LinearLayout linearLayout = findViewById(R.id.show_layout);
            final Spinner columnNamesSpinner = findViewById(R.id.colNamesS);


            // Table name spinner event
            tableName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    columnNames = null;


                    c = db.rawQuery("SELECT * FROM " + tableName.getSelectedItem().toString() + " WHERE 0", null);
                    try {
                        columnNames = c.getColumnNames();

                    } catch (Exception e1) {
                    }

                    // column names are added to the spinner
                    columnNamesSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, columnNames));


                    editTexts = new EditText[columnNames.length];
                    textViews = new TextView[columnNames.length];
                    linearLayout.removeAllViews();

                    types = new String[columnNames.length];


                    for (int i = 0; i < columnNames.length; i++) {


                        if (columnNames[i].substring(columnNames[i].length() - 1).equals("0"))
                            types[i] = "int";
                        else types[i] = "varchar";


                        textViews[i] = new TextView(getApplicationContext());
                        editTexts[i] = new EditText(getApplicationContext());
                        editTexts[i].setEnabled(false);

                        textViews[i].setTextSize(20);
                        textViews[i].setText("Column name: " + columnNames[i] + "(" + types[i] + ")");

                        linearLayout.addView(textViews[i]);
                        linearLayout.addView(editTexts[i]);

                    }
                    //TABLE 1ST COLUMN DETAILS AND VALUE IN SPINNER THEN DISPLAY SELECTED VALUE DETAILS

//                    c = db.rawQuery("Select * from Student where prn="+prns, null);
//                    c.moveToNext();

//                    String pr=c.getString(0);
//                    String naam=c.getString(1);
//                    String em=c.getString(2);
//                    String phnn=c.getString(3);


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {


                }
            });

            final Spinner rowsSpinner = findViewById(R.id.colValuesS);


            // rows are shown in 2nd spinner
            columnNamesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    ArrayList<String> rows = new ArrayList<>();
                    c = db.rawQuery("SELECT " + columnNamesSpinner.getSelectedItem().toString() + " FROM " + tableName.getSelectedItem().toString(), null);


                    Toast.makeText(getApplicationContext(), Integer.toString(c.getCount()), Toast.LENGTH_SHORT).show();
                    if (c.moveToFirst()) {
                        while (!c.isAfterLast()) {

                            if (columnNamesSpinner.getSelectedItem().toString().substring(columnNamesSpinner.getSelectedItem().toString().length() - 1).equals("0")) {
                                rows.add(Integer.toString(c.getInt(0)));

                            } else {
                                rows.add(c.getString(0));

                            }
                            c.moveToNext();
                        }
                    }
                    rowsSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, rows));


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            rowsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    String q;
                    if (columnNamesSpinner.getSelectedItem().toString().substring(columnNamesSpinner.getSelectedItem().toString().length() - 1).equals("0")) {

                        q = "SELECT * FROM " + tableName.getSelectedItem().toString() + " WHERE " + columnNamesSpinner.getSelectedItem().toString() + " = " + rowsSpinner.getSelectedItem().toString();
                    } else {
                        q = "SELECT * FROM " + tableName.getSelectedItem().toString() + " WHERE " + columnNamesSpinner.getSelectedItem().toString() + " = '" + rowsSpinner.getSelectedItem().toString() + "'";
                    }
                    c = db.rawQuery(q, null);

                    Log.e("qwert Query", q);
                    if (c.moveToFirst()) {

                        while (!c.isAfterLast()) {

                            for (int i = 0; i < columnNamesSpinner.getCount(); i++) {


                                if (columnNames[i].substring(columnNames[i].length() - 1).equals("0")) {
                                    Log.i("qwert int", Integer.toString(c.getInt(i)));

                                    editTexts[i].setText(Integer.toString(c.getInt(i)));
                                } else {
                                    Log.i("qwert varchar", c.getString(i));
                                    editTexts[i].setText(c.getString(i));

                                }

                            }
                            c.moveToNext();
                            break;

                        }
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });





        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return super.onOptionsItemSelected(item);
    }
}
