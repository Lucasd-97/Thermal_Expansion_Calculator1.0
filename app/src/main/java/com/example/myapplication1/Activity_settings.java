package com.example.myapplication1;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class Activity_settings extends AppCompatActivity {

    TextView entermetaltype;
    EditText enterexpansion;
    ListView metalLV;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        SharedPreferences sh = getSharedPreferences("MySharedPref", 0);
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

        //Size spinner
        List<String> Sizem = new ArrayList<>();
        Sizem.add("Inch");
        Sizem.add("Foot");
        Sizem.add("Thou IN");
        Sizem.add("MM");
        Sizem.add("Meter");
        Spinner spinnerinmm = findViewById(R.id.sizespinner);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Sizem);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinnerinmm.setAdapter(adapter);
        int sPosition = adapter.getPosition(sizep);
        spinnerinmm.setSelection(sPosition);

        //Temp spinner
        List<String> Temp = new ArrayList<>();
        Temp.add("F");
        Temp.add("C");
        Temp.add("K");
        Spinner TempSpinner = findViewById(R.id.tempspinner);
        ArrayAdapter<String> adapter2 =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Temp);
        adapter2.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        TempSpinner.setAdapter(adapter2);
        int tPosition = adapter2.getPosition(tempp);
        TempSpinner.setSelection(tPosition);

        //Exp spinner
        List<String> exp = new ArrayList<>();
        exp.add("Inch");
        exp.add("MM");
        exp.add("Meter");
        exp.add("Foot");
        exp.add("Thou IN");
        Spinner expSpinner = findViewById(R.id.expspinner);
        ArrayAdapter<String> adapter3 =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, exp);
        adapter3.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        expSpinner.setAdapter(adapter3);
        int ePosition = adapter3.getPosition(expp);
        expSpinner.setSelection(ePosition);


        entermetaltype = findViewById(R.id.entermetaltype);
        enterexpansion = findViewById(R.id.enterexpansion);
        metalLV = findViewById(R.id.metalLV);
        dataBaseHelper = new DataBaseHelper(Activity_settings.this);
        ShowMetalsOnLV(dataBaseHelper);
        metalLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            MetalModel deleteMetal = (MetalModel) adapterView.getItemAtPosition(i);

                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_settings.this);
                builder.setCancelable(true);
                builder.setTitle("Do you want to delete");
                builder.setMessage(deleteMetal.getType() + " : " + deleteMetal.getExpansion());
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dataBaseHelper.deleteOne(deleteMetal);
                                ShowMetalsOnLV(dataBaseHelper);
                                Toast.makeText(Activity_settings.this,"deleted",Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(Activity_settings.this,"Canceled",Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void ShowMetalsOnLV(DataBaseHelper dataBaseHelper) {
        ArrayAdapter<MetalModel> metalAddapter = new ArrayAdapter<>(Activity_settings.this, android.R.layout.simple_list_item_1, dataBaseHelper.getAll());
        metalLV.setAdapter(metalAddapter);
    }

    public void addMetal(View v){
        MetalModel metalModel;
        try{
            metalModel = new MetalModel(-1,entermetaltype.getText().toString(),Float.parseFloat(enterexpansion.getText().toString()));
            Toast.makeText(Activity_settings.this,"attempting to add metal",Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(Activity_settings.this,"error adding metal",Toast.LENGTH_SHORT).show();
            metalModel = new MetalModel(-1,"error",Float.parseFloat("0.000"));
        }
        DataBaseHelper dataBaseHelper = new DataBaseHelper(Activity_settings.this);
        boolean success = dataBaseHelper.addmetal(metalModel);
        if (success){
            Toast.makeText(Activity_settings.this, "Saved", Toast.LENGTH_SHORT).show();
        }
        ShowMetalsOnLV(dataBaseHelper);
    }

    public void viewMetal(View v) {
        try {
            DataBaseHelper dataBaseHelper = new DataBaseHelper(Activity_settings.this);
            ShowMetalsOnLV(dataBaseHelper);
        }
        catch (Exception e){
            Toast.makeText(Activity_settings.this,"Un Problema",Toast.LENGTH_SHORT).show();
        }
    }

    public void pFull(View v) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(Activity_settings.this);
        dataBaseHelper.populateFull();
        ShowMetalsOnLV(dataBaseHelper);
    }

    public void pMetal(View v) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(Activity_settings.this);
        dataBaseHelper.populateMetal();
        ShowMetalsOnLV(dataBaseHelper);
    }

    public void deleteAll(View v) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(Activity_settings.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_settings.this);
        builder.setCancelable(true);
        builder.setTitle("Do you want to delete all?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataBaseHelper.deleteAll();
                        ShowMetalsOnLV(dataBaseHelper);
                        Toast.makeText(Activity_settings.this,"Deleted",Toast.LENGTH_SHORT).show();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Activity_settings.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void savePreferences(View v) {
        Spinner temp1 = findViewById(R.id.tempspinner);
        Spinner size1 = findViewById(R.id.sizespinner);
        Spinner exp1 = findViewById(R.id.expspinner);
        String size = size1.getSelectedItem().toString();
        String temp = temp1.getSelectedItem().toString();
        String exp = exp1.getSelectedItem().toString();

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        // Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        // Storing the key and its value as the data fetched from edittext
        myEdit.putString("size", size);
        myEdit.putString("temp", temp);
        myEdit.putString("exp", exp);

        // Once the changes have been made, we need to commit to apply those changes made,
        // otherwise, it will throw an error
        myEdit.apply();
        Toast.makeText(Activity_settings.this,"Saved",Toast.LENGTH_SHORT).show();
    }
}