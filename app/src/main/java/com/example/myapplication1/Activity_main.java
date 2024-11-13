package com.example.myapplication1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.text.DecimalFormat;
import java.util.List;

public class Activity_main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sh = getSharedPreferences("MySharedPref", 0);
        //if shared preferences is empty, enter default values
        if (sh.getString("material", null) == null) {
            SharedPreferences.Editor myEdit = sh.edit();
            // Storing the key and its value as the data fetched from edittext
            myEdit.putString("material", "Aluminum");
            myEdit.apply();
        }
        String material = sh.getString("material", "");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Home");
        TextView si = findViewById(R.id.Sizeicon);
        TextView ti = findViewById(R.id.Tempicon);
        TextView ei = findViewById(R.id.Expicon);
        //if shared preferences is empty, enter default values
        if (sh.getString("size", null) == null) {
            SharedPreferences.Editor myEdit = sh.edit();
            // Storing the key and its value as the data fetched from edittext
            myEdit.putString("size", "IN");
            myEdit.putString("temp", "F");
            myEdit.putString("exp", "IN");
            myEdit.apply();
        }
        String sizep = sh.getString("size", "");
        String tempp = sh.getString("temp", "");
        String expp = sh.getString("exp", "");
        si.setText(sizep);
        ti.setText(tempp);
        ei.setText(expp);

        Spinner spinner = findViewById(R.id.metaltype);
        spinner.getOnItemSelectedListener();

        //Get types from db
        DataBaseHelper dataBaseHelper = new DataBaseHelper(Activity_main.this);
        List<String> types = dataBaseHelper.getType();
        if (types.isEmpty()) {
            dataBaseHelper.populateFull();
            types = dataBaseHelper.getType();
        }

        //Set to spinner
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);
        int mPosition = adapter.getPosition(material);
        spinner.setSelection(mPosition);

    }

    public void cal(View v) {

        SharedPreferences sh = getSharedPreferences("MySharedPref", 0);
        Spinner spinner = findViewById(R.id.metaltype);
        String types = spinner.getSelectedItem().toString();
        SharedPreferences.Editor myEdit = sh.edit();
        // Storing the key and its value as the data fetched from edittext
        myEdit.putString("material", types);
        myEdit.apply();

        TextView s = findViewById(R.id.Size);
        TextView t = findViewById(R.id.Temp);
        TextView e = findViewById(R.id.Expansion);
        String size = null;
        String temp = null;
        String exp = null;
        try {
            size = s.getText().toString();
            temp = t.getText().toString();
            exp = e.getText().toString();
        } finally {
            //if cal e:
            if (!size.isEmpty() && !temp.isEmpty() && exp.isEmpty()) {
                sortE(v);
            }
            //if cal s:
            else if (!exp.isEmpty() && !temp.isEmpty() && size.isEmpty()) {
                cals(v);
            }
            //if cal t:
            else if (!size.isEmpty() && !exp.isEmpty() && temp.isEmpty()) {
                calt(v);
            }
            else{
                String res = "Enter 2 values";
                ((TextView)findViewById(R.id.Result)).setText(res);
            }
        }
    }

    public void sortE(View v) {
        //Find user unit settings
        TextView sv = findViewById(R.id.Sizeicon);
        TextView tv = findViewById(R.id.Tempicon);
        TextView ev = findViewById(R.id.Expicon);
        String su = sv.getText().toString();
        String tu = tv.getText().toString();
        String eu = ev.getText().toString();

        //Get 2 of 3 input
        TextView s = findViewById(R.id.Size);
        TextView t = findViewById(R.id.Temp);
        String tempa = t.getText().toString();
        float tempin = Float.parseFloat(tempa);
        String sizestring = s.getText().toString();
        Spinner spinner = findViewById(R.id.metaltype);
        String types = spinner.getSelectedItem().toString();
        float size = Float.parseFloat(sizestring);
        DataBaseHelper dataBaseHelper = new DataBaseHelper(Activity_main.this);
        Float exp = (dataBaseHelper.getExpansion(types));
        float tempout = tempin;

        //Convert F to K
        if(tu .equals("F")) {
            tempout = (tempin * .5555555F);
        }
        DecimalFormat df = new DecimalFormat("#.########");
        df.setMaximumFractionDigits(7);

        //Sort to different calculations due to units selected
        if (su .equals(eu)) {
            Float res = (exp / 1000000) * tempout * size;
            String out = (df.format(res) + "  " + eu);
            ((TextView) findViewById(R.id.Result)).setText(out);
            //if exp is one thousanth of size
        } else if (su .equals("Inch") && eu .equals("Thou IN") || su .equals("Meter") && eu .equals("MM")) {
            float res = (exp / 1000000) * tempout * size;
            Float newres = (res * 1000);
            String out = (df.format(newres) + "  " + eu);
            ((TextView) findViewById(R.id.Result)).setText(out);
        } else if (su .equals("Foot") && eu .equals("Inch")) {
            float res = (exp / 1000000) * tempout * size;
            Float newres = (res * 12);
            String out = (df.format(newres) + "  " + eu);
            ((TextView) findViewById(R.id.Result)).setText(out);
        } else if (su .equals("Inch") && eu.equals("Foot")) {
            float res = (exp / 1000000) * tempout * size;
            Float newres = (res / 12);
            String out = (df.format(newres) + "  " + eu);
            ((TextView) findViewById(R.id.Result)).setText(out);
        } else if (su.equals("Thou IN") && eu.equals("Inch") || su.equals("MM") && eu.equals("Meter")) {
            float res = (exp / 1000000) * tempout * size;
            Float newres = (res / 1000);
            String out = (df.format(newres) + "  " + eu);
            ((TextView) findViewById(R.id.Result)).setText(out);
        } else if (su.equals("Thou IN") && eu.equals("Foot")) {
            float res = (exp / 1000000) * tempout * size;
            Float newres = (res / 12000);
            String out = (df.format(newres) + "  " + eu);
            ((TextView) findViewById(R.id.Result)).setText(out);
        } else if (su.equals("Foot") && eu.equals("Thou IN")) {
            float res = (exp / 1000000) * tempout * size;
            Float newres = (res * 12000);
            String out = (df.format(newres) + "  " + eu);
            ((TextView) findViewById(R.id.Result)).setText(out);
        } else if (su.equals("MM") || su.equals("Meter")){
            String text = "Please select metric exp unit";
            ((TextView) findViewById(R.id.Result)).setText(text);
        } else {
            String text = "Please select imperial exp unit";
            ((TextView) findViewById(R.id.Result)).setText(text);
        }
    }

    @SuppressLint("SetTextI18n")
    public void cals(View v) {
        //Find user unit settings
        TextView sv = findViewById(R.id.Sizeicon);
        TextView tv = findViewById(R.id.Tempicon);
        TextView ev = findViewById(R.id.Expicon);
        String su = sv.getText().toString();
        String tu = tv.getText().toString();
        String eu = ev.getText().toString();

        //Get 2 of 3 input
        TextView t = findViewById(R.id.Temp);
        TextView e = findViewById(R.id.Expansion);
        String tempa = t.getText().toString();
        String expa = e.getText().toString();
        float tempin = Float.parseFloat(tempa);
        float expin = Float.parseFloat(expa);
        Spinner spinner = findViewById(R.id.metaltype);
        String types = spinner.getSelectedItem().toString();
        DataBaseHelper dataBaseHelper = new DataBaseHelper(Activity_main.this);
        Float exp = (dataBaseHelper.getExpansion(types));
        float tempout = tempin;

        //Convert F to K
        if (tu.equals("F")) {
            tempout = (tempin * .5555555F);
        }
        DecimalFormat df = new DecimalFormat("#.########");
        df.setMaximumFractionDigits(7);

        //Sort to different calculations due to units selected
        if (su.equals(eu)) {
            Float res = expin / (exp / 1000000) / tempout;
            ((TextView) findViewById(R.id.Result)).setText(df.format(res) + "  " + su);

            //if exp is one thousanth of size
        } else if (su.equals("Inch") && eu.equals("Thou IN") || su.equals("Meter") && eu.equals("MM")) {
            float res = expin / (exp / 1000000) / tempout;
            Float newres = (res / 1000);
            ((TextView) findViewById(R.id.Result)).setText(df.format(newres) + "  " + su);
        } else if (su.equals("Foot") && eu.equals("Inch")) {
            float res = expin / (exp / 1000000) / tempout;
            Float newres = (res / 12);
            ((TextView) findViewById(R.id.Result)).setText(df.format(newres) + "  " + su);
        } else if (su.equals("Inch") && eu.equals("Foot")) {
            float res = expin / (exp / 1000000) / tempout;
            Float newres = (res * 12);
            ((TextView) findViewById(R.id.Result)).setText(df.format(newres) + "  " + su);
        } else if (su.equals("Thou IN") && eu.equals("Inch") || su.equals("MM") && eu.equals("Meter")) {
            float res = expin / (exp / 1000000) / tempout;
            Float newres = (res * 1000);
            ((TextView) findViewById(R.id.Result)).setText(df.format(newres) + "  " + su);
        } else if (su.equals("Thou IN") && eu.equals("Foot")) {
            float res = expin / (exp / 1000000) / tempout;
            Float newres = (res * 12000);
            ((TextView) findViewById(R.id.Result)).setText(df.format(newres) + "  " + su);
        } else if (su.equals("Foot") && eu.equals("Thou IN")) {
            float res = expin / (exp / 1000000) / tempout;
            Float newres = (res / 12000);
            ((TextView) findViewById(R.id.Result)).setText(df.format(newres) + "  " + su);
        } else if (su.equals("MM") || su.equals("Meter")) {
            ((TextView) findViewById(R.id.Result)).setText("Please select metric exp unit");
        } else {
            ((TextView) findViewById(R.id.Result)).setText("Please select imperial exp unit");
        }
    }

    @SuppressLint("SetTextI18n")
    public void calt(View v) {
        //Find user unit settings
        TextView sv = findViewById(R.id.Sizeicon);
        TextView tv = findViewById(R.id.Tempicon);
        TextView ev = findViewById(R.id.Expicon);
        String su = sv.getText().toString();
        String tu = tv.getText().toString();
        String eu = ev.getText().toString();

        //Get 2 of 3 input
        TextView s = findViewById(R.id.Size);
        TextView e = findViewById(R.id.Expansion);
        String sizestring = s.getText().toString();
        String expansionstring = e.getText().toString();
        Spinner spinner = findViewById(R.id.metaltype);
        String types = spinner.getSelectedItem().toString();
        float size = Float.parseFloat(sizestring);
        float expin = Float.parseFloat(expansionstring);
        DataBaseHelper dataBaseHelper = new DataBaseHelper(Activity_main.this);
        Float exp = (dataBaseHelper.getExpansion(types));
        //Float tempout = tempin;

        DecimalFormat df = new DecimalFormat("#.########");
        df.setMaximumFractionDigits(7);

        //Sort to different calculations due to units selected
        if (su .equals(eu)) {
            Float res = expin / (exp / 1000000) / size;
            if(tu .equals("F")) {
                Float tempout = (res * 1.8F);
                ((TextView) findViewById(R.id.Result)).setText(df.format(tempout) + "  " + tu);
            }else {
                ((TextView) findViewById(R.id.Result)).setText(df.format(res) + "  " + tu);
            }
            //if exp is one thousanth of size
        } else if (su .equals("Inch") && eu .equals("Thou IN") || su .equals("Meter") && eu .equals("MM")) {
            Float res = expin / (exp / 1000000) / (size * 1000);
            if(tu .equals("F")) {
                Float tempout = (res * 1.8F);
                ((TextView) findViewById(R.id.Result)).setText(df.format(tempout) + "  " + tu);
            }else {
                ((TextView) findViewById(R.id.Result)).setText(df.format(res) + "  " + tu);
            }
        } else if (su .equals("Foot") && eu .equals("Inch")) {
            Float res = expin / (exp / 1000000) / (size * 12);
            if(tu .equals("F")) {
                Float tempout = (res * 1.8F);
                ((TextView) findViewById(R.id.Result)).setText(df.format(tempout) + "  " + tu);
            }else {
                ((TextView) findViewById(R.id.Result)).setText(df.format(res) + "  " + tu);
            }
        } else if (su .equals("Inch") && eu.equals("Foot")) {
            Float res = expin / (exp / 1000000) / (size / 12);
            if(tu .equals("F")) {
                Float tempout = (res * 1.8F);
                ((TextView) findViewById(R.id.Result)).setText(df.format(tempout) + "  " + tu);
            }else {
                ((TextView) findViewById(R.id.Result)).setText(df.format(res) + "  " + tu);
            }
        } else if (su.equals("Thou IN") && eu.equals("Inch") || su.equals("MM") && eu.equals("Meter")) {
            Float res = expin / (exp / 1000000) / (size / 1000);
            //float newres = res * 1000;
            if(tu .equals("F")) {
                Float tempout = (res * 1.8F);
                ((TextView) findViewById(R.id.Result)).setText(df.format(tempout) + "  " + tu);
            }else {
                ((TextView) findViewById(R.id.Result)).setText(df.format(res) + "  " + tu);
            }
        } else if (su.equals("Thou IN") && eu.equals("Foot")) {
            Float res = expin / (exp / 1000000) / (size / 12000);
            if(tu .equals("F")) {
                Float tempout = (res * 1.8F);
                ((TextView) findViewById(R.id.Result)).setText(df.format(tempout) + "  " + tu);
            }else {
                ((TextView) findViewById(R.id.Result)).setText(df.format(res) + "  " + tu);
            }
        } else if (su.equals("Foot") && eu.equals("Thou IN")) {
            Float res = expin / (exp / 1000000) / (size * 12000);
            if(tu .equals("F")) {
                Float tempout = (res * 1.8F);
                ((TextView) findViewById(R.id.Result)).setText(df.format(tempout) + "  " + tu);
            }else {
                ((TextView) findViewById(R.id.Result)).setText(df.format(res) + "  " + tu);
            }
        } else if (su.equals("MM") || su.equals("Meter")){
            ((TextView) findViewById(R.id.Result)).setText("Please select metric exp unit");
        } else {
            ((TextView) findViewById(R.id.Result)).setText("Please select imperial exp unit");
        }
    }

    public void launchsettings(View v){
        //Launch settings page
        Intent i = new Intent(this, Activity_settings.class);
        startActivity(i);
    }
}